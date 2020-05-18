package com.hust.baseweb.test.simulator;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;
import java.util.concurrent.ExecutionException;

@Getter
@Setter
public class PaymentAgent extends Thread {
    public static final String module = ExportFacilityAgent.class.getName();

    private Random rand = new Random();
    private Thread thread = null;
    private String token;
    private String username;
    private String password;

    private HttpPostExecutor executor = new HttpPostExecutor();

    private int nbIters = 10;
    private int idleTime = 360;

    public PaymentAgent(String username, String password){
        this.username = username; this.password = password;
    }
    public void run() {
        System.out.println(module + "::run....");

        token = Login.login(username, password);

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
