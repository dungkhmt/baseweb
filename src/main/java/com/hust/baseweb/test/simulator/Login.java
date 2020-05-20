package com.hust.baseweb.test.simulator;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Objects;

public class Login {
    private static OkHttpClient client = new OkHttpClient();

    public static String login(String username, String password) {
        try {
            // String url = "http://sscm.dailyopt.ai/api/";
            String url = Constants.URL_ROOT + "/api/";
            String credential = Credentials.basic(username, password);
            Request request = new Request.Builder().url(url)
                .header("Authorization", credential).build();
            Response response = client.newCall(request).execute();
            String res = Objects.requireNonNull(response.body()).string();
            String token = response.header("X-Auth-Token");
//            System.out.println("res = " + res + ", token = " + token);
            return token;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
