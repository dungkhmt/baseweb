package com.hust.baseweb.applications.education.teacherclassassignment.service;

import com.hust.baseweb.applications.education.teacherclassassignment.model.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class TeacherClassAssignmentAlgoServiceImpl implements TeacherClassAssignmentAlgoService {

    private Map<Integer, String[]> period;

    // Remember to re-initialize this property after checking conflicts between all pairs of classes.
    private Map<Integer, ExtractTimetable> extractTimetable;

    public TeacherClassAssignmentAlgoServiceImpl() {
        extractTimetable = new HashMap<>();
        period = new HashMap<>();

        period.put(1, new String[]{"0645", "0730"});
        period.put(2, new String[]{"0730", "0815"});
        period.put(3, new String[]{"0825", "0910"});
        period.put(4, new String[]{"0920", "1005"});
        period.put(5, new String[]{"1015", "1100"});
        period.put(6, new String[]{"1100", "1145"});

        period.put(7, new String[]{"1230", "1315"});
        period.put(8, new String[]{"1315", "1400"});
        period.put(9, new String[]{"1410", "1455"});
        period.put(10, new String[]{"1505", "1550"});
        period.put(11, new String[]{"1600", "1645"});
        period.put(12, new String[]{"1645", "1730"});

        period.put(13, new String[]{"1745", "1830"});
        period.put(14, new String[]{"1830", "1915"});
    }

    private ExtractTimetable extractTimetable(AlgoClassIM classIM) {
        if (extractTimetable.containsKey(classIM.getId())) {
            return extractTimetable.get(classIM.getId());
        } else {
            ExtractTimetable timetable = new ExtractTimetable();
            List<String[]> sessions = Arrays
                .stream(classIM.getTimetable().split(";"))
                .map(session -> session.split(","))
                .collect(
                    Collectors.toList());

            int noSessions = sessions.size();
            String[] start = new String[noSessions];
            String[] end = new String[noSessions];
            Set<Integer>[] weeks = new Set[noSessions];

            for (int i = 0; i < noSessions; i++) {
                start[i] = normalize(StringUtils.deleteWhitespace(sessions.get(i)[1]), true);
                end[i] = normalize(StringUtils.deleteWhitespace(sessions.get(i)[2]), false);
                weeks[i] = new HashSet();

                List<String[]> weekStr = Arrays
                    .stream(Arrays.copyOfRange(sessions.get(i), 3, sessions.get(i).length - 1))
                    .map(ele -> StringUtils.deleteWhitespace(ele).split("-")).collect(Collectors.toList());

                for (String[] ele : weekStr) {
                    if (ele.length == 1) {
                        weeks[i].add(Integer.parseInt(ele[0]));
                    } else {
                        int from = Integer.parseInt(ele[0]);
                        int to = Integer.parseInt(ele[1]) + 1;

                        for (int j = from; j < to; j++) {
                            weeks[i].add(j);
                        }
                    }
                }
            }

            timetable.setStart(start);
            timetable.setEnd(end);
            timetable.setWeeks(weeks);

            extractTimetable.put(classIM.getId(), timetable);
            return timetable;
        }
    }

    private String normalize(String timetable, boolean start) {
        String result;

        switch (timetable.length()) {
            case 3:
                final int period = (Integer.parseInt(timetable.substring(1, 2)) - 1) * 6 +
                                   Integer.parseInt(timetable.substring(2, 3));

                if (start) {
                    result = timetable.substring(0, 2) +
                             this.period.get(period)[0];
                } else {
                    result = timetable.substring(0, 2) +
                             this.period.get(period)[1];
                }
                break;
            default:
                result = timetable;
        }

        return result;
    }

    private boolean conflictTimeTable(AlgoClassIM cls1, AlgoClassIM cls2) {
        ExtractTimetable timetable1 = extractTimetable(cls1);
        ExtractTimetable timetable2 = extractTimetable(cls2);

        for (int i = 0; i < timetable1.getStart().length; i++) {
            for (int j = 0; j < timetable2.getStart().length; j++) {
                // Session does not overlap.
                if (timetable1.getEnd()[i].compareTo(timetable2.getStart()[j]) < 0 ||
                    timetable2.getEnd()[j].compareTo(timetable1.getStart()[i]) < 0) {
                } else { // Sessions overlap.
                    Set<Integer> intersection = new HashSet<>(timetable1.getWeeks()[i]);
                    intersection.retainAll(timetable2.getWeeks()[j]);

                    if (intersection.size() > 0) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
    public String name(){
        return "TeacherClassAssignmentAlgoServiceImpl";
    }

    @Override
    public TeacherClassAssignmentOM computeTeacherClassAssignment(AlgoTeacherAssignmentIM input) {
        AlgoTeacherIM[] algoTeacherIMs = input.getTeachers();
        AlgoClassIM[] algoClassIMS = input.getClasses();
        int n = algoClassIMS.length;// number of classes;
        int m = algoTeacherIMs.length;// number of teachers;
        double[] hourClass;// hourClass[i] is the number of hours of class i
        HashMap<String, Integer> mTeacher2Index = new HashMap();
        for (int i = 0; i < m; i++) {
            mTeacher2Index.put(algoTeacherIMs[i].getId(), i);
        }
        HashMap<String, List<Integer>> mCourseID2ClassIndex = new HashMap();
        for (int i = 0; i < n; i++) {
            if (mCourseID2ClassIndex.get(algoClassIMS[i].getCourseId()) == null) {
                mCourseID2ClassIndex.put(algoClassIMS[i].getCourseId(), new ArrayList<Integer>());
            }
            mCourseID2ClassIndex.get(algoClassIMS[i].getCourseId()).add(i);
        }
        HashSet<Integer>[] D = new HashSet[n];
        hourClass = new double[n];
        for (int i = 0; i < n; i++) {
            D[i] = new HashSet<Integer>();
            hourClass[i] = algoClassIMS[i].getHourLoad();
        }
        for (int i = 0; i < m; i++) {
            AlgoTeacherIM t = algoTeacherIMs[i];
            for (int j = 0; j < t.getCourses().size(); j++) {
                Course4Teacher course4Teacher = t.getCourses().get(j);
                if (mCourseID2ClassIndex.get(course4Teacher.getCourseId()) == null) {
                    //System.out.println(name() + "::no class for course " + course4Teacher.getCourseId());
                } else {
                    for (int c : mCourseID2ClassIndex.get(course4Teacher.getCourseId())) {
                        D[c].add(i);
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            /*
            System.out.println("Class " +
                               algoClassIMS[i].getId() +
                               " " +
                               algoClassIMS[i].getCourseId() +
                               "-" +
                               algoClassIMS[i].getCourseName() +
                               ": ");
            */
            for (int j : D[i]) {
                if(algoTeacherIMs[j].getId().equals("bang.banha@hust.edu.vn")) {
                    System.out.println("teacher " + j + ": " + algoTeacherIMs[j].getId());
                    System.out.println("Class " +
                                       algoClassIMS[i].getId() +
                                       " " +
                                       algoClassIMS[i].getCourseId() +
                                       "-" +
                                       algoClassIMS[i].getCourseName() +
                                       ": ");

                }
            }
        }
        boolean[][] conflict = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                conflict[i][j] = conflictTimeTable(algoClassIMS[i], algoClassIMS[j]);
                if(conflict[i][j]){
                    //System.out.println("Conflict " + algoClassIMS[i].getTimetable() + " VS. " + algoClassIMS[j].getTimetable());
                }else{
                    //System.out.println("NOT Conflict " + algoClassIMS[i].getTimetable() + " VS. " + algoClassIMS[j].getTimetable());
                }
            }
        }
        CBLSSolver solver = new CBLSSolver(n, m, D, conflict, hourClass);
        solver.solve();
        int[] sol = solver.getSolution();

        HashMap<AlgoTeacherIM, List<AlgoClassIM>> mTeacher2AssignedClass = new HashMap();

        Random R = new Random();
        TeacherClassAssignmentModel[] assignmentModels = new TeacherClassAssignmentModel[algoClassIMS.length];
        List<AlgoClassIM> notAssigned = new ArrayList<AlgoClassIM>();
        for (int i = 0; i < algoClassIMS.length; i++) {
            //int j = R.nextInt(algoTeacherIMs.length);
            if(sol[i] >= 0) {
                AlgoTeacherIM t = algoTeacherIMs[sol[i]];
                assignmentModels[i] = new TeacherClassAssignmentModel(algoClassIMS[i], t);
                if(mTeacher2AssignedClass.get(t) == null){
                    mTeacher2AssignedClass.put(t,new ArrayList<AlgoClassIM>());
                }
                mTeacher2AssignedClass.get(t).add(algoClassIMS[i]);
            }else{
                notAssigned.add(algoClassIMS[i]);
            }
        }
        ClassesAssigned2TeacherModel[] classesAssigned2TeacherModels = new ClassesAssigned2TeacherModel[algoTeacherIMs.length];
        for(int t = 0;t < algoTeacherIMs.length; t++){
            classesAssigned2TeacherModels[t] = new ClassesAssigned2TeacherModel(algoTeacherIMs[t],mTeacher2AssignedClass.get(algoTeacherIMs[t]));
        }
        int nbEmpty = 0;
        for(int i = 0; i < n; i++) if(D[i].size() == 0){
            //System.out.println("empty domain course " + algoClassIMS[i].getCourseId() + " - " + algoClassIMS[i].getCourseName());
            nbEmpty++;
        }
        System.out.println("nb empty domain = " + nbEmpty);
        TeacherClassAssignmentOM teacherClassAssignmentOM = new TeacherClassAssignmentOM(assignmentModels,classesAssigned2TeacherModels,notAssigned);

        int[] nbCourseOfTeacher = new int[m];
        for(int i = 0; i < m; i++) nbCourseOfTeacher[i] = 0;
        for(int i = 0; i < n; i++){
            if(sol[i] >= 0){
                nbCourseOfTeacher[sol[i]]++;
            }
        }
        for(int t = 0;t < m; t++){
            System.out.println("Teacher[" + t + "] = " + algoTeacherIMs[t].getId() + " has " + nbCourseOfTeacher[t]);
            for(int i = 0; i < n; i++){
                for(int j = i+1; j < n; j++){
                    if(sol[i] == t && sol[j] == t && conflict[i][j]){
                        System.out.println("BUG with class " + i + " and " + j + " of teacher " + t + ": " + algoClassIMS[i].getTimetable() +
                                           " <-> " + algoClassIMS[j].getTimetable());
                    }
                }
            }
        }
        return teacherClassAssignmentOM;
    }
}
