/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.lang.reflect.Type;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ZestJSON implements JsonDeserializer<ZestElement>, JsonSerializer<ZestElement> {
	
	private static Gson gson = null;
	
	public static String toString(ZestElement element) {
		return getGson().toJson(element).toString();
	}
	
	public static ZestElement fromString(String str) {
		return getGson().fromJson(str, ZestElement.class);
	}
	
	private static Gson getGson() {
		if (gson == null) {
			// Need to add all of the abstract classes
			gson = new GsonBuilder()
						.registerTypeAdapter(ZestAction.class, new ZestJSON())
						.registerTypeAdapter(ZestAssertion.class, new ZestJSON())
						.registerTypeAdapter(ZestAuthentication.class, new ZestJSON())
						.registerTypeAdapter(ZestConditional.class, new ZestJSON())
						.registerTypeAdapter(ZestElement.class, new ZestJSON())
						.registerTypeAdapter(ZestStatement.class, new ZestJSON())
						.registerTypeAdapter(ZestTransformation.class, new ZestJSON())
						.setPrettyPrinting()
						.create();
			
		}
		return gson;
	}

	@Override
	public ZestElement deserialize(JsonElement element, Type rawType,
			JsonDeserializationContext arg2) throws JsonParseException {
		
		if (element instanceof JsonObject) {
			String elementType = ((JsonObject)element).get("elementType").getAsString();

			if (elementType.startsWith("Zest")) {
				try {
					//Class<?> c = Class.forName(this.getClass().getPackage().getName()"org.mozilla.zest.core.v1." + elementType);
					Class<?> c = Class.forName(this.getClass().getPackage().getName() + "." + elementType);
					
					return (ZestElement) getGson().fromJson(element, c);
					
				} catch (ClassNotFoundException e) {
					throw new JsonParseException(e);
				}
			}
		}
		return null;
	}

	@Override
	public JsonElement serialize(ZestElement element, Type rawType,
			JsonSerializationContext context) {
		return new GsonBuilder().setPrettyPrinting().create().toJsonTree(element);
	}

}
