package com.swust.SerializationDeserialization4Json.http;

import org.apache.http.client.CookieStore;
import org.apache.http.client.params.ClientPNames;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/** 
 * @Title: HttpClient.java 
 * @Package com.swust.SerializationDeserialization4Json.http 
 * @Description: TODO(添加描述) 
 * @author lichen8974#gmail.com 
 * @date 2014-4-26 下午08:10:27 
 * @version V1.0 
 */
public abstract class HttpClient {

	private final String TAG = getClass().toString();
	private AsyncHttpClient client;
	protected final static int TIME_OUT = 30 * 1000;

	protected HttpClient() {

		client = new AsyncHttpClient();
		client.setTimeout(TIME_OUT);
		/**
		 * 解决CircularRedirectException
		 */
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
	}

	public void setCookieStore(CookieStore cs) {
		getAsyncHttpClient().setCookieStore(cs);
	}

	public void cancelRequest(Context context, boolean mayInterruptIfRunning) {
		getAsyncHttpClient().cancelRequests(context, mayInterruptIfRunning);
	}

	private String getAbsoluteUrl(String relativeUrl) {
		return getAbsoluteUrl(relativeUrl, getHost());
	}

	public void get(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) { //
		url = getAbsoluteUrl(url);
		d("get:" + url);
		d("params:" + params);
		getAsyncHttpClient().get(context, url, params, responseHandler);
	}

	public void post(Context context, String url,
			RequestParams params, AsyncHttpResponseHandler responseHandler) {
		url = getAbsoluteUrl(url);
		d("post:" + url);
		d("params:" + params);
		getAsyncHttpClient().post(url, params, responseHandler);
	}

	public void put(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		url = getAbsoluteUrl(url);
		d("put:" + url);
		d("params:" + params);
		getAsyncHttpClient().put(url, params, responseHandler);
	}

	public void delete(Context context, String url,
			AsyncHttpResponseHandler responseHandler) {
		url = getAbsoluteUrl(url);
		d("delete:" + url);
		getAsyncHttpClient().delete(context, url, responseHandler);
	}

	public abstract String getHost();

	/**
	 * @return the client
	 */
	public AsyncHttpClient getAsyncHttpClient() {
		return client;
	}

	public static String getAbsoluteUrl(String relativeUrl, String host) {
		if (relativeUrl.startsWith("http://")
				|| relativeUrl.startsWith("https://")) {
			return relativeUrl;
		} else if (relativeUrl.startsWith("/"))
			return host + relativeUrl.substring(1);
		else
			return host + relativeUrl;
	}

	protected void d(String msg) {
		Log.d(TAG, msg);
	}

	protected void d(String msg, Throwable ex) {
		Log.d(TAG, msg, ex);
	}

}
