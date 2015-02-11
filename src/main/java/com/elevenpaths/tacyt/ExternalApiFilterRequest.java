/*Path5 Java SDK - Set of  reusable classes to  allow developers integrate Path5 on their applications.
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

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

public class ExternalApiFilterRequest extends ExternalApiRequest {

    public static final String JSON_FIELD_OPERATION = "operation";
    public static final String JSON_FIELD_DETECTIONS = "detections";

    public static final String FIELD_FILTER = "filter";
    public static final String FIELD_FILTERS = "filters";
    public static final String FIELD_ID = "id";
    public static final String FIELD_TOTAL_COUNT = "totalCount";
    public static final String FIELD_LINK = "link";
    public static final String FIELD_USER = "user";
    public static final String FIELD_PASS = "pass";

    public enum RequestType {
        CREATE,
        READ,
        UPDATE,
        DELETE,
        SEARCH_PUBLIC_FILTER,
        SUBSCRIBE,
        UNSUBSCRIBE,
        GET_RSS,
        LIST_DETECTIONS
    }

    private RequestType requestType;
    private Filter filter;
    private String content;
    private Integer page;

    public RequestType getRequestType() {
        return requestType;
    }

    public Filter getFilter() {
        return filter;
    }

    public String getContent() {
        return content;
    }

    public int getPage() {
        return page;
    }

    public ExternalApiFilterRequest() {}

    public static ExternalApiFilterRequest getCRUDrequest(RequestType requestType, Filter filter) throws IllegalArgumentException {

        if(requestType != RequestType.CREATE && requestType != RequestType.READ
                && requestType != RequestType.UPDATE && requestType != RequestType.DELETE){
            throw new IllegalArgumentException("Invalid request type");
        }

        ExternalApiFilterRequest result = new ExternalApiFilterRequest();
        result.requestType = requestType;
        result.filter = filter;
        return result;
    }

    public static ExternalApiFilterRequest getSearchPublicFilterRequest(String query, int page){

        ExternalApiFilterRequest result = new ExternalApiFilterRequest();
        result.requestType = RequestType.SEARCH_PUBLIC_FILTER;
        result.content = query;
        result.page = page;
        return result;
    }

    public static ExternalApiFilterRequest getUnsubscribePublicFilterRequest(String filterId){

        ExternalApiFilterRequest result = new ExternalApiFilterRequest();
        result.requestType = RequestType.UNSUBSCRIBE;
        result.content = filterId;
        return result;
    }

    public static ExternalApiFilterRequest getSubscribePublicFilterRequest(String filterId){

        ExternalApiFilterRequest result = new ExternalApiFilterRequest();
        result.requestType = RequestType.SUBSCRIBE;
        result.content = filterId;
        return result;
    }

    public static ExternalApiFilterRequest getRSSrequest(String filterId){

        ExternalApiFilterRequest result = new ExternalApiFilterRequest();
        result.requestType = RequestType.GET_RSS;
        result.content = filterId;
        return result;
    }

    public static ExternalApiFilterRequest getListOfDetections(String filterId, int page){

        ExternalApiFilterRequest result = new ExternalApiFilterRequest();
        result.requestType = RequestType.LIST_DETECTIONS;
        result.content = filterId;
        result.page = page;
        return result;
    }

    public String getJsonEncode() throws UnsupportedEncodingException {
        return new Gson().toJsonTree(this).getAsJsonObject().toString();
    }

}

