package com.hust.baseweb.test.simulator;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.reflect.*;
import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.model.GetListFacilityOutputModel;
import com.hust.baseweb.applications.logistics.model.GetListProductOutputModel;
import com.hust.baseweb.applications.order.entity.PartyCustomer;
import com.hust.baseweb.applications.order.model.GetListPartyCustomerOutputModel;
import com.hust.baseweb.applications.order.model.ModelCreateOrderInput;
import com.hust.baseweb.applications.order.model.ModelCreateOrderInputOrderItem;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateOrderAgent extends Thread {
	public static final String module = CreateOrderAgent.class.getName();
	//OkHttpClient client = new OkHttpClient();
	
	private Random rand = new Random();
	private Thread t = null;
	private String token;
	
	//public static final MediaType JSON = MediaType
	//		.get("application/json; charset=utf-8");
	private HttpPostExecutor executor = new HttpPostExecutor();
	
	private int nbIters = 1000;
	private double maxTime = 0;
	private int id;
	/*
	String execPostUseToken(String url, String json, String token)
			throws IOException {
		System.out.println(module + "::execPostUseToken, url = " + url + ", json = " + json + ", token = " + token);
		RequestBody body = RequestBody.create(Constants.JSON, json);
		Request request = new Request.Builder().url(url)
				.header("X-Auth-Token", token).post(body).build();
		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		}
	}
	*/
	public CreateOrderAgent(int id){
		this.id = id;
	}
	public void setNbIters(int nbIters){
		this.nbIters = nbIters;
	}
	public void start() {
		System.out.println(module + ":: start running...");
		if (t == null) {
			t = new Thread(this, module);
			t.start();
		}
	}
	public double getMaxTime(){
		return this.maxTime;
	}
	public void run() {
		System.out.println(module + "::run....");

		token = Login.login("dungpq", "123");

		

		maxTime = 0;
		for (int i = 1; i <= nbIters; i++) {
			Date timePoint = new Date();
			Random R = new Random();
			//double t0 = System.currentTimeMillis();
			double t = createOrder();
			//double t = System.currentTimeMillis() - t0;
			if(maxTime < t) maxTime = t;
			//System.out.println("time = " + t + ", maxTime = " + maxTime);
		}

		System.out.println(module + "[" + id + "] finished, maxTime = " + maxTime);
		
	}

	public List<Product> getProducts(){
		try{
			String json = "{\"statusId\":null}";
			String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/get-list-product", json, token);
			//System.out.println(module + "::getProductIds, rs = " + rs);
			Gson gson = new Gson();
			GetListProductOutputModel products = gson.fromJson(rs, GetListProductOutputModel.class);
			return products.getProducts(); 
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public List<Facility> getFacilities(){
		try{
			String json = "{\"statusId\":null}";
			String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/get-list-facility", json, token);
			//System.out.println(module + "::getFacilities, rs = " + rs);
			Gson gson = new Gson();
			GetListFacilityOutputModel facilities = gson.fromJson(rs, GetListFacilityOutputModel.class);
			return facilities.getFacilities();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public List<PartyCustomer> getCustomers(){
		try{
			String json = "{\"statusId\":null}";
			String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/get-list-party-customers", json, token);
			//System.out.println(module + "::getCustomers, rs = " + rs);
			Gson gson = new Gson();
			GetListPartyCustomerOutputModel customers = gson.fromJson(rs, GetListPartyCustomerOutputModel.class);
			return customers.getCustomers();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public double createOrder(){
		try{
			Gson gson = new Gson();
			String[] salesmanIds = {"dungpq","datnt","admin","nguyenvanseu"};
			SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");
			List<Product> products = getProducts();
			List<Facility> facilities = getFacilities();
			List<PartyCustomer> customers = getCustomers();
			
			//Product sel_p = products.get(rand.nextInt(products.size()));
			Facility sel_f = facilities.get(rand.nextInt(facilities.size()));
			PartyCustomer sel_c = customers.get(rand.nextInt(customers.size()));
			
			ModelCreateOrderInput inp = new ModelCreateOrderInput();
			inp.setFacilityId(sel_f.getFacilityId());
			inp.setSalesChannelId(null);
			inp.setSalesmanId(salesmanIds[rand.nextInt(salesmanIds.length)]);
			inp.setOrderDate(formatter.format(new Date()));
			inp.setPartyCustomerId(sel_c.getPartyId());
			ModelCreateOrderInputOrderItem[] orderItems = new ModelCreateOrderInputOrderItem[products.size()];
			for(int i = 0; i < orderItems.length; i++){
				orderItems[i] = new ModelCreateOrderInputOrderItem();
				orderItems[i].setProductId(products.get(i).getProductId());
				orderItems[i].setQuantity(rand.nextInt(100) + 1);
				orderItems[i].setUnitPrice(new BigDecimal((rand.nextInt(100)+1)*10000000));
				BigDecimal total = orderItems[i].getUnitPrice().multiply(new BigDecimal(orderItems[i].getQuantity()));
				orderItems[i].setTotalItemPrice(total);
			}
			
			inp.setOrderItems(orderItems);
			
			double t0 = System.currentTimeMillis();
			String json = gson.toJson(inp);
			String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/create-order", json, token);
			//System.out.println(module + "::createOrder, rs = " + rs);
			double t = System.currentTimeMillis() - t0;
			return t;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return 0;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CreateOrderAgent a = new CreateOrderAgent(0);
		a.start();
	}

}
