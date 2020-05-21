package com.hust.baseweb.test.simulator;

import lombok.extern.log4j.Log4j2;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

@Log4j2
public class Simulator {

    public final static AtomicLong threadRunningCounter = new AtomicLong(0);

    static {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.info("Threads count: {}", threadRunningCounter.get());
            }
        }, 1000, 1000);
    }

    public static void main(String[] arg) {
        Simulator app = new Simulator();
        app.run(100);
    }

    private void run(int nbAgents) {
        CreateOrderAgent[] agents = new CreateOrderAgent[nbAgents];
        ImportFacilityAgent importFacilityAgent = new ImportFacilityAgent("admin", "123");
        ExportFacilityAgent exportFacilityAgent = new ExportFacilityAgent("admin", "123");
        PaymentAgent paymentAgent = new PaymentAgent("admin", "123");

        importFacilityAgent.start();

        exportFacilityAgent.start();

        paymentAgent.start();

        for (int i = 0; i < agents.length; i++) {
            agents[i] = new CreateOrderAgent("admin", "123");

            agents[i].setAgentId(i);

            agents[i].setNbIters(5);
            agents[i].setFromDate("2020-01-01");
            agents[i].setToDate("2020-05-05");
            agents[i].start();

        }

        //for(int i = 0; i < agents.length; i++){
        //	System.out.println("agents[" + i + "], maxTime = " + agents[i].getMaxTime());
        //}
    }
}
