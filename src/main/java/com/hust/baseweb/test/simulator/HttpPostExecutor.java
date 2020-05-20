package com.hust.baseweb.test.simulator;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class HttpPostExecutor {
    public static final String module = HttpPostExecutor.class.getName();
    private static OkHttpClient client = new OkHttpClient().newBuilder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build();

    public String execPostUseToken(String url, String json, String token)
        throws IOException {
        //System.out.println(module + "::execPostUseToken, url = " + url + ", json = " + json + ", token = " + token);

        RequestBody body = RequestBody.create(json, Constants.JSON);
        Request request = new Request.Builder().url(url)
            .header("X-Auth-Token", token).post(body).build();

        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }

    public String execGetUseToken(String url, String json, String token)
        throws IOException {
        //System.out.println(module + "::execPostUseToken, url = " + url + ", json = " + json + ", token = " + token);

        //RequestBody body = RequestBody.create(json, Constants.JSON);
        Request request = new Request.Builder().url(url)
            .header("X-Auth-Token", token).get().build();

        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }

}
