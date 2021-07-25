package com.hust.baseweb.applications.education.teacherclassassignment.service;

import java.util.HashSet;

public class ORToolMIPSolver {
    private MapDataInput I;
    private MaxAssignedClassConstraintORToolMIPSolver maxAssignedClassConstraintORToolMIPSolver;
    private MaxPriorityClassAssignmentORToolMIPSolver maxPriorityClassAssignmentORToolMIPSolver;
    private MinWorkingDaysClassAssignmentORToolMIPSolver minWorkingDaysClassAssignmentORToolMIPSolver;

    private int[] assignment;
    private HashSet<Integer> notAssigned;
    public ORToolMIPSolver(MapDataInput I){
        this.I = I;
        maxAssignedClassConstraintORToolMIPSolver = new MaxAssignedClassConstraintORToolMIPSolver(I);
        maxPriorityClassAssignmentORToolMIPSolver = new MaxPriorityClassAssignmentORToolMIPSolver(I);
        minWorkingDaysClassAssignmentORToolMIPSolver = new MinWorkingDaysClassAssignmentORToolMIPSolver(I);
    }
    public boolean solve(){
        boolean ok = maxAssignedClassConstraintORToolMIPSolver.solve();
        assignment = maxAssignedClassConstraintORToolMIPSolver.getSolutionAssignment();
        notAssigned= maxAssignedClassConstraintORToolMIPSolver.getNotAssignedClass();
        System.out.println("PHASE 1: maxAssignedClassConstraintORToolMIPSolver, priority = "
                           + maxAssignedClassConstraintORToolMIPSolver.getObjectivePriority()
        + " nbAssignedClass = " + maxAssignedClassConstraintORToolMIPSolver.getObjectiveNumberAssignedClass()
        );

        //if(true) return ok;

        int nbAssignedClasses = I.n - notAssigned.size();
        maxPriorityClassAssignmentORToolMIPSolver.setNbAssignedClasses(nbAssignedClasses);


        ok = maxPriorityClassAssignmentORToolMIPSolver.solve();
        assignment = maxPriorityClassAssignmentORToolMIPSolver.getSolutionAssignment();
        notAssigned = maxPriorityClassAssignmentORToolMIPSolver.getNotAssignedClass();

        System.out.println("PHASE 2: maxPriorityClassAssignmentORToolMIPSolver, priority = "
                           + maxPriorityClassAssignmentORToolMIPSolver.getObjectivePriority()
        + " nbAssignedClass = " + maxPriorityClassAssignmentORToolMIPSolver.getObjectiveNumberAssignedClass()
        );

        minWorkingDaysClassAssignmentORToolMIPSolver.setNbAssignedClasses(nbAssignedClasses);
        ok = minWorkingDaysClassAssignmentORToolMIPSolver.solve();
        assignment = minWorkingDaysClassAssignmentORToolMIPSolver.getSolutionAssignment();
        notAssigned = minWorkingDaysClassAssignmentORToolMIPSolver.getNotAssignedClass();

        System.out.println("PHASE 3: minWorkingDaysClassAssignmentORToolMIPSolver, objectiveMinWorkingDays = " +
                           minWorkingDaysClassAssignmentORToolMIPSolver.getObjectiveMinWorkingDays());

        return ok;
    }
    public int[] getSolutionAssignment() {
        return assignment;
    }
    public HashSet<Integer> getNotAssignedClass(){
        return notAssigned;
    }
}
