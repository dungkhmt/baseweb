package com.hust.baseweb.applications.education.teacherclassassignment.service;

import com.google.gson.Gson;
import com.hust.baseweb.applications.education.entity.EduCourse;
import com.hust.baseweb.applications.education.repo.EduCourseRepo;
import com.hust.baseweb.applications.education.teacherclassassignment.entity.*;
import com.hust.baseweb.applications.education.teacherclassassignment.entity.compositeid.TeacherCourseId;
import com.hust.baseweb.applications.education.teacherclassassignment.model.*;
import com.hust.baseweb.applications.education.teacherclassassignment.repo.*;
import com.hust.baseweb.applications.education.teacherclassassignment.utils.CheckConflict;
import com.hust.baseweb.applications.education.teacherclassassignment.utils.TimetableConflictChecker;
import com.hust.baseweb.entity.UserLogin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class ClassTeacherAssignmentPlanServiceImpl implements ClassTeacherAssignmentPlanService {

    private ClassTeacherAssignmentPlanRepo classTeacherAssignmentPlanRepo;
    private ClassTeacherAssignmentClassInfoRepo classTeacherAssignmentClassInfoRepo;
    private TeacherCourseRepo teacherCourseRepo;
    private EduTeacherRepo eduTeacherRepo;
    private EduCourseRepo eduCourseRepo;
    private TeacherClassAssignmentSolutionRepo teacherClassAssignmentSolutionRepo;
    private TeacherCourseForAssignmentPlanRepo teacherCourseForAssignmentPlanRepo;
    private TeacherForAssignmentPlanRepo teacherForAssignmentPlanRepo;

    private TeacherClassAssignmentAlgoService teacherClassAssignmentAlgoService;

    @Override
    public ClassTeacherAssignmentPlan create(UserLogin u, ClassTeacherAssignmentPlanCreateModel input) {
        ClassTeacherAssignmentPlan classTeacherAssignmentPlan = new ClassTeacherAssignmentPlan();
        classTeacherAssignmentPlan.setPlanName(input.getPlanName());
        classTeacherAssignmentPlan.setCreatedStamp(new Date());
        classTeacherAssignmentPlan.setCreatedByUserLoginId(u.getUserLoginId());
        classTeacherAssignmentPlan = classTeacherAssignmentPlanRepo.save(classTeacherAssignmentPlan);
        return classTeacherAssignmentPlan;
    }

    @Override
    public List<ClassTeacherAssignmentPlan> findAll() {
        List<ClassTeacherAssignmentPlan> classTeacherAssignmentPlans = classTeacherAssignmentPlanRepo.findAll();
        return classTeacherAssignmentPlans;
    }

    @Override
    public ClassTeacherAssignmentPlanDetailModel getClassTeacherAssignmentPlanDetail(UUID planId) {
        ClassTeacherAssignmentPlan classTeacherAssignmentPlan = classTeacherAssignmentPlanRepo
            .findById(planId)
            .orElse(null);
        ClassTeacherAssignmentPlanDetailModel classTeacherAssignmentPlanDetailModel = new ClassTeacherAssignmentPlanDetailModel();
        classTeacherAssignmentPlanDetailModel.setPlanId(classTeacherAssignmentPlan.getPlanId());
        classTeacherAssignmentPlanDetailModel.setPlanName(classTeacherAssignmentPlan.getPlanName());
        classTeacherAssignmentPlanDetailModel.setCreatedByUserLoginId(classTeacherAssignmentPlan.getCreatedByUserLoginId());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String dateTimeStr = dateFormat.format(classTeacherAssignmentPlan.getCreatedStamp());
        classTeacherAssignmentPlanDetailModel.setCreatedDate(dateTimeStr);

        return classTeacherAssignmentPlanDetailModel;
    }

    @Override

    //public List<ClassTeacherAssignmentClassInfo> findAllClassTeacherAssignmentClassByPlanId(UUID planId) {
    public List<ClassInfoForAssignment2TeacherModel> findAllClassTeacherAssignmentClassByPlanId(UUID planId) {
        List<ClassTeacherAssignmentClassInfo> classTeacherAssignmentClassInfos =
            classTeacherAssignmentClassInfoRepo.findAllByPlanId(planId);
        List<ClassInfoForAssignment2TeacherModel> lst = new ArrayList();
        List<TeacherCourse> teacherCourses = teacherCourseRepo.findAll();
        HashMap<String, List<TeacherCourse>> mCourseId2Teacher = new HashMap();
        for (TeacherCourse tc : teacherCourses) {
            if (mCourseId2Teacher.get(tc.getCourseId()) == null) {
                mCourseId2Teacher.put(tc.getCourseId(), new ArrayList<>());
            }
            mCourseId2Teacher.get(tc.getCourseId()).add(tc);
        }
        List<TeacherCourseForAssignmentPlan> teacherCourseForAssignmentPlans = teacherCourseForAssignmentPlanRepo.findAllByPlanId(
            planId);
        List<TeacherForAssignmentPlan> teacherForAssignmentPlans = teacherForAssignmentPlanRepo.findAllByPlanId(planId);
        HashSet<String> teacherPlanIds = new HashSet();
        for (TeacherForAssignmentPlan t : teacherForAssignmentPlans) {
            teacherPlanIds.add(t.getTeacherId());
        }
        HashMap<String, List<TeacherCourseForAssignmentPlan>> mCourseId2TeacherInPlan = new HashMap();
        for (TeacherCourseForAssignmentPlan tcp : teacherCourseForAssignmentPlans) {
            if (mCourseId2TeacherInPlan.get(tcp.getCourseId()) == null) {
                mCourseId2TeacherInPlan.put(tcp.getCourseId(), new ArrayList());
            }

            if (teacherPlanIds.contains(tcp.getTeacherId())) {
                mCourseId2TeacherInPlan.get(tcp.getCourseId()).add(tcp);
            }
        }

        for (ClassTeacherAssignmentClassInfo c : classTeacherAssignmentClassInfos) {
            ClassInfoForAssignment2TeacherModel classInfoForAssignment2TeacherModel =
                new ClassInfoForAssignment2TeacherModel(c);
            int nbTeachers = 0;

            if (mCourseId2Teacher.get(c.getCourseId()) != null) {
                nbTeachers = mCourseId2Teacher.get(c.getCourseId()).size();
            }

            int nbTeachersInPlan = 0;
            if (mCourseId2TeacherInPlan.get(c.getCourseId()) != null) {
                nbTeachersInPlan = mCourseId2TeacherInPlan.get(c.getCourseId()).size();
            }

            classInfoForAssignment2TeacherModel.setNumberPosibleTeachers(nbTeachers);
            classInfoForAssignment2TeacherModel.setNumberPosibleTeachersInPlan(nbTeachersInPlan);

            lst.add(classInfoForAssignment2TeacherModel);
        }
        //return classTeacherAssignmentClassInfos;
        return lst;
    }

    @Transactional
    @Override
    public boolean extractExcelAndStoreDB(UUID planId, MultipartFile file) {
        List<ClassTeacherAssignmentClassInfo> lst = new ArrayList();
        try {
            InputStream inputStream = file.getInputStream();
            HSSFWorkbook wb = new HSSFWorkbook(inputStream);
            HSSFSheet sheet = wb.getSheetAt(0);
            int sz = sheet.getLastRowNum();
            for (int i = 1; i <= sz; i++) {
                Row row = sheet.getRow(i);
                Cell c = row.getCell(0);
                String schoolName = c.getStringCellValue();
                c = row.getCell(1);
                String semesterId = c.getStringCellValue();
                c = row.getCell(2);
                String classId = c.getStringCellValue();
                c = row.getCell(3);
                String courseId = c.getStringCellValue();
                c = row.getCell(4);
                String className = c.getStringCellValue();
                c = row.getCell(5);
                String creditInfo = c.getStringCellValue();
                c = row.getCell(6);
                String classNote = c.getStringCellValue();

                c = row.getCell(7);
                String program = c.getStringCellValue();
                c = row.getCell(8);
                String semesterType = c.getStringCellValue();
                c = row.getCell(9);
                int enrollment = Integer.valueOf(c.getStringCellValue());
                c = row.getCell(10);
                int maxEnrollment = Integer.valueOf(c.getStringCellValue());

                c = row.getCell(12);
                String timeTable = c.getStringCellValue();
                c = row.getCell(13);
                String lesson = c.getStringCellValue();
                c = row.getCell(14);
                String department = c.getStringCellValue();

                ClassTeacherAssignmentClassInfo cls = new ClassTeacherAssignmentClassInfo();
                cls.setClassId(classId);
                cls.setClassName(className);
                cls.setCourseId(courseId);
                cls.setClassNote(classNote);
                cls.setSemesterId(semesterId);
                cls.setSemesterType(semesterType);
                cls.setProgram(program);
                cls.setTimeTable(timeTable);
                cls.setLesson(lesson);
                cls.setDepartmentId(department);
                cls.setEnrollment(enrollment);
                cls.setMaxEnrollment(maxEnrollment);
                cls.setPlanId(planId);
                lst.add(cls);
                log.info(classId + "\t" + courseId + "\t" + className + "\t" + timeTable);
                cls = classTeacherAssignmentClassInfoRepo.save(cls);
            }
            inputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public List<EduTeacher> findAllTeachers() {
        return eduTeacherRepo.findAll();
    }

    @Override
    public List<TeacherForAssignmentPlan> findAllTeacherByPlanId(UUID planId) {
        return teacherForAssignmentPlanRepo.findAllByPlanId(planId);
    }

    @Transactional
    @Override
    public boolean addTeacherToAssignmentPlan(UUID planId, String teacherList) {
        log.info("addTeacherToAssignmentPlan");
        String[] lst = teacherList.split(";");
        Gson gson = new Gson();
        for (String t : lst) {
            TeacherMaxHourLoad teacherMaxHourLoad = gson.fromJson(t, TeacherMaxHourLoad.class);
            TeacherForAssignmentPlan teacherForAssignmentPlan = new TeacherForAssignmentPlan();
            teacherForAssignmentPlan.setMaxHourLoad(teacherMaxHourLoad.getMaxHourLoad());
            teacherForAssignmentPlan.setTeacherId(teacherMaxHourLoad.getTeacherId());
            teacherForAssignmentPlan.setPlanId(planId);

            teacherForAssignmentPlan = teacherForAssignmentPlanRepo.save(teacherForAssignmentPlan);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean removeTeacherFromAssignmentPlan(UUID planId, String teacherList) {
        log.info("removeTeacherFromAssignmentPlan");
        String[] lst = teacherList.split(";");
        Gson gson = new Gson();
        for (String t : lst) {
            TeacherMaxHourLoad teacherMaxHourLoad = gson.fromJson(t, TeacherMaxHourLoad.class);
            TeacherForAssignmentPlan teacherForAssignmentPlan
                = teacherForAssignmentPlanRepo.findByTeacherIdAndPlanId(teacherMaxHourLoad.getTeacherId(), planId);
            if (teacherForAssignmentPlan != null) {
                teacherForAssignmentPlanRepo.delete(teacherForAssignmentPlan);
                log.info("removeTeacherFromAssignmentPlan, delete (" +
                         teacherMaxHourLoad.getTeacherId() +
                         "," +
                         planId +
                         ")");
            }

        }
        return true;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class ClassIdModel {

        String classId;
    }

    @Override
    public boolean removeClassFromAssignmentPlan(UUID planId, String classList) {
        log.info("removeClassFromAssignmentPlan");
        String[] lst = classList.split(";");
        Gson gson = new Gson();
        for (String t : lst) {
            ClassIdModel cm = gson.fromJson(t, ClassIdModel.class);
            String classId = cm.getClassId();
            ClassTeacherAssignmentClassInfo cls = classTeacherAssignmentClassInfoRepo.findById(classId).orElse(null);
            if (cls != null) {
                classTeacherAssignmentClassInfoRepo.delete(cls);
                log.info("removeClassFromAssignmentPlan, remove classId " + classId + " OK");
            }
        }
        return true;
    }

    @Override
    public List<TeacherCourse> findAllTeacherCourse() {
        List<TeacherCourse> teacherCourses = teacherCourseRepo.findAll();
        return teacherCourses;
    }

    @Override
    public List<TeacherCourseForAssignmentPlan> findTeacherCourseOfPlan(UUID planId) {
        return teacherCourseForAssignmentPlanRepo.findAllByPlanId(planId);
    }

    @Transactional
    @Override
    public boolean extractExcelAndStoreDBTeacherCourse(UUID planId, String choice, MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            HSSFWorkbook wb = new HSSFWorkbook(inputStream);
            HSSFSheet sheet = wb.getSheetAt(0);
            int sz = sheet.getLastRowNum();
            List<TeacherCourse> lst = new ArrayList();
            List<EduTeacher> eduTeachers = new ArrayList();
            List<EduCourse> eduCourses = new ArrayList();
            HashMap<TeacherCourseId, TeacherCourse> mId2TeacherCourse = new HashMap();
            HashSet<String> teacherIds = new HashSet();
            HashSet<String> courseIds = new HashSet();
            for (int i = 1; i <= sz; i++) {
                Row row = sheet.getRow(i);
                Cell c = row.getCell(0);
                String courseId = c.getStringCellValue();

                c = row.getCell(1);
                String courseName = c.getStringCellValue();

                c = row.getCell(3);
                String teacherName = c.getStringCellValue();

                c = row.getCell(4);
                String teacherId = c.getStringCellValue();
                //c = row.getCell(5);
                //int maxCredits = Integer.valueOf(c.getStringCellValue());
                c = row.getCell(6);
                int priority = 0;
                if (c.getCellType().equals(CellType.STRING)) {
                    priority = Integer.valueOf(c.getStringCellValue());
                } else if (c.getCellType().equals(CellType.NUMERIC)) {
                    priority = (int) c.getNumericCellValue();
                }

                TeacherCourse teacherCourse = new TeacherCourse();
                teacherCourse.setCourseId(courseId);
                teacherCourse.setTeacherId(teacherId);
                teacherCourse.setPriority(priority);

                TeacherCourseId id = new TeacherCourseId(courseId, teacherId);

                if (!teacherIds.contains(teacherId)) {
                    EduTeacher eduTeacher = new EduTeacher();
                    eduTeacher.setTeacherId(teacherId);
                    eduTeacher.setTeacherName(teacherName);

                    eduTeachers.add(eduTeacher);
                    teacherIds.add(teacherId);
                }
                if (!courseIds.contains(courseId)) {
                    EduCourse eduCourse = new EduCourse();
                    eduCourse.setId(courseId);
                    eduCourse.setName(courseName);
                    eduCourse.setCredit((short) 0);
                    eduCourse.setCreatedStamp(new Date());

                    eduCourses.add(eduCourse);
                    courseIds.add(courseId);
                }

                if (mId2TeacherCourse.get(id) == null) {
                    mId2TeacherCourse.put(id, teacherCourse);
                    lst.add(teacherCourse);
                    //log.info("extractExcelAndStoreDBTeacherCourse, add new " + courseId + ", " + teacherId + ", " + priority);
                } else {
                    log.info("extractExcelAndStoreDBTeacherCourse, " + courseId + ", " + teacherId + ", " + priority +
                             " EXISTS");
                }

                //teacherCourse  = teacherCourseRepo.save(teacherCourse);
                //log.info("extractExcelAndStoreDBTeacherCourse, " + courseId + ", " + teacherId + ", " + priority);
            }
            log.info("extractExcelAndStoreDBTeacherCourse, choice = " + choice + " courses.sz = " + eduCourses.size()
                     + " teachers.sz = " + eduTeachers.size() + ", teacher-course.sz = " + lst.size());


            if (choice.equals("UPLOAD_COURSE")) {
                // check and store courses
                for (EduCourse c : eduCourses) {

                    EduCourse eduCourse = eduCourseRepo.findById(c.getId()).orElse(null);
                    if (eduCourse != null) {
                        // EXISTS, do nothing
                        log.info("extractExcelAndStoreDBTeacherCourse, course " + c.getId() + " EXISTS");
                    } else {
                        c = eduCourseRepo.save(c);
                        log.info("extractExcelAndStoreDBTeacherCourse, course " + c.getId() + " -> INSERTED");
                    }
                }
            } else if (choice.equals("UPLOAD_TEACHER")) {

                // check and store teachers
                for (EduTeacher t : eduTeachers) {
                    EduTeacher teacher = eduTeacherRepo.findById(t.getTeacherId()).orElse(null);
                    if (teacher != null) {
                        // EXISTS do nothing
                        log.info("extractExcelAndStoreDBTeacherCourse, teacher " + t.getTeacherId() + " EXISTS");
                    } else {
                        t = eduTeacherRepo.save(t);
                        log.info("extractExcelAndStoreDBTeacherCourse, teacher " + t.getTeacherId() + " INSERTED");
                    }
                }
            } else if (choice.equals("UPLOAD_TEACHER_COURSE")) {
                log.info("extractExcelAndStoreDBTeacherCourse, saveAll teacher-course");
                teacherCourseRepo.saveAll(lst);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean autoAssignTeacher2Class(UUID planId) {
        List<ClassTeacherAssignmentClassInfo> classes = classTeacherAssignmentClassInfoRepo.findAllByPlanId(planId);
        List<EduTeacher> allteachers = eduTeacherRepo.findAll();
        //List<TeacherCourse> teacherCourses = teacherCourseRepo.findAll();
        HashMap<String, EduTeacher> mId2Teacher = new HashMap();
        for (EduTeacher t : allteachers) {
            mId2Teacher.put(t.getTeacherId(), t);
        }

        List<TeacherForAssignmentPlan> teachers = teacherForAssignmentPlanRepo.findAllByPlanId(planId);
        List<TeacherCourseForAssignmentPlan> teacherCourses = teacherCourseForAssignmentPlanRepo
            .findAllByPlanId(planId);


        List<EduCourse> eduCourses = eduCourseRepo.findAll();
        HashMap<String, EduCourse> mId2Course = new HashMap();
        HashMap<String, List<Course4Teacher>> mTeacher2Courses = new HashMap();

        for (EduCourse c : eduCourses) {
            mId2Course.put(c.getId(), c);
        }
        //for(TeacherCourse tc: teacherCourses){
        for (TeacherCourseForAssignmentPlan tc : teacherCourses) {
            if (mTeacher2Courses.get(tc.getTeacherId()) == null) {
                mTeacher2Courses.put(tc.getTeacherId(), new ArrayList<>());
            }
            EduCourse course = mId2Course.get(tc.getCourseId());
            if (course != null) {
                Course4Teacher c4t = new Course4Teacher(course.getId(), course.getName(), null);
                mTeacher2Courses.get(tc.getTeacherId()).add(c4t);
            }

        }

        // load existing solution/assignment
        List<TeacherClassAssignmentSolution> preAssignmentSolutions =
            teacherClassAssignmentSolutionRepo.findAllByPlanId(planId);


        AlgoClassIM[] inputClass = new AlgoClassIM[classes.size()];
        AlgoTeacherIM[] inputTeacher = new AlgoTeacherIM[teachers.size()];

        HashMap<String, AlgoClassIM> mClassId2AlgoClass = new HashMap();
        for (int i = 0; i < classes.size(); i++) {
            ClassTeacherAssignmentClassInfo c = classes.get(i);
            AlgoClassIM ci = new AlgoClassIM();
            ci.setClassCode(c.getClassId());
            ci.setTimetable(c.getLesson());
            ci.setClassType(c.getClassType());
            ci.setCourseId(c.getCourseId());
            ci.setCourseName(c.getCourseId());
            ci.setHourLoad(c.getHourLoad());// TOBE UPGRADED

            mClassId2AlgoClass.put(c.getClassId(), ci);

            inputClass[i] = ci;
        }

        HashMap<String, AlgoTeacherIM> mTeacherId2AlgoTeacher = new HashMap();
        for (int i = 0; i < teachers.size(); i++) {
            TeacherForAssignmentPlan t = teachers.get(i);
            EduTeacher teacher = mId2Teacher.get(t.getTeacherId());//teachers.get(i);

            AlgoTeacherIM ti = new AlgoTeacherIM();
            ti.setId(t.getTeacherId());
            ti.setName(teacher.getTeacherName());
            ti.setPrespecifiedHourLoad(t.getMaxHourLoad());// TOBE UPGRADED
            List<Course4Teacher> course4Teachers = mTeacher2Courses.get(t.getTeacherId());
            ti.setCourses(course4Teachers);

            mTeacherId2AlgoTeacher.put(t.getTeacherId(), ti);

            inputTeacher[i] = ti;
        }

        TeacherClassAssignmentModel[] preAssignment = new TeacherClassAssignmentModel[preAssignmentSolutions.size()];
        for (int i = 0; i < preAssignmentSolutions.size(); i++) {
            TeacherClassAssignmentSolution s = preAssignmentSolutions.get(i);
            String teacherId = s.getTeacherId();
            String classId = s.getClassId();
            AlgoClassIM ci = mClassId2AlgoClass.get(classId);
            AlgoTeacherIM ti = mTeacherId2AlgoTeacher.get(teacherId);

            preAssignment[i] = new TeacherClassAssignmentModel(ci, ti);
        }

        AlgoTeacherAssignmentIM input = new AlgoTeacherAssignmentIM();
        input.setClasses(inputClass);
        input.setTeachers(inputTeacher);
        input.setPreAssignments(preAssignment);

        TeacherClassAssignmentOM solution = teacherClassAssignmentAlgoService.computeTeacherClassAssignment(input);

        // remove all existing items of planId
        List<TeacherClassAssignmentSolution> teacherClassAssignmentSolutions = teacherClassAssignmentSolutionRepo.findAllByPlanId(
            planId);
        for (TeacherClassAssignmentSolution s : teacherClassAssignmentSolutions) {
            teacherClassAssignmentSolutionRepo.delete(s);
        }

        // store solution intoDB
        log.info("autoAssignTeacher2Class, START storing solution to DB");
        TeacherClassAssignmentModel[] assignments = solution.getAssignments();
        //for(TeacherClassAssignmentModel a: solution.getAssignments()){
        for (int i = 0; i < assignments.length; i++) {
            TeacherClassAssignmentModel a = assignments[i];
            if (a == null) {
                log.info("autoAssignTeacher2Class, found assignment " + i + " NULL");
                continue;
            }
            TeacherClassAssignmentSolution s = new TeacherClassAssignmentSolution();
            if (a.getAlgoClassIM() == null) {
                log.info("autoAssignTeacher2Class, found assignment class NULL " + a.getAlgoTeacherIM().getId());
                continue;
            }
            log.info("autoAssignTeacher2Class, assignment " + a.getAlgoClassIM().getCourseId() +
                     ", " + a.getAlgoClassIM().getId() + ", " + a.getAlgoClassIM().getClassCode());

            s.setClassId(a.getAlgoClassIM().getClassCode());
            s.setTeacherId(a.getAlgoTeacherIM().getId());
            s.setPlanId(planId);
            s.setCreatedStamp(new Date());
            s = teacherClassAssignmentSolutionRepo.save(s);
            log.info("autoAssignTeacher2Class, save solution " + s.getClassId() + " -> " + s.getTeacherId());
        }
        return false;
    }

    @Override
    public List<ClassTeacherAssignmentSolutionModel> getNotAssignedClassSolution(UUID planId) {
        List<EduCourse> eduCourses = eduCourseRepo.findAll();

        HashMap<String, EduCourse> mID2Course = new HashMap();
        for (EduCourse c : eduCourses) {
            mID2Course.put(c.getId(), c);
        }

        List<ClassTeacherAssignmentClassInfo> allClasses =
            classTeacherAssignmentClassInfoRepo.findAllByPlanId(planId);
        List<TeacherClassAssignmentSolution> teacherClassAssignmentSolutions =
            teacherClassAssignmentSolutionRepo.findAllByPlanId(planId);
        HashSet<String> assignedClassIds = new HashSet();
        for (TeacherClassAssignmentSolution s : teacherClassAssignmentSolutions) {
            assignedClassIds.add(s.getClassId());
        }

        List<ClassTeacherAssignmentSolutionModel> notAssignedClass = new ArrayList();
        for (ClassTeacherAssignmentClassInfo c : allClasses) {
            if (!assignedClassIds.contains(c.getClassId())) {
                ClassTeacherAssignmentSolutionModel ns = new ClassTeacherAssignmentSolutionModel();
                ns.setClassCode(c.getClassId());
                ns.setCourseId(c.getCourseId());
                ns.setTimetable(c.getLesson());
                EduCourse course = mID2Course.get(c.getCourseId());
                if (course != null) {
                    ns.setCourseName(course.getName());
                } else {
                    log.info("getNotAssignedClassSolution, courseId = " + c.getCourseId()
                             + " has not name???");
                }
                notAssignedClass.add(ns);
            }
        }

        return notAssignedClass;
    }

    @Override
    public List<SuggestedTeacherForClass> getSuggestedTeacherForClass(String classId) {
        List<ClassTeacherAssignmentClassInfo> cls = classTeacherAssignmentClassInfoRepo.findByClassId(classId);
        String courseId = null;
        for (ClassTeacherAssignmentClassInfo cti : cls) {
            courseId = cti.getCourseId();
            break;
        }
        List<SuggestedTeacherForClass> lst = new ArrayList<SuggestedTeacherForClass>();

        // TOBE improved
        List<TeacherCourse> teacherCourses = teacherCourseRepo.findAll();
        for (TeacherCourse tc : teacherCourses) {
            if (tc.getCourseId().equals(courseId)) {
                SuggestedTeacherForClass t = new SuggestedTeacherForClass();
                t.setTeacherId(tc.getTeacherId());
                t.setTeacherName(tc.getTeacherId());
                t.setHourLoad(0.0);// TOBE upgrade
                lst.add(t);
            }
        }


        return lst;
    }

    @Override
    public TeacherClassAssignmentSolution assignTeacherToClass(UserLogin u, AssignTeacherToClassInputModel input) {
        TeacherClassAssignmentSolution teacherClassAssignmentSolution = new TeacherClassAssignmentSolution();
        teacherClassAssignmentSolution.setTeacherId(input.getTeacherId());
        teacherClassAssignmentSolution.setClassId(input.getClassId());
        teacherClassAssignmentSolution.setPlanId(input.getPlanId());
        teacherClassAssignmentSolution.setCreatedByUserLoginId(u.getUserLoginId());
        teacherClassAssignmentSolution.setCreatedStamp(new Date());

        teacherClassAssignmentSolution = teacherClassAssignmentSolutionRepo.save(teacherClassAssignmentSolution);

        return teacherClassAssignmentSolution;
    }

    @Override
    public boolean removeClassTeacherAssignmentSolution(
        UserLogin u,
        RemoveClassTeacherAssignmentSolutionInputModel input
    ) {
        TeacherClassAssignmentSolution teacherClassAssignmentSolution
            = teacherClassAssignmentSolutionRepo.findById(input.getSolutionItemId()).orElse(null);
        if (teacherClassAssignmentSolution == null) {
            return false;
        } else {
            teacherClassAssignmentSolutionRepo.delete(teacherClassAssignmentSolution);
        }
        return true;
    }

    @Override
    public boolean removeClassTeacherAssignmentSolutionList(UUID planId, String solutionItemList) {
        String[] lst = solutionItemList.split(";");
        Gson gson = new Gson();
        for (String t : lst) {
            RemoveClassTeacherAssignmentSolutionInputModel cm = gson.fromJson(
                t,
                RemoveClassTeacherAssignmentSolutionInputModel.class);
            TeacherClassAssignmentSolution s = teacherClassAssignmentSolutionRepo
                .findById(cm.getSolutionItemId())
                .orElse(null);
            if (s != null) {
                teacherClassAssignmentSolutionRepo.delete(s);
                log.info("removeClassTeacherAssignmentSolutionList, delete " + s.getSolutionItemId());
            }
        }
        return true;
    }

    @Override
    public List<ClassesAssignedToATeacherModel> getClassesAssignedToATeacherSolution(UUID planId) {
        List<ClassesAssignedToATeacherModel> lst = new ArrayList();
        List<EduTeacher> teachers = eduTeacherRepo.findAll();
        HashMap<String, EduTeacher> mId2Teacher = new HashMap();
        for (EduTeacher t : teachers) {
            mId2Teacher.put(t.getTeacherId(), t);
        }

        HashMap<String, List> mTeacherId2Classes = new HashMap();
        List<ClassTeacherAssignmentSolutionModel> sol = getClassTeacherAssignmentSolution(planId);
        for (ClassTeacherAssignmentSolutionModel s : sol) {
            if (mTeacherId2Classes.get(s.getTeacherId()) == null) {
                mTeacherId2Classes.put(s.getTeacherId(), new ArrayList());
            }
            mTeacherId2Classes.get(s.getTeacherId()).add(s);
        }
        for (String teacherId : mTeacherId2Classes.keySet()) {
            ClassesAssignedToATeacherModel c = new ClassesAssignedToATeacherModel();
            c.setTeacherId(teacherId);
            String teacherName = "";
            if (mId2Teacher.get(teacherId) != null) {
                teacherName = mId2Teacher.get(teacherId).getTeacherName();
            }
            c.setTeacherName(teacherName);

            c.setClassList(mTeacherId2Classes.get(teacherId));
            c.setNumberOfClass(c.getClassList().size());
            double hourLoad = 0;
            for (ClassTeacherAssignmentSolutionModel i : c.getClassList()) {
                hourLoad += i.getHourLoad();
            }
            c.setHourLoad(hourLoad);

            lst.add(c);
        }
        return lst;
    }

    @Override
    public List<ClassTeacherAssignmentSolutionModel> getClassTeacherAssignmentSolution(UUID planId) {
        List<TeacherClassAssignmentSolution> teacherClassAssignmentSolutions =
            teacherClassAssignmentSolutionRepo.findAllByPlanId(planId);
        List<ClassTeacherAssignmentSolutionModel> m = new ArrayList();
        List<EduCourse> eduCourses = eduCourseRepo.findAll();
        List<EduTeacher> eduTeachers = eduTeacherRepo.findAll();
        List<ClassTeacherAssignmentClassInfo> classTeacherAssignmentClassInfos = classTeacherAssignmentClassInfoRepo.findAllByPlanId(
            planId);

        HashMap<String, EduCourse> mID2Course = new HashMap();
        HashMap<String, EduTeacher> mID2Teacher = new HashMap();
        HashMap<String, ClassTeacherAssignmentClassInfo> mClass2Info = new HashMap();
        for (ClassTeacherAssignmentClassInfo i : classTeacherAssignmentClassInfos) {
            mClass2Info.put(i.getClassId(), i);
        }
        for (EduCourse c : eduCourses) {
            mID2Course.put(c.getId(), c);
        }
        for (EduTeacher t : eduTeachers) {
            mID2Teacher.put(t.getTeacherId(), t);
        }
        List<ClassTeacherAssignmentSolutionModel> classTeacherAssignmentSolutionModels = new ArrayList<>();
        for (TeacherClassAssignmentSolution s : teacherClassAssignmentSolutions) {
            String classId = s.getClassId();
            ClassTeacherAssignmentClassInfo info = mClass2Info.get(classId);
            ClassTeacherAssignmentSolutionModel classTeacherAssignmentSolutionModel = new ClassTeacherAssignmentSolutionModel();

            classTeacherAssignmentSolutionModel.setSolutionItemId(s.getSolutionItemId());
            classTeacherAssignmentSolutionModel.setClassCode(s.getClassId());
            classTeacherAssignmentSolutionModel.setCourseId(info.getCourseId());
            EduCourse course = mID2Course.get(info.getCourseId());
            classTeacherAssignmentSolutionModel.setCourseName(course.getName());
            classTeacherAssignmentSolutionModel.setTeacherId(s.getTeacherId());
            EduTeacher teacher = mID2Teacher.get(s.getTeacherId());
            classTeacherAssignmentSolutionModel.setTeacherName(teacher.getTeacherName());
            classTeacherAssignmentSolutionModel.setTimetable(info.getLesson());
            classTeacherAssignmentSolutionModel.setHourLoad(info.getHourLoad());

            classTeacherAssignmentSolutionModels.add(classTeacherAssignmentSolutionModel);
        }
        return classTeacherAssignmentSolutionModels;
    }

    @Transactional
    @Override
    public boolean addTeacherCourseToAssignmentPlan(UUID planId, String teacherCourseList) {
        String[] lst = teacherCourseList.split(";");
        log.info("addTeacherCourseToAssignmentPlan, planId = " + planId + ", teacherCourseList = " + teacherCourseList);
        Gson gson = new Gson();
        if (lst != null && lst.length > 0) {
            for (int i = 0; i < lst.length; i++) {
                TeacherCoursePriority tcp = gson.fromJson(lst[i], TeacherCoursePriority.class);
                TeacherCourseForAssignmentPlan teacherCourseForAssignmentPlan = new TeacherCourseForAssignmentPlan();
                teacherCourseForAssignmentPlan.setCourseId(tcp.getCourseId());
                teacherCourseForAssignmentPlan.setTeacherId(tcp.getTeacherId());
                teacherCourseForAssignmentPlan.setPriority(tcp.getPriority());
                teacherCourseForAssignmentPlan.setPlanId(planId);

                teacherCourseForAssignmentPlan = teacherCourseForAssignmentPlanRepo
                    .save(teacherCourseForAssignmentPlan);
            }
        }
        return true;
    }

    @Transactional
    @Override
    public boolean removeTeacherCourseFromAssignmentPlan(UUID planId, String teacherCourseList) {
        String[] lst = teacherCourseList.split(";");
        log.info("removeTeacherCourseFromAssignmentPlan, planId = " +
                 planId +
                 ", teacherCourseList = " +
                 teacherCourseList);
        Gson gson = new Gson();
        if (lst != null && lst.length > 0) {
            for (int i = 0; i < lst.length; i++) {
                TeacherCoursePriority tcp = gson.fromJson(lst[i], TeacherCoursePriority.class);
                TeacherCourseForAssignmentPlan teacherCourseForAssignmentPlan
                    = teacherCourseForAssignmentPlanRepo.findByTeacherIdAndCourseIdAndPlanId(
                    tcp.getTeacherId(),
                    tcp.getCourseId(),
                    planId);

                if (teacherCourseForAssignmentPlan != null) {
                    teacherCourseForAssignmentPlanRepo
                        .delete(teacherCourseForAssignmentPlan);
                    log.info("removeTeacherCourseFromAssignmentPlan, remove (" +
                             tcp.getTeacherId() +
                             "," +
                             tcp.getCourseId() +
                             "," +
                             planId +
                             ")");

                }
            }
        }
        return true;
    }

    @Override
    public List<PairOfConflictTimetableClassModel> getPairOfConflictTimetableClass(UUID planId) {
        List<ClassTeacherAssignmentClassInfo> classes =
            classTeacherAssignmentClassInfoRepo.findAllByPlanId(planId);

        AlgoClassIM[] inputClass = new AlgoClassIM[classes.size()];

        for (int i = 0; i < classes.size(); i++) {
            ClassTeacherAssignmentClassInfo c = classes.get(i);
            AlgoClassIM ci = new AlgoClassIM();
            ci.setClassCode(c.getClassId());
            ci.setTimetable(c.getLesson());
            ci.setClassType(c.getClassType());
            ci.setCourseId(c.getCourseId());
            ci.setCourseName(c.getCourseId());
            ci.setHourLoad(1);// TOBE UPGRADED
            inputClass[i] = ci;
        }
        CheckConflict checker = new CheckConflict();

        List<PairOfConflictTimetableClassModel> lst = new ArrayList<>();
        for (int i = 0; i < inputClass.length; i++) {
            for (int j = i + 1; j < inputClass.length; j++) {
                //if(checker.isConflict(inputClass[i],inputClass[j])){
                ClassTeacherAssignmentClassInfo ci = classes.get(i);
                ClassTeacherAssignmentClassInfo cj = classes.get(j);
                if (TimetableConflictChecker.conflict(ci.getLesson(), cj.getLesson())) {
                    PairOfConflictTimetableClassModel p = new PairOfConflictTimetableClassModel();
                    p.setClassId1(ci.getClassId());
                    p.setCourseId1(ci.getCourseId());
                    p.setTimetable1(ci.getTimeTable());
                    p.setTimetableCode1(ci.getLesson());

                    p.setClassId2(cj.getClassId());
                    p.setCourseId2(cj.getCourseId());
                    p.setTimetable2(cj.getTimeTable());
                    p.setTimetableCode2(cj.getLesson());

                    lst.add(p);
                }
            }
        }

        return lst;
    }

    @Override
    public ClassTeacherAssignmentClassInfo updateClassForAssignment(
        UserLogin u,
        UpdateClassForAssignmentInputModel input
    ) {
        ClassTeacherAssignmentClassInfo classTeacherAssignmentClassInfo =
            classTeacherAssignmentClassInfoRepo.findById(input.getClassId()).orElse(null);
        if (classTeacherAssignmentClassInfo != null) {
            classTeacherAssignmentClassInfo.setHourLoad(input.getHourLoad());
            classTeacherAssignmentClassInfo = classTeacherAssignmentClassInfoRepo.save(classTeacherAssignmentClassInfo);
        }
        return classTeacherAssignmentClassInfo;
    }

    @Override
    public TeacherForAssignmentPlan updateTeacherForAssignment(
        UserLogin u,
        UpdateTeacherForAssignmentInputModel input
    ) {
        TeacherForAssignmentPlan teacherForAssignmentPlan =
            teacherForAssignmentPlanRepo.findByTeacherIdAndPlanId(input.getTeacherId(), input.getPlanId());
        if (teacherForAssignmentPlan != null) {
            teacherForAssignmentPlan.setMaxHourLoad(input.getHourLoad());
            teacherForAssignmentPlan = teacherForAssignmentPlanRepo.save(teacherForAssignmentPlan);
        }
        return teacherForAssignmentPlan;
    }
}
