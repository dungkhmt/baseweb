package com.hust.baseweb.applications.customer.util;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class Crawler {

    public static void main(String[] args) {
        Crawler app = new Crawler();
        app.run();
    }

    public void run() {
        try {
            String stringUrl = "http://www.vantaiduongviet.com/dich_vu/van_tai/danh_sach_cac_cong_ty_van_tai";
            URL url = new URL(stringUrl);
            URLConnection uc = url.openConnection();

            uc.setRequestProperty("X-Requested-With", "Curl");

            //String userpass = "username" + ":" + "password";
            //String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
            //uc.setRequestProperty("Authorization", basicAuth);

            InputStreamReader inputStreamReader = new InputStreamReader(uc.getInputStream());
            // read this input
            Scanner in = new Scanner(inputStreamReader);
            while (in.hasNext()) {
                String line = in.nextLine();
                //System.out.println(line);
                if (line.contains("Địa chỉ")) {
                    System.out.println(line);
                }

            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
