package com.hust.baseweb.applications.education.teacherclassassignment.service;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.util.HashSet;

public class MaxAssignedClassConstraintORToolMIPSolver {

    private int n;// number of classes
    private int m;// number of teachers
    private HashSet<Integer>[] D;// D[i] is the set of teachers that can be assigned to class i
    private boolean[][] conflict;
    private int[][] priority;
    private double[] hourClass;
    private double[] maxHourTeacher;

    // intermediate Data structure
    private double totalHourClass;
    private int INF;
    private int maxP;// max value of priority
    private int minP;// min value of priority

    // parameters
    private int alpha;
    private int[] beta;

    // MIP modelling
    private MPVariable[][] x;// x[i,j] = 1 indicates that teacher i is assigned to class j
    private MPSolver solver;
    private MPVariable obj;
    private MPVariable objectiveMaxNbClassAssigned;
    private MPVariable objectiveMaxPriority;
    private MPVariable[][] y;// Y[i,k] = 1 indicates that class i is assigned to a teacher with priority k
    private MPVariable[] z;// Z[i] = 1 indicates that class i is assigned to some teacher
    private MPVariable[] u;// u[j] is the hour_load of teacher j

    // data structures for solution
    private int[] assignment;// assignment[i] is the teacher assigned to class i
    private HashSet<Integer> notAssigned;
    public MaxAssignedClassConstraintORToolMIPSolver(
        //int n,
        //int m,
        //HashSet[] D,
        //int[][] priority,
        //boolean[][] conflict,
        //double[] hourClass,
        //double[] maxHourTeacher
        MapDataInput I
    ) {
        this.n = I.n;
        this.m = I.m;
        this.D = I.D;
        this.priority = I.priority;
        this.conflict = I.conflict;
        this.hourClass = I.hourClass;
        this.maxHourTeacher = I.maxHourTeacher;
    }

