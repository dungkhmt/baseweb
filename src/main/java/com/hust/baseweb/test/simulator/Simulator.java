package com.hust.baseweb.test.simulator;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.cli.*;





import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

@Log4j2
public class Simulator {

    public final static AtomicLong threadRunningCounter = new AtomicLong(0);

    private int nbCreateOrderAgents = 1;
    private int nbIterCreateOrder = 5;

    static {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.info("Threads count: {}", threadRunningCounter.get());
            }
        }, 1000, 1000);
    }

    public Simulator(int nbCreateOrderAgents, int nbIterCreateOrder) {
        this.nbCreateOrderAgents = nbCreateOrderAgents;
        this.nbIterCreateOrder = nbIterCreateOrder;
    }

    public static void main(String[] arg) throws InterruptedException, ParseException {
//        int nbCreateOrderAgents = 1;
//        int nbIterCreateOrder = 10;
//        if (arg != null) {
//            for (int i = 0; i < arg.length; i++) {
//                if (arg[i].equals("--nbCreateOrderAgents")) {
//                    nbCreateOrderAgents = Integer.parseInt(arg[i + 1]);
//                } else if (arg[i].equals("--nbIterCreateOrder")) {
//                    nbIterCreateOrder = Integer.parseInt(arg[i + 1]);
//                }
//            }
//        }

        CommandLine commandLine = buildCommandLine(arg);

        int nbCreateOrderAgents = Integer.parseInt(commandLine.getOptionValue("nbCreateOrderAgents"));
        int nbIterCreateOrder = Integer.parseInt(commandLine.getOptionValue("nbIterCreateOrder"));
        if (commandLine.hasOption("hostIpAddress")) {
            Constants.URL_ROOT = "http://" + commandLine.getOptionValue("hostIpAddress");
        }

        log.info(
            "Simulator start, numberThreads = {}, numberIterators = {}, hostIp = {}",
            nbCreateOrderAgents,
            nbIterCreateOrder,
            Constants.URL_ROOT);

        Simulator app = new Simulator(nbCreateOrderAgents, nbIterCreateOrder);
        app.run();
    }

    private static CommandLine buildCommandLine(String[] arg) throws ParseException {

        Options options = new Options();
        Option numberThreadsOption = new Option("nt", "nbCreateOrderAgents", true, "number threads");
        numberThreadsOption.setRequired(true);
        Option numberIteratorsOption = new Option("ni", "nbIterCreateOrder", true, "number iterators");
        numberIteratorsOption.setRequired(true);
        Option hostIpAddressOption = new Option("ip", "hostIpAddress", true, "host ip address");
        hostIpAddressOption.setRequired(false);

        options.addOption(numberThreadsOption);
        options.addOption(numberIteratorsOption);
        options.addOption(hostIpAddressOption);

        CommandLineParser commandLineParser = new DefaultParser();
        return commandLineParser.parse(options, arg);
       

    }
    private void run() throws InterruptedException {

        CreateOrderAgent[] agents = new CreateOrderAgent[nbCreateOrderAgents];
        ImportFacilityAgent importFacilityAgent = new ImportFacilityAgent("admin", "123");
        ExportFacilityAgent exportFacilityAgent = new ExportFacilityAgent("admin", "123");
        PaymentAgent paymentAgent = new PaymentAgent("admin", "123");

        importFacilityAgent.start();

        // chờ import xong thì export
        importFacilityAgent.join();

        exportFacilityAgent.start();

        exportFacilityAgent.join();

        paymentAgent.start();

        paymentAgent.join();

        for (int i = 0; i < agents.length; i++) {
            agents[i] = new CreateOrderAgent(Login.login("admin", "123"));

            agents[i].setAgentId(i);

            agents[i].setNbIters(nbIterCreateOrder);
            agents[i].setFromDate("2020-01-01");
            agents[i].setToDate("2020-05-05");
            agents[i].start();

        }

        //for(int i = 0; i < agents.length; i++){
        //	System.out.println("agents[" + i + "], maxTime = " + agents[i].getMaxTime());
        //}
    }
}
