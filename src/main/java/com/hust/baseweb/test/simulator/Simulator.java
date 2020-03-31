package com.hust.baseweb.test.simulator;

public class Simulator {

    public static void main(String[] arg) {
        Simulator app = new Simulator();
        app.run(10);
    }

    private void run(int nbAgents) {
        CreateOrderAgent[] agents = new CreateOrderAgent[nbAgents];
        for (int i = 0; i < agents.length; i++) {
            agents[i] = new CreateOrderAgent(i);
            agents[i].setNbIters(1000);
            agents[i].setFromDate("2020-01-01");
            agents[i].setToDate("2020-03-31");
            agents[i].start();
            
            
        }

        //for(int i = 0; i < agents.length; i++){
        //	System.out.println("agents[" + i + "], maxTime = " + agents[i].getMaxTime());
        //}
    }
}
