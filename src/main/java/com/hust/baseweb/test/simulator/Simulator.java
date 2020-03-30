package com.hust.baseweb.test.simulator;

public class Simulator {

    public static void main(String[] arg) {
        Simulator app = new Simulator();
        app.run(1);
    }

    private void run(int nbAgents) {
        CreateOrderAgent[] agents = new CreateOrderAgent[nbAgents];
        for (int i = 0; i < agents.length; i++) {
            agents[i] = new CreateOrderAgent(i);
            agents[i].setNbIters(500);
            agents[i].setFromDate("2020-03-23");
            agents[i].setToDate("2020-03-29");
            agents[i].start();
            
            
        }

        //for(int i = 0; i < agents.length; i++){
        //	System.out.println("agents[" + i + "], maxTime = " + agents[i].getMaxTime());
        //}
    }
}
