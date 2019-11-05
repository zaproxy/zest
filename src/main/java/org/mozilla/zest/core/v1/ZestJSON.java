/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

// TODO: Auto-generated Javadoc
/** The Class ZestJSON. */
public class ZestJSON implements JsonDeserializer<ZestElement>, JsonSerializer<ZestElement> {

    /** The gson. */
    private static Gson gson = null;

    /**
     * To string.
     *
     * @param element the element
     * @return the string
     */
    public static String toString(ZestElement element) {
        return getGson().toJson(element);
    }

    /**
     * From string.
     *
     * @param str the str
     * @return the zest element
     */
    public static ZestElement fromString(String str) {
        ZestElement ze = getGson().fromJson(str, ZestElement.class);
        if (ze != null && ze instanceof ZestStatement) {
            ((ZestStatement) ze).init();
        }
        return ze;
    }

    /**
     * Gets the gson.
     *
     * @return the gson
     */
    private static Gson getGson() {
        if (gson == null) {
            // Need to add all of the abstract classes
            gson =
                    newDefaultGsonBuilder()
                            .registerTypeAdapter(ZestAction.class, new ZestJSON())
                            .registerTypeAdapter(ZestAssignment.class, new ZestJSON())
                            .registerTypeAdapter(ZestAuthentication.class, new ZestJSON())
                            .registerTypeAdapter(ZestElement.class, new ZestJSON())
                            .registerTypeAdapter(ZestStatement.class, new ZestJSON())
                            .registerTypeAdapter(ZestExpressionElement.class, new ZestJSON())
                            .registerTypeAdapter(ZestLoop.class, new ZestJSON())
                            .registerTypeAdapter(ZestLoopState.class, new ZestJSON())
                            .registerTypeAdapter(ZestLoopTokenSet.class, new ZestJSON())
                            .create();
        }
        return gson;
    }

    private static GsonBuilder newDefaultGsonBuilder() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, DateTypeAdapter.INSTANCE)
                .setPrettyPrinting();
    }

    @Override
    public ZestElement deserialize(
            JsonElement element, Type rawType, JsonDeserializationContext arg2) {
        if (element instanceof JsonObject) {
            String elementType = ((JsonObject) element).get("elementType").getAsString();

            if (elementType.startsWith("Zest")) {
                try {
                    // Class<?> c =
                    // Class.forName(this.getClass().getPackage().getName()"org.mozilla.zest.core.v1." + elementType);
                    Class<?> c =
                            Class.forName(
                                    this.getClass().getPackage().getName() + "." + elementType);
                    return (ZestElement) getGson().fromJson(element, c);

                } catch (ClassNotFoundException e) {
                    throw new JsonParseException(e);
                }
            }
        }
        return null;
    }

    @Override
    public JsonElement serialize(
            ZestElement element, Type rawType, JsonSerializationContext context) {
        return newDefaultGsonBuilder().create().toJsonTree(element);
    }

    private static class DateTypeAdapter implements JsonDeserializer<Date>, JsonSerializer<Date> {

        static final DateTypeAdapter INSTANCE = new DateTypeAdapter();

        private static final DateTimeFormatter FORMAT = DateTimeFormatter.ISO_DATE_TIME;

        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(FORMAT.format(src.toInstant().atOffset(ZoneOffset.UTC)));
        }

        @Override
        public Date deserialize(
                JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            String value = json.getAsJsonPrimitive().getAsString();
            try {
                return new Date(FORMAT.parse(value, Instant::from).toEpochMilli());
            } catch (DateTimeParseException e) {
                throw new JsonParseException("Failed to parse the date: " + value, e);
            }
        }
    }
}
