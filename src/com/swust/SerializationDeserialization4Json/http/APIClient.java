package com.swust.SerializationDeserialization4Json.http;

/**
 * @Title: APIClient.java
 * @Package com.swust.SerializationDeserialization4Json.http
 * @Description: TODO(添加描述)
 * @author lichen8974#gmail.com
 * @date 2014-4-26 下午08:20:27
 * @version V1.0
 */
public class APIClient extends HttpClient {
	protected static String HOST = null;
	private static APIClient singleton;

	public static APIClient getInstanse() {
		if (singleton == null) {
			singleton = new APIClient();
		}
		return singleton;
	}

	@Override
	public String getHost() {
		if (HOST == null || HOST.equals("")) {
			throw new IllegalStateException("you must override this method");
		}
		return HOST;
	}

}
