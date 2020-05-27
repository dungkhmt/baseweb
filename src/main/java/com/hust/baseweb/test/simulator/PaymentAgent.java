package com.hust.baseweb.test.simulator;

import com.google.gson.Gson;
import com.hust.baseweb.applications.accounting.document.Payment;
import com.hust.baseweb.applications.customer.model.PartyCustomerModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Random;

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

    public PaymentAgent(String username, String password) {

        this.username = username;
        this.password = password;
    }

    public void run() {

        Simulator.threadRunningCounter.incrementAndGet();
//        System.out.println(module + "::run....");

        token = Login.login(username, password);

        createPayments();
        Simulator.threadRunningCounter.decrementAndGet();
    }

    public void createAPayment() throws Exception {

        PartyManager partyManager = new PartyManager(token);
        List<PartyCustomerModel> partyCustomerModels = partyManager.getListParty();

        PartyCustomerModel randomPartyCustomerModel = partyCustomerModels.get(rand.nextInt(partyCustomerModels.size()));
        Payment.CreateModel createModel = new Payment.CreateModel(
            randomPartyCustomerModel.getPartyCustomerId(),
            (double) (rand.nextInt(60000) + 10000));

        Gson gson = new Gson();

        executor.execPostUseToken(Constants.URL_ROOT + "/api/create-payment", gson.toJson(createModel), token);
    }

    public void createPayments() {

        for (int i = 0; i < nbIters; i++) {
            try {
                createAPayment();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
