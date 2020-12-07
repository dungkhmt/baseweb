package com.hust.baseweb.applications.education.teacherclassassignment.service;

import com.google.ortools.Loader;
import com.google.ortools.sat.*;
import com.hust.baseweb.applications.education.teacherclassassignment.model.AlgoClassIM;
import com.hust.baseweb.applications.education.teacherclassassignment.model.AlgoTeacherAssignmentIM;
import com.hust.baseweb.applications.education.teacherclassassignment.model.TeacherClassAssignmentOM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ORToolsCPSolver extends BaseMIPSolver implements ISolver {

    private int minOfMaxLoad, maxOfMinLoad; // Max, min load per teacher (from result of phase 1 and 2).

    private int boundOfThisRound; // Bound of total load of teacher in optimal rounds.

    private short[] teachersOfThisRound; // Teachers in an optimal round who not optimized.

    private ArrayList<Double> optimalSD = new ArrayList<>(); // To tracking SD in each optimal round.

    // Modeling.
    private double time = 0;

    private CpModel model;

    private IntVar bound; // Objective in phase 1 and 2, max(bound) = totalLoadOfFeasibleClasses.

    private IntVar[] totalLoadOfTeacher; // Calculate total load of the teacher in optimal rounds.

    private IntVar[] loadEqualBound; // Determine teachers have load equals bound in optimal rounds.

    private IntVar[][] x;

    public ORToolsCPSolver(AlgoTeacherAssignmentIM im) {
        super(im);
    }

    @Override
    public void solve() {
        super.preProcessing();
        phase1();
        phase2();
        phase3();
    }

    private void initModelAndSetUpFirst2Constrains() {
        // Init
        x = new IntVar[numTeachers][numClasses];

        Loader.loadNativeLibraries();
        model = new CpModel();

        if (model == null) {
            System.out.println("Could not create solver SCIP");
            return;
        }

        bound = model.newIntVar(0, totalLoadOfFeasibleClasses, "minOfMaxLoad");

        for (short teacher = 0; teacher < numTeachers; teacher++) {
            for (Short cls : classesTeacherCanTeach[teacher]) {
                if (!infeasibleClasses.contains(cls)) {
                    x[teacher][cls] = model.newBoolVar("x[" + teacher + "][" + cls + "]");
                }
            }
        }


        // Constrains.
        // Constrain 1: Each class taught by only one teacher.
        for (Short cls = 0; cls < numClasses; cls++) {
            if (!infeasibleClasses.contains(cls)) {
                List<IntVar> flow_in = new ArrayList<>();

                for (short i = 0; i < numTeachers; i++) {
                    if (classesTeacherCanTeach[i].contains(cls)) {
                        flow_in.add(x[i][cls]);
                    }
                }

                model.addEquality(LinearExpr.sum(flow_in.toArray(new IntVar[0])), 1);
            }
        }

        // Constrain 2: Do not assign pair of conflict classes to the same teacher.
        for (short i = 0; i < numTeachers; i++) {
            for (short[] pair : pairOfConflictClasses) {
                if (classesTeacherCanTeach[i].containsAll(Arrays.asList(pair[0], pair[1]))) {
                    IntVar[] conflict = new IntVar[2];

                    conflict[0] = x[i][pair[0]];
                    conflict[1] = x[i][pair[1]];

                    model.addLinearConstraint(LinearExpr.sum(conflict), 0, 1);
                }
            }
        }
    }

    private void phase1() {
        initModelAndSetUpFirst2Constrains();

        // Constrain 3: Find total load of a teacher: - (bound = totalLoadOfFeasibleClasses) - pre <= totalLoad[i] - bound <= -pre
        for (short i = 0; i < numTeachers; i++) {
            List<IntVar> vars = new ArrayList<>();
            List<Integer> coeffs = new ArrayList<>();

            vars.add(bound);
            coeffs.add(-1);

            for (Short cls : classesTeacherCanTeach[i]) {
                if (!infeasibleClasses.contains(cls)) {
                    vars.add(x[i][cls]);
                    coeffs.add(loadOfClass[cls]);
                }
            }

            model.addLinearConstraint(
                LinearExpr.scalProd(vars.toArray(new IntVar[0]), coeffs.stream().mapToInt(Integer::intValue).toArray()),
                -totalLoadOfFeasibleClasses - (int) teachers[i].getPrespecifiedHourLoad() * 60,
                (long) (-teachers[i].getPrespecifiedHourLoad() * 60));
        }

        // Objective function.
        model.minimize(LinearExpr.term(bound, 1));

        // Solves.
        CpSolver solver = new CpSolver();
        CpSolverStatus status = solver.solve(model);

        // Prints + Extracts solution.
        if (status == CpSolverStatus.OPTIMAL) {
            minOfMaxLoad = (int) (solver.objectiveValue() + 0.25);
            time += 1.0 * solver.wallTime() / 1000;

            System.out.println("Solution:");
            System.out.println("Max load per teacher: " + minOfMaxLoad);
        } else {
            System.err.println("The problem does not have an optimal solution!");
        }
    }

    private void phase2() {
        initModelAndSetUpFirst2Constrains();

        // Constrain 3: Find total load of a teacher: - pre <= totalLoad[i] - bound <= minOfMaxLoad - pre - (bound = 0)
        for (short i = 0; i < numTeachers; i++) {
            List<IntVar> vars = new ArrayList<>();
            List<Integer> coeffs = new ArrayList<>();

            vars.add(bound);
            coeffs.add(-1);

            for (Short cls : classesTeacherCanTeach[i]) {
                if (!infeasibleClasses.contains(cls)) {
                    vars.add(x[i][cls]);
                    coeffs.add(loadOfClass[cls]);
                }
            }

            model.addLinearConstraint(
                LinearExpr.scalProd(vars.toArray(new IntVar[0]), coeffs.stream().mapToInt(Integer::intValue).toArray()),
                -(int) teachers[i].getPrespecifiedHourLoad() * 60,
                (long) (minOfMaxLoad - teachers[i].getPrespecifiedHourLoad() * 60));
        }

        // Objective function.
        model.maximize(LinearExpr.term(bound, 1));

        // Solves.
        CpSolver solver = new CpSolver();
        CpSolverStatus status = solver.solve(model);

        // Prints + Extracts solution.
        if (status == CpSolverStatus.OPTIMAL) {
            maxOfMinLoad = (int) (solver.objectiveValue() + 0.25);
            time += 1.0 * solver.wallTime() / 1000;

            System.out.println("Solution:");
            System.out.println("Min load per teacher: " + maxOfMinLoad);
        } else {
            System.err.println("The problem does not have an optimal solution!");
        }
    }

    private void phase3() {
        int convergencePoint = (int) averageLoad;

        // Approach 1.
        boundOfThisRound = minOfMaxLoad;
        while (boundOfThisRound > convergencePoint) {
            time += optimalRound(RHS);
        }

        boundOfThisRound = maxOfMinLoad;
        while (boundOfThisRound <= convergencePoint) {
            time += optimalRound(LHS);
        }

        if (isValidSolution()) {
            System.out.println("\nEVERYTHING IS OK!");

            // Calculate SD.
            numTeachers = (short) teachers.length;
            long sum = 0;

            for (short i = 0; i < numTeachers; i++) {
                sum += optimizedTeachers[i];
            }

            System.out.println("SUM == TOTAL LOAD OF FEASIBLE CLASSES: " + (sum == totalLoadOfFeasibleClasses));

            double average = 1.0 * sum / numTeachers;
            double std = 0;

            for (short i = 0; i < numTeachers; i++) {
                std += (optimizedTeachers[i] - average) * (optimizedTeachers[i] - average);
            }

            optimalSD.add(Math.sqrt(std / numTeachers));
        }

        for (short i = 0, bound = (short) optimalSD.size(); i < bound; i++) {
            Double sd = optimalSD.get(i);
            System.out.println("SD ROUND " + i + " = " + sd);
        }

        System.out.println("TOTAL TIME = " + time);
    }

    private Double optimalRound(String mode) {
        teachersOfThisRound = new short[numTeachers]; // List of teachers which are not optimized.

        short idx = 0;
        for (short i = 0; i < optimizedTeachers.length; i++) {
            if (-1 == optimizedTeachers[i]) {
                teachersOfThisRound[idx++] = i;
            }
        }

        // Init.
        Loader.loadNativeLibraries();
        model = new CpModel();

        if (model == null) {
            System.out.println("Could not create solver SCIP");
            return null;
        }

        x = new IntVar[numTeachers][numClasses];

        for (short i = 0; i < numTeachers; i++) {
            for (Short cls : classesTeacherCanTeach[teachersOfThisRound[i]]) {
                if (!infeasibleClasses.contains(cls)) {
                    if (!optimizedClasses[cls]) {
                        x[i][cls] = model.newBoolVar("x[" + i + "][" + cls + "]");
                    }
                }
            }
        }

        // Constrains.
        // Constrain 1: Each class taught by only one teacher.
        for (Short cls = 0; cls < numClasses; cls++) {
            if (!infeasibleClasses.contains(cls)) {
                if (!optimizedClasses[cls]) {
                    List<IntVar> flow_in = new ArrayList<>();

                    for (short i = 0; i < numTeachers; i++) {
                        if (classesTeacherCanTeach[teachersOfThisRound[i]].contains(cls)) {
                            flow_in.add(x[i][cls]);
                        }
                    }

                    model.addEquality(LinearExpr.sum(flow_in.toArray(new IntVar[0])), 1);
                }
            }
        }

        // Constrain 2: A teacher not assigned to pair of conflict classes.
        for (short i = 0; i < numTeachers; i++) {
            for (short[] pair : pairOfConflictClasses) {
                if (!optimizedClasses[pair[0]] &&
                    !optimizedClasses[pair[1]] &&
                    classesTeacherCanTeach[teachersOfThisRound[i]].containsAll(Arrays.asList(pair[0], pair[1]))) {

                    IntVar[] conflict = new IntVar[2];

                    conflict[0] = x[i][pair[0]];
                    conflict[1] = x[i][pair[1]];

                    model.addLinearConstraint(LinearExpr.sum(conflict), 0, 1);
                }
            }
        }

        // Constrain 3: Calculate total load of a teacher.
        totalLoadOfTeacher = new IntVar[numTeachers];
        loadEqualBound = new IntVar[numTeachers];

        if (mode.equals(RHS)) {
            for (short i = 0; i < numTeachers; i++) {
                List<IntVar> calcTotalLoadOfTeacher = new ArrayList<>();
                List<Integer> coeffs = new ArrayList<>();

                loadEqualBound[i] = model.newBoolVar("loadEqualTarget[" + i + "]");
                totalLoadOfTeacher[i] = model.newIntVar(
                    maxOfMinLoad,
                    boundOfThisRound,
                    "totalLoadOfTeacher[" + i + "]");

                calcTotalLoadOfTeacher.add(totalLoadOfTeacher[i]);
                coeffs.add(-1);

                for (Short cls : classesTeacherCanTeach[teachersOfThisRound[i]]) {
                    if (!infeasibleClasses.contains(cls)) {
                        if (!optimizedClasses[cls]) {
                            calcTotalLoadOfTeacher.add(x[i][cls]);
                            coeffs.add(loadOfClass[cls]);
                        }
                    }
                }

                model.addLinearConstraint(
                    LinearExpr.scalProd(
                        calcTotalLoadOfTeacher.toArray(new IntVar[0]),
                        coeffs.stream().mapToInt(Integer::intValue).toArray()),
                    (long) -teachers[teachersOfThisRound[i]].getPrespecifiedHourLoad() * 60,
                    (long) (-teachers[teachersOfThisRound[i]].getPrespecifiedHourLoad() * 60));

                // totalLoadOfTeacher[i] = target --> loadEqualTarget[i] = 1, = 0 otherwise.
                IntVar isEqualBound = model.newBoolVar("isEqualBound[" + i + "]");

                model.addEquality(totalLoadOfTeacher[i], boundOfThisRound).onlyEnforceIf(isEqualBound);
                model.addLessThan(totalLoadOfTeacher[i], boundOfThisRound).onlyEnforceIf(isEqualBound.not());

                model.addEquality(loadEqualBound[i], 1).onlyEnforceIf(isEqualBound);
            }
        } else {
            for (short i = 0; i < numTeachers; i++) {
                List<IntVar> calcTotalLoadOfTeacher = new ArrayList<>();
                List<Integer> coeffs = new ArrayList<>();

                loadEqualBound[i] = model.newBoolVar("loadEqualTarget[" + i + "]");
                totalLoadOfTeacher[i] = model.newIntVar(
                    boundOfThisRound,
                    (long) averageLoad,
                    "totalLoadOfTeacher[" + i + "]");

                calcTotalLoadOfTeacher.add(totalLoadOfTeacher[i]);
                coeffs.add(-1);

                for (Short cls : classesTeacherCanTeach[teachersOfThisRound[i]]) {
                    if (!infeasibleClasses.contains(cls)) {
                        if (!optimizedClasses[cls]) {
                            calcTotalLoadOfTeacher.add(x[i][cls]);
                            coeffs.add(loadOfClass[cls]);
                        }
                    }
                }

                model.addLinearConstraint(
                    LinearExpr.scalProd(
                        calcTotalLoadOfTeacher.toArray(new IntVar[0]),
                        coeffs.stream().mapToInt(Integer::intValue).toArray()),
                    (long) -teachers[teachersOfThisRound[i]].getPrespecifiedHourLoad() * 60,
                    (long) (-teachers[teachersOfThisRound[i]].getPrespecifiedHourLoad() * 60));

                // totalLoadOfTeacher[i] = target --> loadEqualTarget[i] = 1, = 0 otherwise.
                IntVar isEqualBound = model.newBoolVar("isEqualBound[" + i + "]");

                model.addEquality(totalLoadOfTeacher[i], boundOfThisRound).onlyEnforceIf(isEqualBound);
                model.addGreaterThan(totalLoadOfTeacher[i], boundOfThisRound).onlyEnforceIf(isEqualBound.not());

                model.addEquality(loadEqualBound[i], 1).onlyEnforceIf(isEqualBound);
            }
        }

        // Objective function.
        model.minimize(LinearExpr.sum(loadEqualBound));

        // Solves.
        CpSolver solver = new CpSolver();
        CpSolverStatus status = solver.solve(model);

        // Prints + Extracts solution.
        if (status == CpSolverStatus.OPTIMAL) {
            short optimalValue = (short) (solver.objectiveValue() + 0.25);

            System.out.println("Target = " +
                               boundOfThisRound +
                               ", the smallest number of teacher whose total load equal target = " +
                               optimalValue);
            System.out.println("Optimal solution:");
            System.out.println("          Teacher          Classes, (total load)");

            time = solver.wallTime();
            analyseSolution(solver, mode);

            if (optimalValue != teachersOfThisRound.length - numTeachers) {
                System.err.println("SOLVER IS FAILURE!");
            }
        } else {
            System.err.println("The problem does not have an optimal solution!");
        }

        return 1.0 * time / 1000;
    }

    private void analyseSolution(CpSolver solver, String mode) {
        int boundOfNextRound;

        if (mode.equals(RHS)) {
            boundOfNextRound = -1;
        } else {
            boundOfNextRound = (int) averageLoad;
        }

        int[] totalLoadOfTeacherThisSolution = new int[optimizedTeachers.length];
        short numOptimizedTeachersThisRound = 0;

        for (short i = 0; i < optimizedTeachers.length; i++) {
            if (-1 != optimizedTeachers[i]) {
                totalLoadOfTeacherThisSolution[i] = optimizedTeachers[i];
            }
        }

        for (short i = 0; i < numTeachers; i++) {
            System.out.print("\n             " + i + "             ");

            int totalLoad = (int) (solver.value(totalLoadOfTeacher[i]) + 0.25);
            totalLoadOfTeacherThisSolution[teachersOfThisRound[i]] = totalLoad;

            if (totalLoad == boundOfThisRound) {
                numOptimizedTeachersThisRound++;
                optimizedTeachers[teachersOfThisRound[i]] = boundOfThisRound;

                for (Short cls : classesTeacherCanTeach[teachersOfThisRound[i]]) {
                    if (!infeasibleClasses.contains(cls)) {
                        if (!optimizedClasses[cls] && solver.value(x[i][cls]) > 0.75) {

                            optimizedClasses[cls] = true;
                            optimalClassesOfTeacher[teachersOfThisRound[i]].add(cls);

                            System.out.print(cls + ", ");
                        }
                    }
                }
            } else {
                if (mode.equals(RHS)) {
                    boundOfNextRound = Math.max(boundOfNextRound, totalLoad);
                } else {
                    boundOfNextRound = Math.min(boundOfNextRound, totalLoad);
                }

                // Print classes assigned for the teacher.
                for (Short cls : classesTeacherCanTeach[teachersOfThisRound[i]]) {
                    if (!infeasibleClasses.contains(cls)) {
                        if (!optimizedClasses[cls] && solver.value(x[i][cls]) > 0.75) {
                            System.out.print(cls + ", ");
                        }
                    }
                }
            }

            System.out.print(solver.value(loadEqualBound[i]));
            boolean isEqual = solver.value(loadEqualBound[i]) > 0.75;
            System.out.println("(totalLoad = " + totalLoad + ", totalLoad == boundOfThisRound: " + isEqual + ")");
        }

        // Calculate SD.
        short len = (short) totalLoadOfTeacherThisSolution.length;

        double average = 1.0 * totalLoadOfFeasibleClasses / len;
        double std = 0;

        for (short i = 0; i < len; i++) {
            std += (totalLoadOfTeacherThisSolution[i] - average) * (totalLoadOfTeacherThisSolution[i] - average);
        }

        optimalSD.add(Math.sqrt(std / len));

        boundOfThisRound = boundOfNextRound;
        numTeachers -= numOptimizedTeachersThisRound;
        System.out.println("BOUND OF NEXT ROUND = " + boundOfNextRound);
    }

    @Override
    public TeacherClassAssignmentOM getSolution() {
        List<AlgoClassIM> notAssigned = infeasibleClasses
            .stream()
            .map(cls -> classes.get(cls))
            .collect(Collectors.toList());

        om.setNotAssigned(notAssigned);
        return om;
    }
}
