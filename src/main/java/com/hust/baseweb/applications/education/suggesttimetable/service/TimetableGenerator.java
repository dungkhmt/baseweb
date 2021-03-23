package com.hust.baseweb.applications.education.suggesttimetable.service;

import com.google.ortools.Loader;
import com.google.ortools.sat.*;
import com.hust.baseweb.applications.education.suggesttimetable.entity.EduClass;
import com.hust.baseweb.applications.education.suggesttimetable.model.EduClassOM;
import com.hust.baseweb.applications.education.suggesttimetable.model.FindAndGroupClassesOM;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.BidiMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Le Anh Tuan
 */
@AllArgsConstructor
public class TimetableGenerator {

    static class SolutionExtractor extends CpSolverSolutionCallback {

        private int solutionCount;

        private final IntVar[] vars;

        private final List<long[]> solutions = new ArrayList<>();

        public SolutionExtractor(IntVar[] vars) {
            this.vars = vars;
        }

        private void printSolution() {
            System.out.printf("Solution #%d: time = %.02f s%n", solutionCount, wallTime());

            for (IntVar v : vars) {
                System.out.printf("  %s = %d%n", v.getName(), value(v));
            }
        }

        private void extractSolution() {
            long[] solution = new long[vars.length];

            for (short i = 0, size = (short) solution.length; i < size; i++) {
                solution[i] = value(vars[i]);
            }

            solutions.add(solution);
        }

        @Override
        public void onSolutionCallback() {
            solutionCount++;
            printSolution();
            extractSolution();
        }

        public int getSolutionCount() {
            return solutionCount;
        }
    }

    private final List<FindAndGroupClassesOM> classGroups;

    private final BidiMap<Integer, Integer> classIndexMap;

    private final ArrayList<short[]> conflictSet;

    public List<List<EduClassOM>> generate() {
        Loader.loadNativeLibraries();
        // Create the model.
        CpModel model = new CpModel();

        // Create the variables.
        int numClasses = classIndexMap.size();
        IntVar[] x = new IntVar[numClasses];

        // Init.
        for (short i = 0; i < numClasses; i++) {
            x[i] = model.newIntVar(0, 1, "class " + i);
        }

        // Create the constraints.
        // Constraint 1: Pick only one class in a group.
        for (FindAndGroupClassesOM group : classGroups) {
            List<EduClass> groupClasses = group.getClasses();
            int size = groupClasses.size();
            IntVar[] classes = new IntVar[size];

            for (short i = 0; i < size; i++) {
                classes[i] = x[classIndexMap.get(groupClasses.get(i).getClassId())];
            }

            model.addEquality(LinearExpr.sum(classes), 1);
        }

        // Constraint 2: Overlapping classes.
        for (short[] pair : conflictSet) {
            model.addLessThan(LinearExpr.sum(new IntVar[]{x[pair[0]], x[pair[1]]}), 2);
        }

        // Create a solver and solve the model.
        CpSolver solver = new CpSolver();
        SolutionExtractor extractor = new SolutionExtractor(x);
        solver.searchAllSolutions(model, extractor);

        System.out.println(extractor.getSolutionCount() + " solutions found.");
        return convertSolution(extractor.solutions);
    }

    private List<List<EduClassOM>> convertSolution(final List<long[]> solutions) {
        List<List<EduClassOM>> timetables = new ArrayList<>();
        short numGroups = (short) classGroups.size();

        for (long[] solution : solutions) {
            List<EduClassOM> timetable = new ArrayList<>();

            short currGroup = 0;
            for (short i = 0; i < solution.length; i++) {
                if (currGroup == numGroups) {
                    break;
                }

                if (1 == solution[i]) {
                    Integer classId = classIndexMap.getKey(i);

                    List<EduClassOM> classes = classGroups
                        .get(currGroup)
                        .getClasses()
                        .stream()
                        .filter(clazz -> clazz.getClassId().equals(classId))
                        .map(clazz -> new EduClassOM(
                            clazz.getClassId(),
                            clazz.getAttachedClassId(),
                            clazz.getCourseId(),
                            clazz.getCredit(),
                            clazz.getDayOfWeek(),
                            clazz.getStartTime(),
                            clazz.getEndTime(),
                            clazz.getShift(),
                            clazz.getWeeks(),
                            clazz.getRoom(),
                            clazz.getClassType(),
                            null))
                        .collect(Collectors.toList());

                    timetable.addAll(classes);
                    currGroup++;
                }
            }

            timetables.add(timetable);
        }

        return timetables;
    }
}
