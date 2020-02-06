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

import java.util.List;

public class ExternalApiTagRequest extends ExternalApiRequest {

    public enum enmRequestType {
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

    public static ExternalApiTagRequest list() {
        return new ExternalApiTagRequest(enmRequestType.LIST, null, null);
    }

    public static ExternalApiTagRequest assignTag(String tag, List<String> apps) {
        return new ExternalApiTagRequest(enmRequestType.CREATE, tag, apps);
    }

    public static ExternalApiTagRequest removeTagFromApp(String tag, List<String> apps) {
        return new ExternalApiTagRequest(enmRequestType.REMOVE, tag, apps);
    }

    public static ExternalApiTagRequest removeTag(String tag) {
        return new ExternalApiTagRequest(enmRequestType.REMOVE_ALL, tag, null);
    }
}
