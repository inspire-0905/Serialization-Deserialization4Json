package com.swust.SerializationDeserialization4Json.sample;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.google.gson.JsonElement;
import com.swust.SerializationDeserialization4Json.R;
import com.swust.SerializationDeserialization4Json.http.APIClient;
import com.swust.SerializationDeserialization4Json.http.DataResponseHandler;
import com.swust.SerializationDeserialization4Json.model.Model;

public class MainActivity extends Activity {

	private String json = "{\"name\": \"TeamPush\","
			+ "\"description\": \"A Team Knowledge Push And Share Project Based Pocket\","
			+ "\"version\": \"0.1.0\","
			+ "\"repository\": \"https://github.com/inspire-0905/TeamPush\","
			+ "\"dependencies\": {" + "\"express\": \"3.5.0\","
			+ "\"nedb\": \"~0.10.4\"," + "\"underscore\": \"~1.6.0\","
			+ "\"async\": \"~0.2.10\"," + "\"moment\": \"~2.5.1\","
			+ "\"handlebars\": \"~2.0.0-alpha.2\","
			+ "\"consolidate\": \"~0.10.0\"}}";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/**
		 * serialize to objcet
		 */
		Package p = Model.fromJson(json, Package.class);
		System.out.println(p.name);
		System.out.println(p.description);
		System.out.println(p.version);
		System.out.println(p.repository);
		System.out.println(p.dependencies.express);
		System.out.println(p.dependencies.nedb);
		System.out.println(p.dependencies.underscore);
		System.out.println(p.dependencies.async);
		System.out.println(p.dependencies.moment);
		System.out.println(p.dependencies.handlebars);
		System.out.println(p.dependencies.consolidate);
		/**
		 * Deserialize to String
		 */
		String data = Model.toJson(p);
		System.out.println(data);

		/**
		 * serialize from Internet
		 */
		WeatherClient.getInstanse().weatherBeijing(this,
				new DataResponseHandler() {

					@Override
					public void onSuccess(int status, JsonElement element) {
						JSONObject jobject;
						if (element.isJsonObject()) {
							try {
								jobject = new JSONObject(element.toString());
								WeatherInfo model;
								model = Model.fromJson(
										jobject.get("weatherinfo").toString(),
										WeatherInfo.class);
								System.out.println(model.toString());
								System.out.println(model.cityid);
								System.out.println(model.isRadar);
								System.out.println(model.Radar);
								System.out.println(model.SD);
								System.out.println(model.temp);
								System.out.println(model.time);
								System.out.println(model.WD);
								System.out.println(model.WS);
								System.out.println(model.WSE);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

				});

	}

	static class WeatherClient extends APIClient {

		private static WeatherClient singleton;

		private WeatherClient() {
		}

		public static String API_WEATHER_BEIJING = "data/sk/101010100.html";

		public static WeatherClient getInstanse() {
			if (singleton == null) {
				HOST = "http://www.weather.com.cn/";
				singleton = new WeatherClient();
			}
			return singleton;
		}

		public void weatherBeijing(Context context,
				DataResponseHandler responseHandler) {
			get(context, API_WEATHER_BEIJING, null, responseHandler);
		}
	}

	class WeatherInfo extends Model {
		public String city;
		public int cityid;
		public int temp;
		public String WD;
		public String WS;
		public String SD;
		public String WSE;
		public String time;
		public String isRadar;
		public String Radar;
	}

	class Package extends Model {
		public String name;
		public String description;
		public String version;
		public String repository;
		public Dependencies dependencies;

		public class Dependencies {
			public String express;
			public String nedb;
			public String underscore;
			public String async;
			public String moment;
			public String handlebars;
			public String consolidate;
		}
	}

}
