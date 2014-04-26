package com.swust.SerializationDeserialization4Json.http;

import java.net.ConnectException;
import java.net.UnknownHostException;

import org.apache.http.client.HttpResponseException;

import android.accounts.NetworkErrorException;

public class ModelResponseException extends Exception {
	public static final int STATUS_CODE_UNKNOWN_EXCEPTION = 0xFFFFFFF1;
	public static final int STATUS_CODE_JSON_PARSE_EXCEPTION = 0xFFF0000;
	public static final int STATUS_CODE_OBJECT_MAPPING_EXCEPTION = 0xFFFF0000;
	public static final int STATUS_CODE_CONNECT_EXCEPTION = 0xF0000;
	public static final int STATUS_CODE_NETWORK_EXCEPTION = 0xFF0000;
	/**
     *
     */
	private static final long serialVersionUID = -1870921441796123137L;
	private int statusCode;
	private String msg;

	public ModelResponseException(int statusCode, String msg) {
		super();
		this.statusCode = statusCode;
		this.msg = msg;
	}

	public ModelResponseException(Throwable e) {
		super();
		if (e instanceof HttpResponseException) {
			HttpResponseException exp = (HttpResponseException) e;
			this.statusCode = exp.getStatusCode();
			this.msg = exp.getMessage();
		} else if (e instanceof ConnectException) {
			this.statusCode = STATUS_CODE_CONNECT_EXCEPTION;
			this.msg = e.getMessage();
		} else if (e instanceof NetworkErrorException) {
			this.statusCode = STATUS_CODE_NETWORK_EXCEPTION;
			this.msg = e.getMessage();
		} else if (e instanceof UnknownHostException) {
			this.statusCode = STATUS_CODE_JSON_PARSE_EXCEPTION;
			this.msg = e.getMessage();
		} else {
			this.statusCode = STATUS_CODE_UNKNOWN_EXCEPTION;
			this.msg = "unknown exception:" + e.getClass().getName()
					+ " | Message:" + e.getMessage();
		}
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	@Override
	public String getMessage() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
