package com.hust.baseweb.test.simulator;

import com.google.gson.Gson;
import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.model.product.SetProductPriceInputModel;

import java.util.List;
import java.util.Random;

public class SetProductPriceAgent extends Thread {
    public static final String module = SetProductPriceAgent.class.getName();
    // OkHttpClient client = new OkHttpClient();

    private Random rand = new Random();
    private Thread thread = null;
    private String token;

    // public static final MediaType JSON = MediaType
    // .get("application/json; charset=utf-8");
    private HttpPostExecutor executor = new HttpPostExecutor();

    private int nbIters = 1000;
    private double maxTime = 0;
    private int id;

    public String name() {
        return module + "[" + id + "[";
    }

    public SetProductPriceAgent(int id) {
        this.id = id;
    }

    public void start() {
//		System.out.println(name() + ":: start running...");
        if (thread == null) {
            thread = new Thread(this, name());
            thread.start();
        }
    }

    public void run() {
        try {
//			System.out.println(name() + "::run....");

            token = Login.login("admin", "123");

            ProductManager prodMgr = new ProductManager(token);
            List<Product> products = prodMgr.getProducts();

            Gson gson = new Gson();
            for (int i = 0; i < products.size(); i++) {
                //for(int i = 0; i <= 0; i++){
                Product p = products.get(i);

                SetProductPriceInputModel input = new SetProductPriceInputModel();
                input.setCurrencyUomId("CUR_vnd");
                input.setProductId(p.getProductId());
                input.setPrice(new Double(1));
                input.setTaxInPrice("N");

                String json = gson.toJson(input);
//				System.out.println(name() + "::run, " + i + "/" + products.size() + ", set price of product "
//						+ p.getProductId() + ", json = " + json);

                String rs = executor.execPostUseToken(Constants.URL_ROOT
                    + "/api/set-product-price", json, token);
//				System.out.println(name() + "::run, set price of product "
//						+ p.getProductId() + ", rs = " + rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SetProductPriceAgent a = new SetProductPriceAgent(0);
        a.start();
    }
}
