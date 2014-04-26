package com.swust.SerializationDeserialization4Json.http;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;

import android.os.Message;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @Title: GsonResponseHandler.java
 * @Package com.swust.SerializationDeserialization4Json.http
 * @Description: TODO(添加描述)
 * @author lichen8974#gmail.com
 * @date 2014-4-26 下午09:04:14
 * @version V1.0
 */
public abstract class GsonResponseHandler extends AsyncHttpResponseHandler {
	protected static final int SUCCESS_JSON_MESSAGE = 100;
	protected final String TAG = getClass().toString();

	public GsonResponseHandler() {
	}

	@Override
	public void onStart() {
		dd("onstart");
	}

	public void onSuccess(int status, JsonElement element) {
		Log.w(TAG, "onSuccess escape");
	}

	public void onFailure(ModelResponseException e, JsonElement errorResponse) {
		e.printStackTrace();
		Log.w(TAG, "onFailure escape");
	}

	protected void onAuthenticationFailure() {
	}

	protected void onServiceFailure()
	{
		
	}
	
	protected void onHttpPageNotFound()
	{
		
	}
	protected void onRequestTimeout()
	{
		
	}
	@Override
	protected void sendSuccessMessage(int statusCode, String responseBody) {
		if (statusCode != HttpStatus.SC_NO_CONTENT) {
			try {
				JsonElement jsonResponse = parseResponse(responseBody);
				sendMessage(obtainMessage(SUCCESS_JSON_MESSAGE, new Object[] {
						statusCode, jsonResponse }));
			} catch (JsonParseException e) { // FIXME 缩小错误范围
				sendFailureMessage(e, responseBody);
			}
		} else {
			sendMessage(obtainMessage(SUCCESS_JSON_MESSAGE, new Object[] {
					statusCode, new JsonObject() }));
		}
	}

	@Override
	protected void handleMessage(Message msg) {
		switch (msg.what) {
		case SUCCESS_JSON_MESSAGE:
			Object[] response = (Object[]) msg.obj;
			handleJsonData(((Integer) response[0]).intValue(),
					(JsonElement) response[1]);
			break;
		default:
			super.handleMessage(msg);
		}
	}

	protected JsonElement parseResponse(String responseBody) {
		JsonElement result = null;
		responseBody = responseBody.trim();
		result = new JsonParser().parse(responseBody);
		return result;
	}

	/**
	 * 成功返回 Json 数据，在这个方法内对 Json 进行处理，判断返回是否成功
	 * 
	 * @param statusCode
	 * @param headers
	 * @param jsonResponse
	 */
	public abstract void handleJsonData(int statusCode, JsonElement jsonResponse);

	/**
	 * 三层错误 HTTP JSON
	 * 
	 * @param e
	 * @param responseBody
	 */
	@Override
	protected void handleFailureMessage(Throwable e, String responseBody) {
		try {

			Log.d(TAG, "handle Failure:" + responseBody);
			e.printStackTrace();

			if (e instanceof HttpResponseException) {
				HttpResponseException exp = (HttpResponseException) e;
				switch (exp.getStatusCode())
				{
				case 401:
					onAuthenticationFailure();
					return;
				case 404:
					onHttpPageNotFound();
					return;
				case 408:
					onRequestTimeout();
					return;
				default:
					if (exp.getStatusCode()>=500){
						onServiceFailure();
					}
					break;
				}
			}
			if (responseBody != null) {
				JsonElement jsonResponse = parseResponse(responseBody);
				onFailure(new ModelResponseException(e), jsonResponse);
			} else {
				onFailure(new ModelResponseException(e), (JsonObject) null);
			}
		} catch (JsonParseException ex) {
			// TODO 错误结果转化为json 出错
			ex.printStackTrace();
			onFailure(new ModelResponseException(e), (JsonObject) null);
		}
	}

	public List<String> toStringList(JsonElement ele) {
		List<String> list = new ArrayList<String>();
		JsonArray array = ele.getAsJsonArray();
		Iterator<JsonElement> iterator = array.iterator();
		while (iterator.hasNext()) {
			list.add(iterator.next().getAsString());
		}
		return list;
	}

	protected void dd(String msg) {
		Log.d(TAG, msg);
	}

	protected void dd(String msg, Throwable ex) {
		Log.d(TAG, msg, ex);
	}

}
