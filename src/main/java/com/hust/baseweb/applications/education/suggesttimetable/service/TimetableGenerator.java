package com.hust.baseweb.applications.education.suggesttimetable.service;

import com.google.ortools.Loader;
import com.google.ortools.sat.*;
import com.hust.baseweb.applications.education.suggesttimetable.entity.EduClass;
import com.hust.baseweb.applications.education.suggesttimetable.entity.EduCourse;
import com.hust.baseweb.applications.education.suggesttimetable.model.EduClassOM;
import com.hust.baseweb.applications.education.suggesttimetable.model.GroupClassesOM;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Le Anh Tuan
 */
final class TimetableGenerator {

    static class SolutionExtractor extends CpSolverSolutionCallback {

        private final IntVar[] vars;

        private final List<long[]> solutions = new ArrayList<>();

        public SolutionExtractor(IntVar[] vars) {
            this.vars = vars;
        }

        private void printSolution() {
            System.out.printf("Solution #%d: time = %.02f s%n", solutions.size(), wallTime());

            for (IntVar var : vars) {
                System.out.printf("  %s = %d%n", var.getName(), value(var));
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
//            printSolution();
            extractSolution();
        }
    }

    private final List<GroupClassesOM> classGroups;

    private final List<EduCourse> courses;

    private BidiMap<Integer, Short> classDVarMap; // Map class id with index of decision var.

    private short[] numClassOfGroup;

    private ArrayList<short[]> conflictSet;

    private boolean notGenerated = true;

    private List<List<EduClassOM>> timetables; // Solutions.

    public TimetableGenerator(List<GroupClassesOM> classGroups, List<EduCourse> courses) {
        this.classGroups = classGroups;
        this.courses = courses;
    }

    public List<List<EduClassOM>> generate() {
        if (notGenerated) {
            genSetOfConflictClassPairs();

            // Modeling.
            Loader.loadNativeLibraries();
            // Create the model.
            CpModel model = new CpModel();

            // Create the variables.
            int numClasses = classDVarMap.size();
            IntVar[] x = new IntVar[numClasses];

            // Init.
            for (short i = 0; i < numClasses; i++) {
                x[i] = model.newIntVar(0, 1, "class " + i);
            }

            // Create the constraints.
            // Constraint 1: Pick only one class in a group.
            short idx = 0;
            for (short numClass : numClassOfGroup) {
                if (numClass == 1) {
                    model.addEquality(x[idx], 1);
                    idx++;
                } else {
                    IntVar[] classes = new IntVar[numClass];

                    for (short i = 0; i < numClass; i++) {
                        classes[i] = x[idx];
                        idx++;
                    }

                    model.addEquality(LinearExpr.sum(classes), 1);
                }
            }

            // Constraint 2: Overlapping classes.
            for (short[] pair : conflictSet) {
                model.addLessThan(LinearExpr.sum(new IntVar[]{x[pair[0]], x[pair[1]]}), 2);
            }

            // Create a solver and solve the model.
            CpSolver solver = new CpSolver();
            SolutionExtractor extractor = new SolutionExtractor(x);
            solver.searchAllSolutions(model, extractor);

//            System.out.println(extractor.solutions.size() + " solutions found.");
            convertSolution(extractor.solutions);
            notGenerated = false;
        }

        return timetables;
    }

    private void convertSolution(final List<long[]> solutions) {
        timetables = new ArrayList<>();
        int numGroups = classGroups.size();
        Map<String, String> courseName = new HashMap();

        for (EduCourse course : courses) {
            courseName.put(course.getId(), course.getName());
        }

        // Convert.
        for (long[] solution : solutions) {
            List<EduClassOM> timetable = new ArrayList<>();

            short currGroup = 0;
            for (short i = 0; i < solution.length; i++) {
                if (currGroup == numGroups) {
                    break;
                }

                if (1 == solution[i]) {
                    Integer classId = classDVarMap.getKey(i);

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
                            courseName.get(clazz.getCourseId())))
                        .collect(Collectors.toList());

                    timetable.addAll(classes);
                    currGroup++;
                }
            }

            timetables.add(timetable);
        }
    }

    private void buildMap() {
        classDVarMap = new DualHashBidiMap<>();
        int size = classGroups.size();
        numClassOfGroup = new short[size];
        short idx = 0;

        for (short i = 0; i < size; i++) {
            HashSet<Integer> classIds = new HashSet<>();

            for (EduClass clazz : classGroups.get(i).getClasses()) {
                Integer classId = clazz.getClassId();

                if (!classIds.contains(classId)) {
                    classDVarMap.put(classId, idx);
                    classIds.add(classId);
                    idx++;
                }
            }

            numClassOfGroup[i] = (short) classIds.size();
        }

//        System.out.println(classDVarMap.toString());
    }

    private void genSetOfConflictClassPairs() {
        buildMap();

        conflictSet = new ArrayList<>();
        int numGroup = classGroups.size();

        for (short i = 0; i < numGroup; i++) {
            List<EduClass> firstGroupClasses = classGroups.get(i).getClasses();

            for (int j = i + 1; j < numGroup; j++) {
                List<EduClass> secondGroupClasses = classGroups.get(j).getClasses();

                for (EduClass firstClass : firstGroupClasses) {
                    for (EduClass secondClass : secondGroupClasses) {
                        if (firstClass.overlap(secondClass)) {
                            conflictSet.add(new short[]{
                                classDVarMap.get(firstClass.getClassId()),
                                classDVarMap.get(secondClass.getClassId())});
                        }
                    }
                }
            }
        }

//        for (short[] pair : conflictSet) {
//            System.out.println();
//            for (short clazz : pair) {
//                System.out.print(clazz + ", ");
//            }
//        }
    }
}
