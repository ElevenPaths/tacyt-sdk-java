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
    public ExternalApiFilterRequest(RequestType requestType, Filter filter, String content, int page) {
        this.requestType = requestType;
        this.filter = filter;
        this.content = content;
        this.page = page < 1 ? 1 : page;
    }

    public static ExternalApiFilterRequest getCRUDrequest(RequestType requestType, Filter filter) throws IllegalArgumentException {

        if(requestType != RequestType.CREATE && requestType != RequestType.READ
                && requestType != RequestType.UPDATE && requestType != RequestType.DELETE){
            throw new IllegalArgumentException();
        }

        return new ExternalApiFilterRequest(requestType, filter, null, 0);
    }

    public static ExternalApiFilterRequest getSearchPublicFilterRequest(String query, int page){

        return new ExternalApiFilterRequest(RequestType.SEARCH_PUBLIC_FILTER, null, query, page);
    }

    public static ExternalApiFilterRequest getUnsubscribePublicFilterRequest(String filterId){

        return new ExternalApiFilterRequest(RequestType.UNSUBSCRIBE, null, filterId, 0);
    }

    public static ExternalApiFilterRequest getSubscribePublicFilterRequest(String filterId){

        return new ExternalApiFilterRequest(RequestType.SUBSCRIBE, null, filterId, 0);
    }

    public static ExternalApiFilterRequest getRSSrequest(String filterId){

        return new ExternalApiFilterRequest(RequestType.GET_RSS, null, filterId, 0);
    }

    public static ExternalApiFilterRequest getListOfDetections(String filterId, int page){

        return new ExternalApiFilterRequest(RequestType.LIST_DETECTIONS, null, filterId, page);
    }

    public String getJsonEncode() throws UnsupportedEncodingException {
        return new Gson().toJsonTree(this).getAsJsonObject().toString();
    }

}

