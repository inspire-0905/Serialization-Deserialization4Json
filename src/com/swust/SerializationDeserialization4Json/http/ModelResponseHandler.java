package com.swust.SerializationDeserialization4Json.http;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.swust.SerializationDeserialization4Json.model.Model;

/**
 * @Title: ModelResponseHandler.java
 * @Package com.swust.SerializationDeserialization4Json.http
 * @Description: TODO(添加描述)
 * @author lichen8974#gmail.com
 * @date 2014-4-26 下午10:04:59
 * @version V1.0
 */
public class ModelResponseHandler<T extends Model> extends DataResponseHandler {
	private Class<T> tClass;

	public ModelResponseHandler() {
		super();
		this.tClass = null;
	}

	public ModelResponseHandler(Class<T> tClass) {
		super();
		this.tClass = tClass;
	}

	public void onSuccess(int status, T model) {
		throw new IllegalStateException("You should overide this methods");
	}

	public void onSuccess(int status, List<T> list) {
		throw new IllegalStateException("You should overide this methods");
	}

	@Override
	protected void handleSuccessData(JsonObject json) {
		JsonElement obj = getData(json);
		dd("data:" + obj.toString());
		if (obj == null
				|| obj.isJsonNull()
				|| (obj.isJsonObject() && ((JsonObject) obj).entrySet().size() == 0)) {
			onSuccess(statusCode, (T) null);
		} else {
			try {
				if (obj.isJsonObject()) {
					onSuccess(statusCode, T.create(obj, tClass));
				} else if (obj.isJsonArray()) {
					onSuccess(statusCode, T.createList(obj, tClass));
				} else {
					onFailure(
							new ModelResponseException(
									ModelResponseException.STATUS_CODE_JSON_PARSE_EXCEPTION,
									"Unexpected type "
											+ obj.getClass().getName()), json);
				}
			} catch (JsonSyntaxException e) {
				onFailure(
						new ModelResponseException(
								ModelResponseException.STATUS_CODE_OBJECT_MAPPING_EXCEPTION,
								"fail map " + json + " to "
										+ tClass.getCanonicalName()),
						(JsonElement) null);
			} catch (IllegalArgumentException e) {
				onFailure(
						new ModelResponseException(
								ModelResponseException.STATUS_CODE_OBJECT_MAPPING_EXCEPTION,
								"fail map " + json + " to "
										+ tClass.getCanonicalName()),
						(JsonElement) null);
			}
		}
	}
}
