package com.hust.baseweb.test.simulator;

import com.hust.baseweb.applications.accounting.document.Payment;

public class Simulator {

    public static void main(String[] arg) {
        Simulator app = new Simulator();
        app.run(1);
    }

    private void run(int nbAgents) {
        CreateOrderAgent[] agents = new CreateOrderAgent[nbAgents];
        ImportFacilityAgent importFacilityAgent = new ImportFacilityAgent("logadmin","123");
        ExportFacilityAgent exportFacilityAgent = new ExportFacilityAgent("logadmin","123");
        PaymentAgent paymentAgent = new PaymentAgent("accadmin","123");

        importFacilityAgent.start();

        exportFacilityAgent.start();

        paymentAgent.start();

        for (int i = 0; i < agents.length; i++) {
            agents[i] = new CreateOrderAgent(i);
            agents[i].setUsername("salesadmin");
            agents[i].setPassword("123");

            agents[i].setNbIters(1000);
            agents[i].setFromDate("2020-01-01");
            agents[i].setToDate("2020-05-05");
            agents[i].start();
            
        }

        //for(int i = 0; i < agents.length; i++){
        //	System.out.println("agents[" + i + "], maxTime = " + agents[i].getMaxTime());
        //}
    }
}
