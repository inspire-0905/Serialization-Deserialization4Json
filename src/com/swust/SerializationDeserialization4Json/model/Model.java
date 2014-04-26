package com.swust.SerializationDeserialization4Json.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * @Title: Model.java
 * @Package com.swust.SerializationDeserialization4Json.model
 * @Description: TODO(添加描述)
 * @author lichen8974#gmail.com
 * @date 2014-4-26 下午08:59:27
 * @version V1.0
 */
public class Model {
	public static <T> T create(JsonElement json, Class<T> classOfModel) {
		T t = gson().fromJson(json, classOfModel);
		return t;
	}

	/**
	 * 
	 * @param json
	 *            JsonArray
	 * @param classOfModel
	 * @return 空数组将会返回空列表而不是null
	 * 
	 */
	public static <T> List<T> createList(JsonElement json, Class<T> classOfModel) {
		List<T> list = new ArrayList<T>();
		JsonArray array = json.getAsJsonArray();
		Iterator<JsonElement> iterator = array.iterator();
		while (iterator.hasNext()) {
			list.add(create(iterator.next(), classOfModel));
		}
		return list;
	}

	public static <T> Map<String, T> createMap(JsonElement json,
			Class<T> classOfModel) {
		Map<String, T> objMap = null;
		objMap = gson().fromJson(json, new TypeToken<Map<String, T>>() {
		}.getType());
		return objMap;

	}

	protected static Gson gson() {
		return new GsonBuilder().create();
	}

	public static String toJson(Object model) {
		Gson gson = gson();
		return gson.toJson(model);
	}

	public static <T> T fromJson(String json, Class<T> classOfModel) {
		Gson gson = gson();
		return gson.fromJson(json, classOfModel);
	}

	public static <T> List<T> fromJsonToList(String json, Class<T> classOfModel) {
		return createList(new JsonParser().parse(json), classOfModel);
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		String newLine = System.getProperty("line.separator");

		result.append(this.getClass().getName());
		result.append(" Object {");
		result.append(newLine);

		Field[] fields = this.getClass().getDeclaredFields();

		for (Field field : fields) {
			result.append("  ");
			try {
				result.append(field.getName());
				result.append(": ");
				result.append(field.get(this));
			} catch (IllegalAccessException ex) {
				System.out.println(ex);
			}
			result.append(newLine);
		}
		result.append("}");

		return result.toString();
	}
}
