package com.hust.baseweb.algorithmsapi.stafftaskassignment;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.*;

public class StaffTaskAssignmentSolver {

    private StaffTaskAssignmentInput input;
    private int n;// number of tasks;
    private int m;// number of staffs;
    private int[] d;// d[i] duration of task i
    private Set<Integer>[] S; //S[i] is the set of staffs that can perform task i
    private int[] x;// x[i] is the staff assigned to the task i
    private int[] load;// load[s] is the load of staff s

    public StaffTaskAssignmentSolver(StaffTaskAssignmentInput input) {
        this.input = input;
        n = input.getTasks().size();
        m = input.getStaffs().size();
        S = new Set[n];
        d = new int[n];
        for (int i = 0; i < n; i++) {
            S[i] = new HashSet<Integer>();
        }

        for (int i = 0; i < n; i++) {
            Task t = input.getTasks().get(i);
            d[i] = t.getDuration();
            for (int j = 0; j < m; j++) {
                Staff st = input.getStaffs().get(j);
                for (String s : st.getSkills()) {
                    if (s.equals(t.getTaskTypeID())) {
                        S[i].add(j);
                    }
                }
            }
        }

    }

    private void greedy() {
        load = new int[m];
        x = new int[n];
        for (int j = 0; j < m; j++) {
            load[j] = 0;
        }

        for (int i = 0; i < n; i++) {
            int sel_j = -1;
            int min_load = Integer.MAX_VALUE;
            for (int j : S[i]) {
                if (load[j] < min_load) {
                    sel_j = j;
                    min_load = load[j];
                }
            }
            x[i] = sel_j;
            load[sel_j] += d[i];
        }
    }

    public void printSolution() {
        for (int j = 0; j < m; j++) {
            System.out.println("load[" + j + "] = " + load[j]);
        }
    }

