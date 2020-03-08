package com.hust.baseweb.tester;

import com.hust.baseweb.test.simulator.Constants;
import okhttp3.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class RequestAgent extends Thread {
    public static final String module = RequestAgent.class.getName();
    public static final String name = RequestAgent.class.getName();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static String urlRoot = "http://localhost:8080";
    // public static String url_root = "http://3.1.6.126";
    private OkHttpClient client = new OkHttpClient();
    private Random rand = new Random();
    private Thread thread = null;
    private String token;

    public static void main(String[] args) {
        RequestAgent agent = new RequestAgent();
        agent.start();
    }

    String execGetUseToken(String url, String token) throws IOException {

        Request request = new Request.Builder().url(url)
                // .header("Authorization", token)
                .header("X-Auth-Token", token).build();
        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }

    String execPostUseToken(String url, String json, String token)
            throws IOException {
        System.out.println(module + "::execPostUseToken, url = " + url + ", json = " + json + ", token = " + token);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder().url(url)
                .header("X-Auth-Token", token).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }

    public void start() {
        System.out.println(name + ":: start running...");
        if (thread == null) {
            thread = new Thread(this, name);
            thread.start();
        }
    }

    public String login(String username, String password) {
        try {
            // String url = "http://3.1.6.126/api/";
            String url = urlRoot + "/api/";
            String credential = Credentials.basic(username, password);
            Request request = new Request.Builder().url(url)
                    .header("Authorization", credential).build();
            Response response = client.newCall(request).execute();
            String res = Objects.requireNonNull(response.body()).string();
            String token = response.header("X-Auth-Token");
            System.out.println("res = " + res + ", token = " + token);
            return token;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getUserLogins() {
        try {
            String res = execGetUseToken(urlRoot + "/api/get-list-userlogins",
                    token);
            System.out.println(module + "::getUserLogins, res = " + res);
            return res;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String postLocation(double lat, double lng, Date timePoint) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String json = "{" + "\"lat\":" + lat + ",\"lng\":" + lng
                + ",\"timePoint\":\"" + formatter.format(timePoint) + "\""
                + "}";
        System.out.println(module + "::postLocation, input json = " + json);
        try {
            String res = execPostUseToken(urlRoot + "/api/post-location",
                    json, token);
            System.out.println(module + "::postLocation, res = " + res);

            json = "{\"statusId\":null}";
            res = execPostUseToken(Constants.URL_ROOT + "/api/get-list-product",
                    json, token);
            System.out.println(module + "::postLocation, res = " + res);


            return res;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void run() {
        System.out.println(name + "::run....");

        token = login("dungpq", "123");

        getUserLogins();

        int N = 1;
        double maxTime = 0;
        for (int i = 1; i <= N; i++) {
            Date timePoint = new Date();
            Random R = new Random();
            int a = R.nextInt(10000) + 210000;
            int b = R.nextInt(10000) + 1050000;
            double lat = a * 1.0 / 10000;
            double lng = b * 1.0 / 10000;
            double t0 = System.currentTimeMillis();
            postLocation(lat, lng, timePoint);
            double t = System.currentTimeMillis() - t0;
            if (maxTime < t) {
                maxTime = t;
            }
            System.out.println("time = " + t + ", maxTime = " + maxTime);
        }


    }
}
