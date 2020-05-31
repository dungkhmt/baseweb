package com.hust.baseweb.tester;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.PrintWriter;

public class HttpRequestTester {

    static OkHttpClient client = new OkHttpClient();

    // code request code here
    String doGetRequest(String url) throws IOException {

        Request request = new Request.Builder().url(url).build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public void getSourceHtml(String url) {
        try {
            String html = doGetRequest(url);
            PrintWriter out = new PrintWriter("crawl-output.html");
            out.print(html);
            System.out.print(html);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        HttpRequestTester app = new HttpRequestTester();
        app.getSourceHtml("https://www.worldometers.info/coronavirus/");
    }
}