    public void printSolutionExcel(String filename) {
        try {
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("Load");
            Row r = sheet.createRow(0);
            Cell c = r.createCell(0);
            c.setCellValue("StaffID");
            c = r.createCell(1);
            c.setCellValue("Load");
            c = r.createCell(2);
            c.setCellValue("Tasks");
            for (int j = 0; j < m; j++) {
                r = sheet.createRow(j + 1);
                c = r.createCell(0);
                c.setCellValue(input.getStaffs().get(j).getStaffID());
                c = r.createCell(1);
                c.setCellValue(load[j]);
                String tasks = "";
                for (int i = 0; i < n; i++) {
                    if (x[i] == j) {
                        Task t = input.getTasks().get(i);
                        tasks = tasks + "[" + t.getTaskID() + "," + t.getDuration() + "], ";
                    }
                }
                c = r.createCell(2);
                c.setCellValue(tasks);
            }

            sheet = wb.createSheet("Task");
            r = sheet.createRow(0);
            c = r.createCell(0);
            c.setCellValue("Task");
            c = r.createCell(1);
            c.setCellValue("Staff");
            for (int i = 0; i < n; i++) {
                r = sheet.createRow(i + 1);
                c = r.createCell(0);
                c.setCellValue(input.getTasks().get(i).getTaskID());
                c = r.createCell(1);
                c.setCellValue(input.getStaffs().get(x[i]).getStaffID());

            }
            FileOutputStream out = new FileOutputStream(filename);
            wb.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void solve() {
        greedy();
    }

    public void generateInstance(String filename, int n, int m, int minD, int maxD) {
        int[] dt = {15, 20, 30, 60};
        String[] typeConf = {"T1", "T2", "T3", "T4", "T5"};
        int[] x = new int[n];
        int[] d = new int[n];
        String[] type = new String[n];
        List<String>[] skills = new List[m];
        for (int i = 0; i < m; i++) {
            skills[i] = new ArrayList<String>();
        }
        Random R = new Random();

        for (int i = 0; i < n; i++) {
            int j = R.nextInt(dt.length);
            d[i] = dt[j];
            //j = R.nextInt(typeConf.length);
            //type[i] = typeConf[j];
        }
        for (int j = 0; j < m; j++) {
            int L = R.nextInt(3) + 1;
            HashSet<String> S = new HashSet<String>();
            do {
                int k = R.nextInt(typeConf.length);
                S.add(typeConf[k]);
            } while (S.size() < L);
            for (String s : S) {
                skills[j].add(s);
            }
        }
        int[] load = new int[m];
        for (int i = 0; i < m; i++) {
            load[i] = 0;
        }
        for (int i = 0; i < n; i++) {
            int sel_j = -1;
            int min_load = Integer.MAX_VALUE;
            for (int j = 0; j < m; j++) {
                if (min_load > load[j]) {
                    min_load = load[j];
                    sel_j = j;
                }
            }
            x[i] = sel_j;
            load[sel_j] += d[i];
            int k = R.nextInt(skills[sel_j].size());
            type[i] = skills[sel_j].get(k);
            //if(!skills[sel_j].contains(type[i]))
            //    skills[sel_j].add(type[i]);
        }
        // add randomly other skills
        for (int i = 0; i < m; i++) {
            int L = R.nextInt(3);
            for (int k = 0; k <= L; k++) {
                for (int j = 0; j < typeConf.length; j++) {
                    if (!skills[i].contains(typeConf[j])) {
                        skills[i].add(typeConf[j]);
                        break;
                    }
                }
            }
        }
        for (int j = 0; j < m; j++) {
            System.out.print("load[" + j + "] = " + load[j] + " skills: ");
            for (String s : skills[j]) {
                System.out.print(s + ", ");
            }
            System.out.println();
        }

        try {
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("Tasks");
            Row r = sheet.createRow(0);
            Cell c = r.createCell(0);
            c.setCellValue("TaskID");
            c = r.createCell(1);
            c.setCellValue("TaskDuration");
            c = r.createCell(2);
            c.setCellValue("location");
            c = r.createCell(3);
            c.setCellValue("Type");

            for (int i = 0; i < n; i++) {
                r = sheet.createRow(i + 1);
                c = r.createCell(0);
                c.setCellValue(i + 1);
                c = r.createCell(1);
                c.setCellValue(d[i]);
                c = r.createCell(2);
                c.setCellValue("L0" + i);
                c = r.createCell(3);
                c.setCellValue(type[i]);
            }

            sheet = wb.createSheet("Staffs");
            r = sheet.createRow(0);
            c = r.createCell(0);
            c.setCellValue("StaffID");
            c = r.createCell(1);
            c.setCellValue("Skills");
            for (int i = 0; i < m; i++) {
                r = sheet.createRow(i + 1);
                c = r.createCell(0);
                c.setCellValue(i + 1);
                c = r.createCell(1);
                String s = "";
                for (int k = 0; k < skills[i].size() - 1; k++) {
                    s = s + skills[i].get(k) + ",";
                }
                s = s + skills[i].get(skills[i].size() - 1);
                c.setCellValue(s);
            }
            sheet = wb.createSheet("Distances");


            FileOutputStream out = new FileOutputStream(filename);
            wb.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        StaffTaskAssignmentInput input = new StaffTaskAssignmentInput();


        //input.loadData("D:/projects/baseweb/data/stafftaskassignment/input.xlsx");
        input.loadData("D:/projects/baseweb/data/stafftaskassignment/input-2000.xlsx");
        StaffTaskAssignmentSolver solver = new StaffTaskAssignmentSolver(input);
        //solver.generateInstance("D:/projects/baseweb/data/stafftaskassignment/input-2000.xlsx",2000,100,1,2);
        //if(true) return;

        solver.solve();
        solver.printSolution();
        solver.printSolutionExcel("D:/projects/baseweb/data/stafftaskassignment/output.xlsx");
    }
}
