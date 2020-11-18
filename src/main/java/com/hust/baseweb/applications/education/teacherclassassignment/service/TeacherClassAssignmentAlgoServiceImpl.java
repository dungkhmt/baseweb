package com.hust.baseweb.applications.education.teacherclassassignment.service;

import com.hust.baseweb.applications.education.entity.Course;
import com.hust.baseweb.applications.education.teacherclassassignment.model.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@Service
public class TeacherClassAssignmentAlgoServiceImpl implements TeacherClassAssignmentAlgoService {

    private boolean conflictTimeTable(AlgoClassIM cls1, AlgoClassIM cls2){
        //TODO by TuanLA
        return false;
    }
    @Override
    public TeacherClassAssignmentOM computeTeacherClassAssignment(AlgoTeacherAssignmentIM input) {
        //TODO by TuanLA
        AlgoTeacherIM[] algoTeacherIMs = input.getTeachers();
        AlgoClassIM[] algoClassIMS = input.getClasses();
        int n = algoClassIMS.length;// number of classes;
        int m = algoTeacherIMs.length;// number of teachers;
        HashMap<String, Integer> mTeacher2Index = new HashMap();
        for(int i = 0; i < m; i++){
            mTeacher2Index.put(algoTeacherIMs[i].getId(),i);
        }
        HashMap<String, List<Integer>> mCourseID2ClassIndex = new HashMap();
        for(int i = 0; i < n; i++){
            if(mCourseID2ClassIndex.get(algoClassIMS[i].getCourseId()) == null){
                mCourseID2ClassIndex.put(algoClassIMS[i].getCourseId(), new ArrayList<Integer>());
            }
            mCourseID2ClassIndex.get(algoClassIMS[i].getCourseId()).add(i);
        }
        HashSet<Integer>[] D = new HashSet[n];
        for(int i = 0; i < n; i++){
            D[i] = new HashSet<Integer>();
        }
        for(int i = 0; i < m; i++){
            AlgoTeacherIM t = algoTeacherIMs[i];
            for(int j = 0; j < t.getCourses().size(); j++){
                Course4Teacher course4Teacher = t.getCourses().get(j);
                if(mCourseID2ClassIndex.get(course4Teacher.getCourseId()) == null){
                    System.out.println("no class for course " + course4Teacher.getCourseId());
                }else
                for(int c: mCourseID2ClassIndex.get(course4Teacher.getCourseId())){
                    D[c].add(i);
                }
            }
        }
        for(int i = 0; i < n; i++){
            System.out.println("Class " + algoClassIMS[i].getId() + " " + algoClassIMS[i].getCourseId() + "-" + algoClassIMS[i].getCourseName() + ": ");
            for(int j : D[i]){
                System.out.println(algoTeacherIMs[j].getId());
            }
        }
        boolean[][] conflict = new boolean[n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                conflict[i][j] = conflictTimeTable(algoClassIMS[i],algoClassIMS[j]);
            }
        }
        CBLSSolver solver = new CBLSSolver(n,m,D,conflict);
        solver.solve();
        int[] sol = solver.getSolution();

        Random R = new Random();
        TeacherClassAssignmentModel[] assignmentModels = new TeacherClassAssignmentModel[algoClassIMS.length];
        for(int i = 0; i < algoClassIMS.length; i++){
            //int j = R.nextInt(algoTeacherIMs.length);
            AlgoTeacherIM t = algoTeacherIMs[sol[i]];
            assignmentModels[i] = new TeacherClassAssignmentModel(algoClassIMS[i],t);
        }

        TeacherClassAssignmentOM teacherClassAssignmentOM = new TeacherClassAssignmentOM(assignmentModels);

        return teacherClassAssignmentOM;
    }
}
