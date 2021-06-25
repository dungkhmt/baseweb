package com.hust.baseweb.applications.education.teacherclassassignment.service;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.util.HashSet;

public class MaxLoadConstraintORToolMIPSolver {
    private int n;// number of classes
    private int m;// number of teachers
    private HashSet<Integer>[] D;// D[i] is the set of teachers that can be assigned to class i
    private boolean[][] conflict;
    private double[] hourClass;
    private double[] maxHourTeacher;

    // intermediate Data structure
    private double totalHourClass;
    private int INF;

    // MIP modelling
    private MPVariable[][] x;// x[i,j] = 1 indicates that teacher i is assigned to class j
    private MPSolver solver;
    private MPVariable obj;

    // data structures for solution
    private int[] assignment;// assignment[i] is the teacher assigned to class i

    public MaxLoadConstraintORToolMIPSolver(int n, int m, HashSet[] D, boolean[][] conflict, double[] hourClass, double[] maxHourTeacher) {
        this.n = n;
        this.m = m;
        this.D = D;
        this.conflict = conflict;
        this.hourClass = hourClass;
        this.maxHourTeacher = maxHourTeacher;


    }
    private void initDatastructures(){
        totalHourClass = 0;
        for(int i = 0; i < n; i++) totalHourClass += hourClass[i];
        INF = (int)totalHourClass + 1;

    }
    public void createSolverAndVariables(){
        x = new MPVariable[m][n];

        Loader.loadNativeLibraries();
        solver = MPSolver.createSolver(String.valueOf(MPSolver.OptimizationProblemType.SCIP_MIXED_INTEGER_PROGRAMMING));

        if (solver == null) {
            System.err.println("Could not create solver SCIP");
            return;
        }

        System.out.println("createSolverAndVariables, n = " + n + " m = " + m);

        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                if(!D[i].contains(j)){// teacher j cannot be assigned to class i
                    x[j][i] = solver.makeIntVar(0,0,"x[" + j + "," + i + "]");
                }else{
                    x[j][i] = solver.makeIntVar(0,1,"x[" + j + "," + i + "]");
                }
            }
        }
        obj = solver.makeIntVar(0, totalHourClass, "minOfMaxLoad");

    }
    private void createdConstraints(){
        // each class is assigned to exactly one teacher
        for(int i = 0; i < n; i++){
            MPConstraint c = solver.makeConstraint(1,1);
            for(int j = 0; j < m; j++)
                c.setCoefficient(x[j][i],1);
        }

        // hour load of each teacher cannot exceed the maximum allowed valueOf
        for(int j = 0; j < m; j++){
            MPConstraint c = solver.makeConstraint(0,maxHourTeacher[j]);
            for(int i = 0; i < n; i++)
                c.setCoefficient(x[j][i],hourClass[i]);
        }

        // conflict constraint
        for(int j = 0; j < m; j++){
            for(int i1 = 0; i1 < n; i1++){
                for(int i2 = i1+1; i2 < n; i2++){
                    if(conflict[i1][i2]){
                        MPConstraint c = solver.makeConstraint(0,1);
                        c.setCoefficient(x[j][i1],1);
                        c.setCoefficient(x[j][i2],1);
                    }
                }
            }
        }

        // constraint on the objective function
        for(int j = 0; j < m; j++){
            MPConstraint c= solver.makeConstraint(-INF,0);
            for(int i = 0; i < n; i++){
                c.setCoefficient(x[j][i],hourClass[i]);
            }
            c.setCoefficient(obj,-1);
        }
    }
    private void createObjective(){
        MPObjective objective = solver.objective();
        objective.setCoefficient(obj, 1);
        objective.setMinimization();

    }
    public boolean solve() {
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
            for(int i = 0; i < n; i++){
                for(int j = 0; j < m; j++){
                    System.out.println("solver, x[" + i + "," + j + "] = " + x[j][i].solutionValue());
                    if(x[j][i].solutionValue() > 0){
                        assignment[i] = j;
                    }
                }
            }
            return true;
        }
        return false;
    }
    public int[] getSolutionAssignment(){
        return assignment;
    }

}
