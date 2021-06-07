package com.hust.baseweb.applications.education.programsubmisson.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class ContestResultChecker {

    private String filename;
    private String stdFilename;
    public String msg;

    public ContestResultChecker(String filename, String stdFilename) {
        this.filename = filename;
        this.stdFilename = stdFilename;
    }

    private ArrayList<ArrayList<String>> extract(Scanner in) {
        ArrayList<ArrayList<String>> infoIn = new ArrayList<>();
        while (in.hasNext()) {
            String line = in.nextLine();
            line = line.trim();
            if (line == null || line.equals("")) {
                continue;
            }
            String[] elements = line.split(" ");
            if (elements != null && elements.length > 0) {
                ArrayList<String> list_line = new ArrayList<>();
                for (int i = 0; i < elements.length; i++) {
                    list_line.add(elements[i].trim());
                }
                infoIn.add(list_line);
            }
        }
        return infoIn;
    }

    public boolean check() {
        Scanner in = null;
        Scanner stdIn = null;
        ArrayList<ArrayList<String>> infoStdIn = new ArrayList<>();
        ArrayList<ArrayList<String>> infoIn = new ArrayList<>();
        try {
            in = new Scanner(new File(filename));
            infoIn = extract(in);
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
            if (in != null) {
                in.close();
            }
            if (stdIn != null) {
                stdIn.close();
            }
            msg = "Cannot find result file ";
            return false;
        }
        try {
            stdIn = new Scanner(new File(stdFilename));
            infoStdIn = extract(stdIn);
            stdIn.close();

        } catch (Exception e) {
            e.printStackTrace();
            if (in != null) {
                in.close();
            }
            if (stdIn != null) {
                stdIn.close();
            }
            msg = "Cannot find standard output file ";
            return false;
        }
        if (infoIn.size() != infoStdIn.size()) {
            msg = "Results are different";
            return false;
        }
        for (int i = 0; i < infoIn.size(); i++) {
            ArrayList<String> L1 = infoIn.get(i);
            ArrayList<String> L2 = infoStdIn.get(i);
            if (L1.size() != L2.size()) {
                msg = "Results are different";
                return false;
            }
            for (int j = 0; j < L1.size(); j++) {
                if (!L1.get(j).equals(L2.get(j))) {
                    msg = "Results are different";
                    return false;
                }
            }
        }
        return true;


    }
}
