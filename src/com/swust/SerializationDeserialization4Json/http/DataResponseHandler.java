package com.swust.SerializationDeserialization4Json.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @Title: DataResponseHandler.java
 * @Package com.swust.SerializationDeserialization4Json.http
 * @Description: TODO(添加描述)
 * @author lichen8974#gmail.com
 * @date 2014-4-26 下午09:46:23
 * @version V1.0
 */
public class DataResponseHandler extends GsonResponseHandler {

	protected int statusCode=-1;
	public DataResponseHandler() {
	}

	protected static JsonElement getData(JsonObject obj) {
		/**
		 * 一般JSON数据分为3部,statusCode,data,msg.若无data,则默认全为data.
		 */
		if (obj.has("data")) {
			return obj.get("data");
		} else {
			return obj;
		}
	}

	protected static int getStatusCode(JsonObject obj) {
		/**
		 * 一般JSON数据分为3部,statusCode,data,msg.若无status_code,则默认返回-1.
		 */
		if (obj.has("status_code")) {
			return obj.get("status_code").getAsInt();
		} else {
			return -1;
		}
	}
	
	protected static String getMsg(JsonObject obj)
	{
		/**
		 * 一般JSON数据分为3部,statusCode,data,msg.若无msg,则默认返回空.
		 */
		if (obj.has("msg")) {
			return obj.get("msg").getAsString();
		} else {
			return "";
		}
	}
	protected void handleSuccessData(JsonObject json) {
		JsonElement obj = getData(json);
		onSuccess(0, obj);
	}
	
	protected void handleFailureData(int statusCode,JsonObject json)
	{
		onFailure(new ModelResponseException(statusCode, getMsg(json)), json);
	}
	@Override
	public void handleJsonData(int statusCode, JsonElement jsonResponse) {
		dd("handleSuccessJsonMessage:" + "statusCode:" + statusCode + "\n"
				+ jsonResponse);
		if (jsonResponse.isJsonObject()) {
			JsonObject json = jsonResponse.getAsJsonObject();
			int _statusCode = getStatusCode(json);
			if (_statusCode==-1)
			{
				handleSuccessData(json);
			}else if (_statusCode!=-1&&_statusCode==statusCode){
			handleSuccessData(json);
			}else{
			handleFailureData(_statusCode,json);
			}
		} else {
			//TODO 返回非JSON类型
			onFailure(new ModelResponseException(new Exception(
					"Unexpected type " + jsonResponse.getClass().getName())),
					(JsonElement) null);
		}
	}
	
	public void setSucceedStatusCode(int statusCode)
	{
		this.statusCode=statusCode;
	}

}
