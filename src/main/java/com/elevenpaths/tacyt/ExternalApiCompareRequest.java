package com.elevenpaths.tacyt;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class ExternalApiCompareRequest {

    public final static String FIELD_MATCHING_FIELDS = "matchingFields";
    public final static String FIELD_MATCHING_QUERY = "matchingQuery";
    public final static String FIELD_APPS = "apps";

    private List<String> apps;
    private boolean includeDetails;

    public List<String> getApps() {
        return apps;
    }

    public void setApps(List<String> apps) {
        this.apps = apps;
    }

    public boolean isIncludeDetails() {
        return includeDetails;
    }

    public void setIncludeDetails(boolean includeDetails) {
        this.includeDetails = includeDetails;
    }

    public ExternalApiCompareRequest(List<String> apps, boolean includeDetails) {
        this.apps = apps;
        this.includeDetails = includeDetails;
    }

    public String getJsonEncode() throws UnsupportedEncodingException {
        return new Gson().toJsonTree(this).getAsJsonObject().toString();
    }

}
