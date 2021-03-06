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

import java.util.Set;

public class ExternalApiUploadURLRequest extends ExternalApiRequest {


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
