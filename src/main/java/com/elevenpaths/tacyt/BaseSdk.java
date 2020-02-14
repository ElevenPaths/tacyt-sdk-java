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
import com.ning.http.util.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

abstract class BaseSdk {

    protected static final String AUTHORIZATION_METHOD = "11PATHS";
    protected static final String AUTHORIZATION_HEADER_FIELD_SEPARATOR = " ";
    protected static final String UTC_STRING_FORMAT = "yyyy-MM-dd HH:mm:ss";
    protected static final String HMAC_ALGORITHM = "HmacSHA1";
    protected static final String CHARSET_ASCII = "US-ASCII";
    protected static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
    protected static final String CHARSET_UTF_8 = "UTF-8";
    protected static final String HTTP_METHOD_GET = "GET";
    protected static final String HTTP_METHOD_POST = "POST";
    protected static final String HTTP_METHOD_PUT = "PUT";
    protected static final String HTTP_METHOD_DELETE = "DELETE";
    protected static final String HTTP_HEADER_CONTENT_LENGTH = "Content-Length";
    protected static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
    protected static final String HTTP_HEADER_CONTENT_TYPE_FORM_URLENCODED = "application/x-www-form-urlencoded";
    protected static final String HTTP_HEADER_CONTENT_TYPE_JSON = "application/json";
    protected static final String PARAM_SEPARATOR = "&";
    protected static final String PARAM_VALUE_SEPARATOR = "=";
    protected static final String X_11PATHS_HEADER_PREFIX = "X-11paths-";
    protected static final String X_11PATHS_HEADER_SEPARATOR = ":";
    protected static final String BODY_HASH_HEADER_NAME = X_11PATHS_HEADER_PREFIX + "Body-Hash";
    protected static final String FILE_HASH_HEADER_NAME = X_11PATHS_HEADER_PREFIX + "File-Hash";
    protected static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    protected static final String DATE_HEADER_NAME = X_11PATHS_HEADER_PREFIX + "Date";

    public static String API_HOST;

    public static void setHost(String host) {
        API_HOST = host;
    }

    protected String appId;
    protected String secretKey;

    public BaseSdk(String appId, String secretKey) {
        this.appId = appId;
        this.secretKey = secretKey;
    }

    protected abstract JsonElement HTTP(String URL, String method, Map<String, String> headers, String body, File file);

    /**
     * The custom header consists of three parts, the method, the appId and the signature
     * This method returns the specified part if it exists.
     *
     * @param part   The zero indexed part to be returned
     * @param header The HTTP header value from which to extract the part
     * @return the specified part from the header or an empty string if not existent
     */
    protected static final String getPartFromHeader(int part, String header) {
        if (header != null) {
            String[] parts = header.split(AUTHORIZATION_HEADER_FIELD_SEPARATOR);
            if (parts.length > part) {
                return parts[part];
            }
        }
        return "";
    }

    /**
     * @param authorizationHeader Authorization HTTP Header
     * @return the Authorization method. Typical values are "Basic", "Digest" or "11PATHS"
     */
    protected static final String getAuthMethodFromHeader(String authorizationHeader) {
        return getPartFromHeader(0, authorizationHeader);
    }

    /**
     * @param authorizationHeader Authorization HTTP Header
     * @return the requesting application Id. Identifies the application using the API
     */
    protected static final String getAppIdFromHeader(String authorizationHeader) {
        return getPartFromHeader(1, authorizationHeader);
    }

    /**
     * @param authorizationHeader Authorization HTTP Header
     * @return the signature of the current request. Verifies the identity of the application using the API
     */
    protected static final String getSignatureFromHeader(String authorizationHeader) {
        return getPartFromHeader(2, authorizationHeader);
    }

    protected JsonElement HTTP_GET(String URL, Map<String, String> headers) {
        return HTTP(URL, HTTP_METHOD_GET, headers, null, null);
    }

    protected JsonElement HTTP_POST(String URL, Map<String, String> headers, String body) {
        return HTTP(URL, HTTP_METHOD_POST, headers, body, null);
    }

    protected JsonElement HTTP_PUT(String URL, Map<String, String> headers, String body) {
        return HTTP(URL, HTTP_METHOD_PUT, headers, body, null);
    }

    protected JsonElement HTTP_POST_FILE(String URL, Map<String, String> headers, File file, String tagName) {
        return HTTP(URL, HTTP_METHOD_POST, headers, tagName, file);
    }

