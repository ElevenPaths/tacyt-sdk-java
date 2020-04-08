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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Error {

    private int code;
    private String message;
    private Object[] args;

    public Error(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public Error(int code, String msg, Object[] args) {
        this.code = code;
        this.message = msg;
        this.args = args;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object[] getArgs() {
        return args;
    }

    /**
     * @return a JsonObject with the code and message of the error
     */
    public JsonObject toJson() {
        JsonObject error = new JsonObject();
        error.addProperty("code", code);
        error.addProperty("message", message);
        if (this.args != null) {
            JsonArray args = new JsonArray();
            for (Object obj : this.args) {
                args.add(new JsonPrimitive(obj.toString()));
            }
            error.add("args", args);
        }
        return error;
    }

    @Override
    public String toString() {
        return toJson().toString();
    }
}