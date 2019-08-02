package com.elevenpaths.tacyt;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.Set;

public class ExternalApiUploadURLRequest {


    public final static String FIELD_APPS = "apps";

    private Set<String> urls;
    private Set<String> tagNames;

    public ExternalApiUploadURLRequest(Set<String> urls, Set<String> tagNames) {
        this.urls = urls;
        this.tagNames = tagNames;
    }

    public ExternalApiUploadURLRequest(Set<String> urls) {
        this.urls = urls;
        this.tagNames = null;
    }

    public String getJsonEncode() throws UnsupportedEncodingException {
        return new Gson().toJsonTree(this).getAsJsonObject().toString();
    }

    public Set<String> getUrls() {
        return urls;
    }

    public void setUrls(Set<String> urls) {
        this.urls = urls;
    }

    public Set<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(Set<String> tagNames) {
        this.tagNames = tagNames;
    }
}
