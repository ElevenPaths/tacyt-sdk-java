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

import java.io.UnsupportedEncodingException;

public class ExternalApiSearchRequest extends ExternalApiRequest {

    private String query;
    private int numberPage;
    private int maxResults;
    private String outputFields;
    private boolean grouped;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getNumberPage() {
        return numberPage;
    }

    public void setNumberPage(int numberPage) {
        this.numberPage = numberPage;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public String getOutputFields() {
        return outputFields;
    }

    public void setOutputFields(String outputFields) {
        this.outputFields = outputFields;
    }

    public boolean isGrouped() {
        return grouped;
    }

    public void setGrouped(boolean grouped) {
        this.grouped = grouped;
    }

    public ExternalApiSearchRequest(String query) {
        this.query = query;
        this.numberPage = 1;
        this.maxResults = 20;
        this.outputFields = null;
        this.grouped = false;
    }

    public ExternalApiSearchRequest(String query, int numberPage, int maxResults, String[] outputFields, boolean grouped) {
        this.query = query;
        this.numberPage = numberPage > 0 ? numberPage : 1;
        this.maxResults = maxResults > 0 ? maxResults : 20;
        this.grouped = grouped;

        if(outputFields != null && outputFields.length > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for(String field : outputFields){
                stringBuilder.append(field + ",");
            }
            this.outputFields = stringBuilder.toString();
        }
    }

    public String getJsonEncode() throws UnsupportedEncodingException {

        return Utils.getGsonParser().toJsonTree(this).getAsJsonObject().toString();
    }

    public static ExternalApiSearchRequest getFromJson(String json) {

        ExternalApiSearchRequest result;

        try {
            result = Utils.getGsonParser().fromJson(json, ExternalApiSearchRequest.class);

        } catch (Exception e){
            throw new IllegalArgumentException();
        }

        if(result.getQuery() == null || result.getQuery().isEmpty()){
            throw new IllegalArgumentException();
        }

        return result;
    }
}
