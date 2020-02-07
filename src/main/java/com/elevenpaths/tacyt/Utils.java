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

import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.TimeZone;

public class Utils {

    public static final String ENCODING = "UTF-8";
    public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

    public static String generateToken(int length) {
        String alphanum = "abcdefghijkmnpqrstuvwxyzABCDEFGHJKLMNPQRTUVWXYZ2346789";
        SecureRandom generator = new SecureRandom();
        char[] token = new char[length];
        for (int i = 0; i < length; i++) {
            token[i] = alphanum.charAt(generator.nextInt(alphanum.length()));

        }
        return new String(token);
    }

    public static String sha1(File file) throws NoSuchAlgorithmException, IOException {
        final MessageDigest messageDigest = MessageDigest.getInstance("SHA1");

        InputStream is = new BufferedInputStream(new FileInputStream(file));
        final byte[] buffer = new byte[1024];
        for (int read = 0; (read = is.read(buffer)) != -1; ) {
            messageDigest.update(buffer, 0, read);
        }

        if (is != null) {
            is.close();
        }

        // Convert the byte to hex format
        Formatter formatter = new Formatter();
        for (final byte b : messageDigest.digest()) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    /**
     * Returns a substring controlling the size to don't throw exception if is exceeded
     *
     * @param str   String to substring
     * @param start First character included in the substring
     * @param end   Last character included in the substring
     * @return
     */
    public static String safeSubstringWithoutCrop(String str, int start, int end) {

        String result = "";

        if (str != null && start < str.length() && end <= str.length() && start < end) {

            int index = str.lastIndexOf(' ', end);
            if (index != -1) {
                result = str.substring(start, index);
            } else {
                result = str.substring(start, end);
            }

        } else if (str != null && start < str.length() && start < end) {

            result = str.substring(start, str.length());
        }

        return result;
    }

    /**
     * Returns a substring controlling the size to don't throw exception if is exceeded
     *
     * @param str   String to substring
     * @param start First character included in the substring
     * @param end   Last character included in the substring
     * @return
     */
    public static String safeSubstring(String str, int start, int end) {

        String result = "";

        if (str != null && start < str.length() && end <= str.length()) {

            result = str.substring(start, end);

        } else if (str != null && start < str.length()) {

            result = str.substring(start, str.length());
        }

        return result;
    }


    // **************************
    // Generic gson parser
    // **************************

    private static Gson gsonParser = null;

    public static Gson getGsonParser() {
        if (Utils.gsonParser == null) {
            Utils.gsonParser = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new DateSerializer())
                    .registerTypeAdapter(Date.class, new DateDeserializer()).create();
        }

        return Utils.gsonParser;
    }

    // **************************
    // Simple date formats
    // **************************

    private static SimpleDateFormat defaultSDF = null;
    private static SimpleDateFormat solrSDF = null;

    private static final String[] DATE_FORMATS = new String[]{
            "yyyy-MM-dd'T'HH:mm:ss.SSSX",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ssX",
            "yyyy-MM-dd'T'HH:mm:ss.SSX",
            "yyyy-MM-dd'T'HH:mm:ss.SX",
    };

    public static class DateDeserializer implements JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonElement jsonElement, Type typeOF,
                                JsonDeserializationContext context) throws JsonParseException {
            for (String format : DATE_FORMATS) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(format);//, Configuration.DEFAULT_LOCALE);
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                    return sdf.parse(jsonElement.getAsString());

                } catch (ParseException e) {
                    //Logger.error("DateSerializer.deserialize error ParseException: " + e.getMessage());

                } catch (IllegalArgumentException e1) {
                    //Logger.error("DateSerializer.deserialize error IllegalArgument: " + e1.getMessage());

                }
            }

            //if the date can't be parsed, then a now date will be returned to avoid an exception
            return new Date();
        }
    }

    public static class DateSerializer implements JsonSerializer<Date> {

        @Override
        public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(Utils.getSolrSDF().format(date));
        }
    }

    /**
     * Returns a SimpleDateFormat for the format "yyyy-MM-dd HH:mm:ss"
     *
     * @return
     */
    public static SimpleDateFormat getDefaultSDF() {
        if (defaultSDF == null) {
            defaultSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//, Configuration.DEFAULT_LOCALE);
            defaultSDF.setTimeZone(TimeZone.getTimeZone("UTC"));
            defaultSDF.setLenient(false);
        }
        return defaultSDF;
    }

    /**
     * Returns a SimpleDateFormat for the format "yyyy-MM-dd'T'HH:mm:ss'Z'"
     *
     * @return
     */
    public static SimpleDateFormat getSolrSDF() {
        if (solrSDF == null) {
            solrSDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");//, Configuration.DEFAULT_LOCALE);
            solrSDF.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return solrSDF;
    }

    /**
     * Modify a date adding/subtracting a given time
     *
     * @param date Date in which the add/subtract will be applied
     * @param add  TRUE if add, FALSE if subtract
     * @param unit Calendar.UNIT, examples: Calendar.SECOND, Calendar.MINUTE...
     * @param time Time to add/subtract
     * @return
     */
    public static Date addSubtractTimeToDate(Date date, boolean add, int unit, int time) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(unit, ((add) ? time : -time));

        return cal.getTime();
    }

    private static SimpleDateFormat dateFormatPresentation = null;

    public static SimpleDateFormat getDateFormatPresentation() {
        if (dateFormatPresentation == null) {
            dateFormatPresentation = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//, Configuration.DEFAULT_LOCALE);
            dateFormatPresentation.setTimeZone(TimeZone.getTimeZone("UTC"));
        }

        return dateFormatPresentation;
    }
}
