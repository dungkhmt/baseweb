package com.hust.baseweb.test.simulator;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpPostExecutor {
	public static final String module = HttpPostExecutor.class.getName();
	OkHttpClient client = new OkHttpClient();
	
	String execPostUseToken(String url, String json, String token)
			throws IOException {
		//System.out.println(module + "::execPostUseToken, url = " + url + ", json = " + json + ", token = " + token);
		
		RequestBody body = RequestBody.create(Constants.JSON, json);
		Request request = new Request.Builder().url(url)
				.header("X-Auth-Token", token).post(body).build();
		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		}
	}
}
