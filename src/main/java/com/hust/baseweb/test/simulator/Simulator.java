package com.hust.baseweb.test.simulator;

public class Simulator {
	
	public void run(int nbAgents){
		CreateOrderAgent[] agents = new CreateOrderAgent[nbAgents];
		for(int i = 0; i < agents.length; i++){
			agents[i] = new CreateOrderAgent(i);
			agents[i].setNbIters(10);
			agents[i].start();
		}
		
		//for(int i = 0; i < agents.length; i++){
		//	System.out.println("agents[" + i + "], maxTime = " + agents[i].getMaxTime());
		//}
	}
	public static void main(String[] arg){
		Simulator app = new Simulator();
		app.run(10);
	}
}
