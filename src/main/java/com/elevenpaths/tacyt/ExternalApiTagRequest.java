package com.elevenpaths.tacyt;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class ExternalApiTagRequest {

    public enum enmRequestType{
        LIST,
        CREATE,
        REMOVE,
        REMOVE_ALL
    }

    private enmRequestType requestType;
    private String tag;
    private List<String> apps;

    public enmRequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(enmRequestType requestType) {
        this.requestType = requestType;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<String> getApps() {
        return apps;
    }

    public void setApps(List<String> apps) {
        this.apps = apps;
    }

    private ExternalApiTagRequest(enmRequestType requestType, String tag, List<String> apps) {
        this.requestType = requestType;
        this.tag = tag;
        this.apps = apps;
    }

    public static ExternalApiTagRequest list(){
        return new ExternalApiTagRequest(enmRequestType.LIST, null, null);
    }

    public static ExternalApiTagRequest assignTag(String tag, List<String> apps){
        return new ExternalApiTagRequest(enmRequestType.CREATE, tag, apps);
    }

    public static ExternalApiTagRequest removeTagFromApp(String tag, List<String> apps){
        return new ExternalApiTagRequest(enmRequestType.REMOVE, tag, apps);
    }

    public static ExternalApiTagRequest removeTag(String tag){
        return new ExternalApiTagRequest(enmRequestType.REMOVE_ALL, tag, null);
    }

    public String getJsonEncode() throws UnsupportedEncodingException {
        return new Gson().toJsonTree(this).getAsJsonObject().toString();
    }
}
