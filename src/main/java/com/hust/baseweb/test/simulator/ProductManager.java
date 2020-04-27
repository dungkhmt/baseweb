package com.hust.baseweb.test.simulator;

import com.google.gson.Gson;
import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.model.GetListProductOutputModel;

import java.util.List;

public class ProductManager {
	private HttpPostExecutor executor = new HttpPostExecutor();
	private String token;
	public ProductManager(String token){
		this.token = token;
	}
	public List<Product> getProducts() {
//        System.out.println("create order agent getProducts");
        try {
            String json = "{\"statusId\":null}";
            String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/get-list-product", json, token);
            //System.out.println(module + "::getProductIds, rs = " + rs);
            Gson gson = new Gson();
            GetListProductOutputModel products = gson.fromJson(rs, GetListProductOutputModel.class);
            return products.getProducts();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