    public HashSet<Integer> getNotAssignedClass(){
        return notAssigned;
    }
    private void initDatastructures() {
        totalHourClass = 0;
        for (int i = 0; i < n; i++) {
            totalHourClass += hourClass[i];
        }
        INF = (int) totalHourClass + 1;
        maxP = 0;
        minP = Integer.MAX_VALUE;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                if(priority[i][j] < Integer.MAX_VALUE){
                        if(priority[i][j] > maxP) maxP = priority[i][j];
                        if(priority[i][j] < minP) minP = priority[i][j];
                }

            }
        }

        // init parameters
        alpha = 10000;
        beta = new int[maxP + 1];
        for(int k = minP; k <= maxP; k++) beta[k] = 1;
        beta[minP] = 10000; beta[minP+1] = 100;
    }

    public void createSolverAndVariables() {
        x = new MPVariable[m][n];

        Loader.loadNativeLibraries();
        solver = MPSolver.createSolver(String.valueOf(MPSolver.OptimizationProblemType.SCIP_MIXED_INTEGER_PROGRAMMING));

        if (solver == null) {
            System.err.println("Could not create solver SCIP");
            return;
        }

        System.out.println("createSolverAndVariables, n = " + n + " m = " + m);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (!D[i].contains(j)) {// teacher j cannot be assigned to class i
                    x[j][i] = solver.makeIntVar(0, 0, "x[" + j + "," + i + "]");
                } else {
                    x[j][i] = solver.makeIntVar(0, 1, "x[" + j + "," + i + "]");
                }
            }
        }
        obj = solver.makeIntVar(0, totalHourClass, "minOfMaxLoad");

        int sumBeta = 0;
        for(int p = minP; p <= maxP; p++) sumBeta += beta[p];
        sumBeta = sumBeta * n;
        objectiveMaxPriority = solver.makeIntVar(0,sumBeta, "objectiveMaxPriority");

        objectiveMaxNbClassAssigned = solver.makeIntVar(0,n,"objectiveMaxNbClassAssigned");


        // create variables y
        y = new MPVariable[n][maxP+1];
        for(int i = 0; i < n; i++){
            for(int k = minP; k <= maxP; k++){
                y[i][k] = solver.makeIntVar(0,1,"y[" + i + "," + k + "]");
            }
        }

        // create variable z
        z = new MPVariable[n];
        for(int i = 0; i < n; i++)
            z[i] = solver.makeIntVar(0,1,"z[" + i + "]");

        // create variable u
        //u = new MPVariable[n];
        //for(int i = 0; i < n; i++)
        //    u[i] = solver.makeIntVar(0,totalHourClass,"u[" + i + "]");
    }
    private void createConstraintAtMostOneTeacherIsAssignedToEachClass(){
        // each class is assigned to at most one teacher
        for (int i = 0; i < n; i++) {
            //MPConstraint c = solver.makeConstraint(1, 1);
            MPConstraint c = solver.makeConstraint(0, 1);
            for (int j = 0; j < m; j++) {
                c.setCoefficient(x[j][i], 1);
            }
        }

    }
    private void createConstraintMaxHourLoadTeacher(){
        // hour load of each teacher cannot exceed the maximum allowed valueOf
        for (int j = 0; j < m; j++) {
            MPConstraint c = solver.makeConstraint(0, maxHourTeacher[j]);
            for (int i = 0; i < n; i++) {
                c.setCoefficient(x[j][i], hourClass[i]);
            }
        }
    }
    private void createConstraintConflictClasses(){
        // conflict constraint
        for (int j = 0; j < m; j++) {
            for (int i1 = 0; i1 < n; i1++) {
                for (int i2 = i1 + 1; i2 < n; i2++) {
                    if (conflict[i1][i2]) {
                        MPConstraint c = solver.makeConstraint(0, 1);
                        c.setCoefficient(x[j][i1], 1);
                        c.setCoefficient(x[j][i2], 1);
                    }
                }
            }
        }
    }
    private void createConstraintChannelXZ(){
        // constraint between x and z: z[i] = \sum_{j=1..m} x[j,i]
        for(int i = 0; i < n; i++){
            MPConstraint c = solver.makeConstraint(0, 0);
            c.setCoefficient(z[i],1);
            for(int j = 0; j < m; j++)
                c.setCoefficient(x[j][i],-1);

        }
    }
    private void createConstraintOnY(){
        for(int i = 0; i < n; i++){
            MPConstraint c = solver.makeConstraint(0,1);
            for(int p = minP; p <= maxP; p++)
                c.setCoefficient(y[i][p],1);
        }
    }

    private void createConstraintChannelXY(){
        // constraint between x and y
        for(int p = minP; p <= maxP; p++){
            for(int i = 0; i < n; i++){
                for(int j = 0; j < m; j++){
                    if(priority[i][j] == p){ // y[i,p] = x[j,i]
                        MPConstraint c = solver.makeConstraint(0,0);
                        c.setCoefficient(y[i][p],1);
                        c.setCoefficient(x[j][i],-1);
                    }
                    /*
                    else{
                        MPConstraint c = solver.makeConstraint(0,0);
                        c.setCoefficient(y[i][p],1);
                    }
                    */
                }
            }
        }

    }
    private void createConstraintObj(){
        // constraint on the objective function
        for (int j = 0; j < m; j++) {
            MPConstraint c = solver.makeConstraint(-INF, 0);
            for (int i = 0; i < n; i++) {
                c.setCoefficient(x[j][i], hourClass[i]);
            }
            c.setCoefficient(obj, -1);
        }
    }
    private void createMaxPriorityObjectiveConstraint() {
        MPConstraint c = solver.makeConstraint(0,0);
        for(int p = minP; p <= maxP; p++){
            for(int i = 0; i < n; i++){
                c.setCoefficient(y[i][p],beta[p]);
            }
        }
        c.setCoefficient(objectiveMaxPriority,-1);

    }
    private void createConstraintChannelYZ(){
        for(int i = 0; i < n; i++){
            MPConstraint c = solver.makeConstraint(0,INF);
            for(int p = minP; p <= maxP; p++)
                c.setCoefficient(y[i][p],-1);
            c.setCoefficient(z[i],1);
        }
    }

    private void createMaxNbAssignedClassConstraint(){
        MPConstraint c = solver.makeConstraint(0,0);
        for(int i = 0; i < n; i++) c.setCoefficient(z[i],1);
        c.setCoefficient(objectiveMaxNbClassAssigned,-1);
    }
    private void createdConstraints() {
        createConstraintAtMostOneTeacherIsAssignedToEachClass();
        createConstraintMaxHourLoadTeacher();
        createConstraintConflictClasses();
        createConstraintChannelXY();
        createConstraintChannelXZ();
        createConstraintChannelYZ();
        //createConstraintOnY();
        createConstraintObj();
        createMaxNbAssignedClassConstraint();
        //createMaxPriorityObjectiveConstraint();
    }
    private void createObjective(){
        MPObjective objective = solver.objective();
        objective.setCoefficient(objectiveMaxNbClassAssigned,1);
        objective.setMaximization();
    }
    public double getObjectivePriority(){
        return objectiveMaxPriority.solutionValue();
    }
    public double getObjectiveNumberAssignedClass(){
        return objectiveMaxNbClassAssigned.solutionValue();
    }
    public void printSolutionVariables(){
        for(int j = 0; j < m; j++){
            for(int i = 0; i < n; i++){
                if(x[j][i].solutionValue() > 0){
                    System.out.println("x[" + j + "," + i + "] = " + x[j][i].solutionValue());
                }
            }
        }
        for(int i = 0; i < n; i++){
            if(z[i].solutionValue() > 0){
                System.out.println("z[" + i + "]= " + z[i].solutionValue());
            }
        }
        for(int i = 0; i < n; i++){
            for(int p = minP; p <= maxP; p++){
                if(y[i][p].solutionValue() > 0){
                    System.out.println("y[" + i + "," + p + "] = " + y[i][p].solutionValue());
                }
            }
        }
    }
    private boolean solvePhase1(){
        // maximize the number of class assigned
        initDatastructures();
        createSolverAndVariables();
        createdConstraints();
        createObjective();

        // Solves.
        final MPSolver.ResultStatus resultStatus = solver.solve();

        notAssigned = new HashSet<Integer>();
        // Analyse solution.
        if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
            assignment = new int[n];
            for(int i = 0; i < n; i++) assignment[i] = -1;
            //System.out.println("solve, n = " + n + " m = " + m + ", objective = " + objectiveMaxNbClassAssigned.value());
            printSolutionVariables();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) if (x[j][i].solutionValue() > 0){
                    //System.out.println("solver, x[" + i + "," + j + "] = " + x[j][i].solutionValue());
                    if (x[j][i].solutionValue() > 0) {
                        assignment[i] = j;
                    }
                }
            }
            for(int i = 0; i < n; i++) {
                //System.out.println("solver z[" + i + "] = " + z[i].solutionValue());
                if (z[i].solutionValue() <= 0) {
                    notAssigned.add(i);
                }
            }
            return true;
        }
        return false;

    }

    public boolean solve() {
        if(true)
        return solvePhase1();

        initDatastructures();
        createSolverAndVariables();
        createdConstraints();
        createObjective();

        // Solves.
        final MPSolver.ResultStatus resultStatus = solver.solve();

        // Analyse solution.
        if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
            assignment = new int[n];
            System.out.println("solve, n = " + n + " m = " + m);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    System.out.println("solver, x[" + i + "," + j + "] = " + x[j][i].solutionValue());
                    if (x[j][i].solutionValue() > 0) {
                        assignment[i] = j;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public int[] getSolutionAssignment() {
        return assignment;
    }

}
