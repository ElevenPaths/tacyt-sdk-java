/*Tacyt Java SDK - Set of  reusable classes to  allow developers integrate Tacyt on their applications.
Copyright (C) 2013 Eleven Paths

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA*/

package com.elevenpaths.tacyt;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.*;


public class Tacyt extends BaseSdk {

    protected static final String API_VERSION = "2.10";
    protected static final String API_SEARCH_URL = "/api/" + API_VERSION + "/search";
    protected static final String API_TAGS_URL = "/api/" + API_VERSION + "/tags";
    protected static final String API_DETAILS_URL = "/api/" + API_VERSION + "/details";
    protected static final String API_FILTERS_URL = "/api/" + API_VERSION + "/filters";
    protected static final String API_COMPARER_URL = "/api/" + API_VERSION + "/compare";
    protected static final String API_UPLOAD_URL = "/api/" + API_VERSION + "/upload";
    protected static final String API_ENGINE_VERSION_URL = "/api/" + API_VERSION + "/engineVersion";
    protected static final String API_UPLOADURL_URL = "/api/" + API_VERSION + "/uploadURL";
    protected static final String API_APPLICATION = "/api/" + API_VERSION + "/app";

    /**
     * Create an instance of the class with the Application ID and secret obtained from Eleven Paths
     *
     * @param appId
     * @param secretKey
     */
    public Tacyt(String appId, String secretKey) {
        super(appId, secretKey);
        BaseSdk.API_HOST = "https://tacyt.elevenpaths.com";
    }


    /**
     * Makes an HTTP request
     *
     * @param URL     The request URL.
     * @param method  The request method.
     * @param headers Headers to add to the HTTP request.
     * @param body    The HTTP request body.
     * @return The server's JSON response or null if something has gone wrong.
     */
    protected JsonElement HTTP(String URL, String method, Map<String, String> headers, String body, File file) {
        JsonElement rv = null;
        InputStream is = null;
        OutputStream os = null;
        InputStreamReader isr = null;

        try {

            URL theURL = new URL(URL);
            HttpURLConnection theConnection = (HttpURLConnection) theURL.openConnection();

            theConnection.setRequestMethod(method);

            if (headers != null && !headers.isEmpty()) {
                Iterator<String> iterator = headers.keySet().iterator();
                while (iterator.hasNext()) {
                    String headerName = iterator.next();
                    theConnection.setRequestProperty(headerName, headers.get(headerName));
                }
            }

            if (HTTP_METHOD_POST.equals(method) || HTTP_METHOD_PUT.equals(method)) {
                if (body != null && file == null) {
                    byte[] bodyBytes = body.getBytes(CHARSET_UTF_8);
                    theConnection.setRequestProperty(HTTP_HEADER_CONTENT_TYPE, HTTP_HEADER_CONTENT_TYPE_JSON);
                    theConnection.setRequestProperty(HTTP_HEADER_CONTENT_LENGTH, String.valueOf(bodyBytes.length));
                    theConnection.setDoOutput(true);
                    os = theConnection.getOutputStream();
                    os.write(bodyBytes);
                    os.flush();
                }

                if (file != null) {
                    String boundary = Long.toHexString(System.currentTimeMillis());
                    String CRLF = "\r\n";
                    theConnection.setDoOutput(true);
                    theConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                    os = theConnection.getOutputStream();

                    PrintWriter writer = new PrintWriter(os, true);

                    if (body != null) {

                        JsonParser jsonParser = new JsonParser();
                        JsonObject jsonObj = jsonParser.parse(body.toString()).getAsJsonObject();

                        for (Map.Entry entry : jsonObj.entrySet()) {
                            writer.append("--" + boundary).append(CRLF);
                            writer.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"").append(CRLF).append(CRLF);
                            writer.append(entry.getValue().toString().replaceAll("\"", "")).append(CRLF);
                        }
                    }

                    writer.append("--" + boundary).append(CRLF);
                    writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"").append(CRLF);
                    writer.append("Content-Type: application/octet-stream").append(CRLF);
                    writer.append("Content-Transfer-Encoding: binary").append(CRLF);
                    writer.append(CRLF).flush();
                    Files.copy(file.toPath(), os);
                    os.flush();
                    writer.append(CRLF).flush();

                    // End of multipart/form-data.
                    writer.append("--" + boundary + "--").append(CRLF).flush();
                }
            }

