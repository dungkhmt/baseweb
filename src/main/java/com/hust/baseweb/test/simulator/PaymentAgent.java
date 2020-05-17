package com.hust.baseweb.test.simulator;

import java.util.Random;
import java.util.concurrent.ExecutionException;

public class PaymentAgent extends Thread {
    public static final String module = ExportFacilityAgent.class.getName();

    private Random rand = new Random();
    private Thread thread = null;
    private String token;
    private HttpPostExecutor executor = new HttpPostExecutor();

    private int nbIters = 10;
    private int idleTime = 360;

    public void run() {
        System.out.println(module + "::run....");

        token = Login.login("admin", "123");

        createPayments();

    }

    public void createAPayment() throws Exception{

    }
    public void createPayments(){
        for(int i = 0; i < nbIters; i++){
            try {
                createAPayment();
            }catch(Exception e){
                e.printStackTrace(); break;
            }
        }
    }
}