    protected TacytResponse HTTP_GET_proxy(String url) {
        try {
            return new TacytResponse(HTTP_GET(API_HOST + url, authenticationHeaders(HTTP_METHOD_GET, url, null, null)));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    protected TacytResponse HTTP_POST_proxy(String url) throws UnsupportedEncodingException {
        return HTTP_POST_proxy(url, "");
    }

    protected TacytResponse HTTP_POST_proxy(String url, String body) throws UnsupportedEncodingException {
        return new TacytResponse(HTTP_POST(API_HOST + url, authenticationHeadersWithBody(HTTP_METHOD_POST, url, null, body), body));
    }

    protected TacytResponse HTTP_PUT_proxy(String url, String body) throws UnsupportedEncodingException {
        return new TacytResponse(HTTP_PUT(API_HOST + url, authenticationHeadersWithBody(HTTP_METHOD_PUT, url, null, body), body));
    }

    /**
     * @param data the string to sign
     * @return base64 encoding of the HMAC-SHA1 hash of the data parameter using {@code secretKey} as cipher key.
     */
    protected String signData(String data) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), HMAC_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(keySpec);
            return Base64.encode(mac.doFinal(data.getBytes(CHARSET_ISO_8859_1))); // data is ASCII except HTTP header values which can be ISO_8859_1
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Calculates the headers to be sent with a request to the API so the server
     * can verify the signature
     * <p>
     * Calls {@link #authenticationHeaders(String, String, Map, Map, String)}
     * with the current date as {@code utc}.
     *
     * @param method      The HTTP request method.
     * @param querystring The urlencoded string including the path (from the
     *                    first forward slash) and the parameters.
     * @param xHeaders    The HTTP request headers specific to the API, excluding
     *                    X-11Paths-Date. null if not needed.
     * @param params      The HTTP request params. Must be only those to be sent in
     *                    the body of the request and must be urldecoded. null if not
     *                    needed.
     * @return A map with the {@value AUTHORIZATION_HEADER_NAME} and {@value
     * DATE_HEADER_NAME} headers needed to be sent with a request to the
     * API.
     * @throws UnsupportedEncodingException If {@value CHARSET_UTF_8} charset is
     *                                      not supported.
     */
    protected final Map<String, String> authenticationHeaders(String method, String querystring, Map<String, String> xHeaders, Map<String, String> params) throws UnsupportedEncodingException {
        return authenticationHeaders(method, querystring, xHeaders, params, getCurrentUTC());
    }


    protected final Map<String, String> authenticationHeadersWithBody(String method, String querystring, Map<String, String> xHeaders, String body) throws UnsupportedEncodingException {
        String currentUTC = getCurrentUTC();
        return authenticationHeadersWithBody(method, querystring, xHeaders, body.getBytes(CHARSET_UTF_8), currentUTC);
    }

    /**
     * Calculate the authentication headers to be sent with a request to the API
     *
     * @param HTTPMethod  the HTTP Method
     * @param queryString the urlencoded string including the path (from the first forward slash) and the parameters
     * @param xHeaders    HTTP headers specific to the 11-paths API, excluding X-11Paths-Date. null if not needed.
     * @param params      The HTTP request params. Must be only those to be sent in the body of the request and must be urldecoded. null if not needed.
     * @param utc         the Universal Coordinated Time for the X-11Paths-Date HTTP header
     * @return a map with the Authorization and X-11Paths-Date headers needed to sign a Path5 API request
     * @throws java.io.UnsupportedEncodingException If {@value CHARSET_UTF_8} charset is not supported.
     */
    protected final Map<String, String> authenticationHeaders(String HTTPMethod, String queryString, Map<String, String> xHeaders, Map<String, String> params, String utc) throws UnsupportedEncodingException {
        StringBuilder stringToSign = new StringBuilder();
        stringToSign.append(HTTPMethod.toUpperCase().trim());
        stringToSign.append("\n");
        stringToSign.append(utc);
        stringToSign.append("\n");
        stringToSign.append(getSerializedHeaders(xHeaders));
        stringToSign.append("\n");
        stringToSign.append(sortQueryString(queryString.trim()));
        if (params != null && !params.isEmpty()) {
            String serializedParams = getSerializedParams(params);
            if (serializedParams != null && !serializedParams.isEmpty()) {
                stringToSign.append("\n");
                stringToSign.append(serializedParams);
            }
        }

        String signedData = signData(stringToSign.toString());
        String authorizationHeader = new StringBuilder(AUTHORIZATION_METHOD)
                .append(AUTHORIZATION_HEADER_FIELD_SEPARATOR)
                .append(this.appId)
                .append(AUTHORIZATION_HEADER_FIELD_SEPARATOR)
                .append(signedData)
                .toString();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(AUTHORIZATION_HEADER_NAME, authorizationHeader);
        headers.put(DATE_HEADER_NAME, utc);
        return headers;
    }

    /**
     * Calculates the headers to be sent with a request to the API so the server
     * can verify the signature
     *
     * @param method      The HTTP request method.
     * @param querystring The urlencoded string including the path (from the
     *                    first forward slash) and the parameters.
     * @param xHeaders    The HTTP request headers specific to the API, excluding
     *                    X-11Paths-Date. null if not needed.
     * @param body        The HTTP request body. Null if not needed.
     * @param utc         the Universal Coordinated Time for the X-11Paths-Date HTTP
     *                    header
     * @return A map with the {@value AUTHORIZATION_HEADER_NAME}, the {@value
     * DATE_HEADER_NAME} and the {@value BODY_HASH_HEADER_NAME} headers
     * needed to be sent with a request to the API.
     * @throws java.io.UnsupportedEncodingException If {@value CHARSET_UTF_8} charset is
     *                                              not supported.
     */
    protected final Map<String, String> authenticationHeadersWithBody(String method, String querystring, Map<String, String> xHeaders, byte[] body, String utc) throws UnsupportedEncodingException {

        HashMap<String, String> headers = new HashMap<String, String>();

        String bodyHash = null;
        if (body != null) {
            bodyHash = DigestUtils.sha1Hex(body);
            if (xHeaders == null) {
                xHeaders = new HashMap<String, String>();

            } else {
                for (String header : xHeaders.keySet()) {
                    headers.put(header, xHeaders.get(header));
                }
            }
            xHeaders.put(BODY_HASH_HEADER_NAME, bodyHash);
        }
        StringBuilder stringToSign = new StringBuilder();
        stringToSign.append(method.toUpperCase().trim());
        stringToSign.append("\n");
        stringToSign.append(utc);
        stringToSign.append("\n");
        stringToSign.append(getSerializedHeaders(xHeaders));
        stringToSign.append("\n");
        stringToSign.append(sortQueryString(querystring.trim()));
        String signedData = signData(stringToSign.toString());
        String authorizationHeader = new StringBuilder(AUTHORIZATION_METHOD)
                .append(AUTHORIZATION_HEADER_FIELD_SEPARATOR)
                .append(this.appId)
                .append(AUTHORIZATION_HEADER_FIELD_SEPARATOR)
                .append(signedData)
                .toString();
        headers.put(AUTHORIZATION_HEADER_NAME, authorizationHeader);
        headers.put(DATE_HEADER_NAME, utc);
        if (bodyHash != null) {
            headers.put(BODY_HASH_HEADER_NAME, bodyHash);
        }
        return headers;
    }

    /**
     * Prepares and returns a string ready to be signed from the 11-paths specific HTTP headers received
     *
     * @param xHeaders a non necessarily ordered map of the HTTP headers to be ordered without duplicates.
     * @return a String with the serialized headers, an empty string if no headers are passed, or null if there's a problem
     * such as non specific 11paths headers
     */
    protected String getSerializedHeaders(Map<String, String> xHeaders) {
        if (xHeaders != null) {
            TreeMap<String, String> sortedMap = new TreeMap<String, String>();
            for (String key : xHeaders.keySet()) {
                if (!key.toLowerCase().startsWith(X_11PATHS_HEADER_PREFIX.toLowerCase())) {
                    System.out.println("Error serializing headers. Only specific " + X_11PATHS_HEADER_PREFIX + " headers need to be signed");
                }
                sortedMap.put(key.toLowerCase(), xHeaders.get(key));
            }
            StringBuilder serializedHeaders = new StringBuilder();
            for (String key : sortedMap.keySet()) {
                serializedHeaders.append(key).append(X_11PATHS_HEADER_SEPARATOR).append(sortedMap.get(key)).append(" ");
            }
            return serializedHeaders.toString().trim();
        } else {
            return "";
        }
    }

    /**
     * Prepares and returns a string ready to be signed from the params of an
     * HTTP request
     * <p>
     * The params must be only those included in the body of the HTTP request
     * when its content type is application/x-www-urlencoded and must be
     * urldecoded.
     *
     * @param params The params of an HTTP request.
     * @return A serialized representation of the params ready to be signed.
     * null if there are no valid params.
     * @throws UnsupportedEncodingException If {@value CHARSET_UTF_8} charset is
     *                                      not supported.
     */
    protected String getSerializedParams(Map<String, String> params) throws UnsupportedEncodingException {
        String rv = null;
        if (params != null && !params.isEmpty()) {
            TreeMap<String, String> sortedParams = new TreeMap<String, String>();
            for (String key : params.keySet()) {
                if (key != null && params.get(key) != null) {
                    sortedParams.put(key, params.get(key));
                }
            }
            StringBuilder serializedParams = new StringBuilder();
            for (String key : sortedParams.keySet()) {
                serializedParams.append(URLEncoder.encode(key, CHARSET_UTF_8));
                serializedParams.append(PARAM_VALUE_SEPARATOR);
                serializedParams.append(URLEncoder.encode(sortedParams.get(key), CHARSET_UTF_8));
                if (!key.equals(sortedParams.lastKey())) {
                    serializedParams.append(PARAM_SEPARATOR);
                }
            }
            if (serializedParams.length() > 0) {
                rv = serializedParams.toString();
            }
        }
        return rv;
    }

    /**
     * @return a string representation of the current time in UTC to be used in a Date HTTP Header
     */
    protected final String getCurrentUTC() {
        final SimpleDateFormat sdf = new SimpleDateFormat(UTC_STRING_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());

    }

    private String sortQueryString(String queryString) {
        if (queryString == null || queryString.isEmpty()) {
            return "";
        }

        if (queryString.contains("?")) {
            String path = queryString.substring(0, queryString.indexOf("?") + 1);

            queryString = queryString.replace(path, "");
            List<String> params = Arrays.asList(queryString.split(PARAM_SEPARATOR));
            Collections.sort(params);

            queryString = path + StringUtils.join(params, PARAM_SEPARATOR);
        }

        return queryString;
    }
}