            JsonParser parser = new JsonParser();
            is = theConnection.getInputStream();
            isr = new InputStreamReader(is, CHARSET_UTF_8);
            rv = parser.parse(isr);

        } catch (MalformedURLException e) {
            System.err.println("The URL is malformed (" + URL + ")");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("An exception has been thrown when communicating with Tacyt backend");
            e.printStackTrace();
        } finally {

            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    System.err.println("An exception has been thrown when trying to close the output stream");
                    e.printStackTrace();
                }
            }

            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    System.err.println("An exception has been thrown when trying to close the input stream reader");
                    e.printStackTrace();
                }
            }

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    System.err.println("An exception has been thrown when trying to close the input stream");
                    e.printStackTrace();
                }
            }

        }

        return rv;

    }


    /**
     * Get all the details for an app: files, images and comments
     *
     * @param appKey The value returned by a generic search response for an app, for example: com.elevenpaths.android11GooglePlay
     * @return TacytResponse object with the json of all the details of the app
     */
    public TacytResponse getAppDetails(String appKey) {
        return HTTP_GET_proxy(new StringBuilder(API_DETAILS_URL).append("/").append(appKey).toString());
    }

    /**
     * Perform a generic search to Tacyt
     *
     * @param query      The query as a json string for the search. Please visit Tacyt website for reference and examples.
     * @param numberPage A number greater or equal to 1 indicating the page of results which have to be retrieved.
     * @param maxResults A number between 1 and 100 indicating the max number of apps which have to be retrieved.
     * @return
     */
    public TacytResponse searchApps(String query, int numberPage, int maxResults, String[] outputFields, boolean grouped) {
        ExternalApiSearchRequest searchQuery = new ExternalApiSearchRequest(query, numberPage, maxResults, outputFields, grouped);
        try {
            return HTTP_POST_proxy(new StringBuilder(API_SEARCH_URL).toString(), searchQuery.getJsonEncode());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Returns the list of all tags
     *
     * @return
     */
    public TacytResponse listTags() {
        ExternalApiTagRequest tagRequest = ExternalApiTagRequest.list();
        try {
            return HTTP_POST_proxy(new StringBuilder(API_TAGS_URL).toString(), tagRequest.getJsonEncode());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Assign the tag passed as parameter to an app or set of apps
     *
     * @param tag     Name of the tag to assign to the apps
     * @param appKeys Set of apps to apply the tag
     * @return
     */
    public TacytResponse assignTag(String tag, List<String> appKeys) {
        ExternalApiTagRequest tagRequest = ExternalApiTagRequest.assignTag(tag, appKeys);
        try {
            return HTTP_POST_proxy(new StringBuilder(API_TAGS_URL).toString(), tagRequest.getJsonEncode());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Remove the tag passed as parameter to an app or set of apps
     *
     * @param tag     Name of the tag to remove
     * @param appKeys Set of apps to remove the tag
     * @return
     */
    public TacytResponse removeTagForApps(String tag, List<String> appKeys) {
        ExternalApiTagRequest tagRequest = ExternalApiTagRequest.removeTagFromApp(tag, appKeys);
        try {
            return HTTP_POST_proxy(new StringBuilder(API_TAGS_URL).toString(), tagRequest.getJsonEncode());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Remove the tag and all its assigned apps
     *
     * @param tag Name of the tag to remove
     * @return
     */
    public TacytResponse deleteTag(String tag) {
        ExternalApiTagRequest tagRequest = ExternalApiTagRequest.removeTag(tag);
        try {
            return HTTP_POST_proxy(new StringBuilder(API_TAGS_URL).toString(), tagRequest.getJsonEncode());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Creates a filter
     *
     * @param filter
     * @return
     */
    public TacytResponse createFilter(Filter filter) {
        ExternalApiFilterRequest result = ExternalApiFilterRequest.getCRUDrequest(ExternalApiFilterRequest.RequestType.CREATE, filter);
        try {
            return HTTP_POST_proxy(new StringBuilder(API_FILTERS_URL).toString(), result.getJsonEncode());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Updates a given filter
     *
     * @param filter
     * @return
     */
    public TacytResponse updateFilter(Filter filter) {
        ExternalApiFilterRequest result = ExternalApiFilterRequest.getCRUDrequest(ExternalApiFilterRequest.RequestType.UPDATE, filter);
        try {
            return HTTP_POST_proxy(new StringBuilder(API_FILTERS_URL).toString(), result.getJsonEncode());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Returns all the created filters
     *
     * @return
     */
    public TacytResponse readGroupFilters() {
        ExternalApiFilterRequest result = ExternalApiFilterRequest.getGroups();
        try {
            return HTTP_POST_proxy(new StringBuilder(API_FILTERS_URL).toString(), result.getJsonEncode());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns all the created filters
     *
     * @return
     */
    public TacytResponse readAllFilters() {
        ExternalApiFilterRequest result = ExternalApiFilterRequest.getCRUDrequest(ExternalApiFilterRequest.RequestType.READ, null);
        try {
            return HTTP_POST_proxy(new StringBuilder(API_FILTERS_URL).toString(), result.getJsonEncode());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Read the properties of one single filter
     *
     * @param filterId ID of the filter
     * @return
     */
    public TacytResponse readOneFilter(String filterId) {
        ExternalApiFilterRequest result = ExternalApiFilterRequest.getCRUDrequest(ExternalApiFilterRequest.RequestType.READ, new Filter(filterId));
        try {
            return HTTP_POST_proxy(new StringBuilder(API_FILTERS_URL).toString(), result.getJsonEncode());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Deletes a filter
     *
     * @param filterId ID of the filter
     * @return
     */
    public TacytResponse deleteFilter(String filterId) {
        ExternalApiFilterRequest result = ExternalApiFilterRequest.getCRUDrequest(ExternalApiFilterRequest.RequestType.DELETE, new Filter(filterId));
        try {
            return HTTP_POST_proxy(new StringBuilder(API_FILTERS_URL).toString(), result.getJsonEncode());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * List detected apps by a filter
     *
     * @param filterId ID of the filter
     * @return
     */
    public TacytResponse listDetectedApps(String filterId, int page) {
        ExternalApiFilterRequest result = ExternalApiFilterRequest.getListOfDetections(filterId, page);
        try {
            return HTTP_POST_proxy(new StringBuilder(API_FILTERS_URL).toString(), result.getJsonEncode());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * List detected apps by a filter group
     *
     * @param groupName ID of the filter
     * @param page      Used for pagination, first page is 1
     * @return
     */
    public TacytResponse listGroupDetectedApps(String groupName, int page) {
        ExternalApiFilterRequest result = ExternalApiFilterRequest.getListOfGroupDetections(groupName, page);
        try {
            return HTTP_POST_proxy(new StringBuilder(API_FILTERS_URL).toString(), result.getJsonEncode());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Search by public filters defined by other users of the system
     *
     * @param query Content to look for in the public filters
     * @param page  Used for pagination, first page is 1
     * @return
     */
    public TacytResponse searchPublicFilter(String query, int page) {
        ExternalApiFilterRequest result = ExternalApiFilterRequest.getSearchPublicFilterRequest(query, page);
        try {
            return HTTP_POST_proxy(new StringBuilder(API_FILTERS_URL).toString(), result.getJsonEncode());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Remove the subscription to a filter
     *
     * @param filterId ID of the filter
     * @return
     */
    public TacytResponse unsubscribePublicFilter(String filterId) {
        ExternalApiFilterRequest result = ExternalApiFilterRequest.getUnsubscribePublicFilterRequest(filterId);
        try {
            return HTTP_POST_proxy(new StringBuilder(API_FILTERS_URL).toString(), result.getJsonEncode());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Subscribe to a public filter given a filter
     *
     * @param filterId ID of the filter
     * @return
     */
    public TacytResponse subscribePublicFilter(String filterId) {
        ExternalApiFilterRequest result = ExternalApiFilterRequest.getSubscribePublicFilterRequest(filterId);
        try {
            return HTTP_POST_proxy(new StringBuilder(API_FILTERS_URL).toString(), result.getJsonEncode());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Returns the info of the RSS given a filter
     *
     * @param filterId ID of the filter
     * @return
     */
    public TacytResponse getRSSinfo(String filterId) {
        ExternalApiFilterRequest result = ExternalApiFilterRequest.getRSSrequest(filterId);
        try {
            return HTTP_POST_proxy(new StringBuilder(API_FILTERS_URL).toString(), result.getJsonEncode());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Returns the info of the RSS given a filter group name
     *
     * @param groupName Name of the group
     * @return
     */
    public TacytResponse getGroupRSSinfo(String groupName) {
        ExternalApiFilterRequest result = ExternalApiFilterRequest.getGroupRSS(groupName);
        try {
            return HTTP_POST_proxy(new StringBuilder(API_FILTERS_URL).toString(), result.getJsonEncode());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Returns the info of the RSS given a filter
     *
     * @param apps           List of apps to compare
     * @param includeDetails TRUE if you want to include the details of the compared apps in the result
     * @return
     */
    public TacytResponse compareApps(List<String> apps, boolean includeDetails) {
        ExternalApiCompareRequest result = new ExternalApiCompareRequest(apps, includeDetails);
        try {
            return HTTP_POST_proxy(new StringBuilder(API_COMPARER_URL).toString(), result.getJsonEncode());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public TacytResponse uploadApp(File file, String tagName, Date sendToAVDate) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put(FILE_HASH_HEADER_NAME, DigestUtils.sha1Hex(Files.readAllBytes(file.toPath())));
            return new TacytResponse(HTTP_POST_FILE(API_HOST + API_UPLOAD_URL, authenticationHeadersWithBody(HTTP_METHOD_POST, API_UPLOAD_URL, headers, ""), file, tagName, sendToAVDate));
        } catch (UnsupportedEncodingException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Search an engine and its associated vulnerabilities. If no params return a list of all existing engines
     *
     * @param engineId engine id.
     * @param date     search the engine available on that date
     * @param lang     output language of vulnerabilities fields. Values "es" or "en"
     * @return
     */
    public TacytResponse getEngineVersion(String engineId, String date, String lang) {

        StringBuilder searchQuery = new StringBuilder(API_ENGINE_VERSION_URL);

        int numParams = 0;

        try {
            if (engineId != null && !engineId.isEmpty()) {
                searchQuery.append((numParams > 0) ? "&" : "?").append("engineId=").append(URLEncoder.encode(engineId, "UTF-8"));
                numParams++;
            }
            if (date != null && !date.isEmpty()) {
                searchQuery.append((numParams > 0) ? "&" : "?").append("date=").append(URLEncoder.encode(date, "UTF-8"));
                numParams++;
            }
            if (lang != null && !lang.isEmpty()) {
                searchQuery.append((numParams > 0) ? "&" : "?").append("lang=").append(lang);
                numParams++;
            }
        } catch (UnsupportedEncodingException e) {
            return null;
        }

        return HTTP_GET_proxy(searchQuery.toString());
    }

    /**
     * Upload and analyze vulnerabilities and behaviors immediately of applications list. Urls must be Google Play or Apple Store links
     *
     * @param urls     List of urls to upload
     * @param tagNames List of tags to identify the application
     * @return
     */
    public TacytResponse uploadURL(Set<String> urls, Set<String> tagNames) {
        ExternalApiUploadURLRequest result = new ExternalApiUploadURLRequest(urls, tagNames);
        try {
            return HTTP_POST_proxy(new StringBuilder(API_UPLOADURL_URL).toString(), result.getJsonEncode());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Add the metadata of an application
     *
     * @param application Object with all fields of the new application
     * @return
     */
    public TacytResponse addApplication(ExternalApiAplicationRequest application) {
        try {
            return HTTP_POST_proxy(new StringBuilder(API_APPLICATION).toString(), application.getJsonEncode());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Update the metadata of an application
     *
     * @param application Object with all fields of the application to be modified
     * @return
     */
    public TacytResponse updateApplication(ExternalApiAplicationRequest application) {
        try {
            return HTTP_PUT_proxy(new StringBuilder(API_APPLICATION).toString(), application.getJsonEncode());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
