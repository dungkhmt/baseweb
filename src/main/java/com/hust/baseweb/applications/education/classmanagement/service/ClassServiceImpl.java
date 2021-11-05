package com.hust.baseweb.applications.education.classmanagement.service;

import com.hust.baseweb.applications.education.classmanagement.enumeration.RegistStatus;
import com.hust.baseweb.applications.education.entity.*;
import com.hust.baseweb.applications.education.exception.SimpleResponse;
import com.hust.baseweb.applications.education.model.*;
import com.hust.baseweb.applications.education.model.educlassuserloginrole.AddEduClassUserLoginRoleIM;
import com.hust.baseweb.applications.education.model.educlassuserloginrole.ClassOfUserOM;
import com.hust.baseweb.applications.education.model.educlassuserloginrole.EduClassUserLoginRoleType;
import com.hust.baseweb.applications.education.model.getclasslist.ClassOM;
import com.hust.baseweb.applications.education.model.getclasslist.GetClassListOM;
import com.hust.baseweb.applications.education.repo.*;
import com.hust.baseweb.applications.notifications.entity.Notifications;
import com.hust.baseweb.applications.notifications.repo.NotificationsRepo;
import com.hust.baseweb.entity.UserLogin;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
public class ClassServiceImpl implements ClassService {

    private ClassRepo classRepo;

    private EduCourseRepo courseRepo;

    private SemesterRepo semesterRepo;

    private ClassRegistrationRepo registRepo;

    private EduDepartmentRepo eduDepartmentRepo;

    private EduClassUserLoginRoleRepo eduClassUserLoginRoleRepo;

    private NotificationsRepo notificationsRepo;


    @Override
    public EduClass findById(UUID id) {
        EduClass eduClass = classRepo.findById(id).orElse(null);
        return eduClass;
    }

    @Override
    @Transactional
    public EduClass save(UserLogin userLogin, AddClassModel addClassModel) {
        log.info("save start courseCode = " +
                 addClassModel.getCourseId() +
                 ", classCode = " +
                 addClassModel.getClassCode());

        EduClass aClass = new EduClass();
        //EduClass dupClass = classRepo.findFirstByCode(Integer.valueOf(addClassModel.getClassCode())).orElse(null);
        List<EduClass> dupClass = classRepo.findByClassCode(addClassModel.getClassCode());
        if (dupClass != null && dupClass.size() > 0) {
            aClass.setMessage("duplicate");
            return aClass;
        }

        Semester semester = semesterRepo.findById(Short.valueOf(addClassModel.getSemesterId()));

        log.info("save, got semester " + semester.getName() + ", id = " + semester.getId());
        EduDepartment department = eduDepartmentRepo.findById(addClassModel.getDepartmentId()).orElse(null);
        log.info("save got department " + department.getName());
        //UserLogin userLogin = userService.findById(addClassModel.getUserLoginId());
        log.info("save got user " + userLogin.getUserLoginId());

        EduCourse course = courseRepo.findById(addClassModel.getCourseId()).orElse(null);
        log.info("save got course " + course.getName());

        //aClass.setCode(Integer.valueOf(addClassModel.getClassCode()));
        aClass.setCode(0);
        aClass.setClassCode(addClassModel.getClassCode());

        log.info("save, finished setCode");

        aClass.setEduDepartment(department);
        log.info("save finished setDepartment");
        aClass.setTeacher(userLogin);
        log.info("save finished setTeacher");
        aClass.setEduCourse(course);
        log.info("save finished setCourse");
        aClass.setSemester(semester);
        aClass.setClassType(addClassModel.getClassType());

        //log.info("save before classRepo.save(), aClass.classCode = " + aClass.getCode() + ", courseCode = "
        //+ aClass.getEduCourse().getName() + ", semester = " + aClass.getSemester().getName() + ", department = "
        //+ aClass.getEduDepartment().getName());

        aClass = classRepo.save(aClass);
        log.info("save OK, aClass.id = " + aClass.getId());

        // create a corresponding role record in EduClassUserLoginRole
        EduClassUserLoginRole eduClassUserLoginRole = new EduClassUserLoginRole();
        eduClassUserLoginRole.setClassId(aClass.getId());
        eduClassUserLoginRole.setUserLoginId(userLogin.getUserLoginId());
        eduClassUserLoginRole.setRoleId(EduClassUserLoginRole.ROLE_OWNER);
        eduClassUserLoginRole = eduClassUserLoginRoleRepo.save(eduClassUserLoginRole);

        return aClass;
    }

    @Override
    @Transactional(readOnly = true)
    public GetClassListOM getClassesOfCurrentSemester(String studentId, GetClassesIM filterParams, Pageable pageable) {
        Semester semester = semesterRepo.findByActiveTrue();
        Page<ClassOM> classes;
        Set<String> registeredClasses = null;

        if (Stream.of(
            filterParams.getCourseId(),
            filterParams.getCourseName(),
            filterParams.getClassType(),
            filterParams.getDepartmentId())
                  .allMatch(attr -> StringUtils.isBlank(attr)) && null == filterParams.getCode()) {
            log.info("getClassesOfCurrentSemester -> call classRepo.findBySemester");
            classes = classRepo.findBySemester(semester.getId(), EduClass.STATUS_OPEN, pageable);
        } else {
            log.info("getClassesOfCurrentSemester -> call classRepo.classRepo.findBySemesterWithFilters");
            classes = classRepo.findBySemesterWithFilters(
                semester.getId(),
                null == filterParams.getCode() ? "" : filterParams.getCode().toString(),
                null == filterParams.getClassCode() ? "" : filterParams.getClassCode().toString(),
                null == filterParams.getCourseId() ? "" : StringUtils.deleteWhitespace(filterParams.getCourseId()),
                null == filterParams.getCourseName() ? "" : StringUtils.normalizeSpace(filterParams.getCourseName()),
                null == filterParams.getClassType() ? "" : StringUtils.deleteWhitespace(filterParams.getClassType()),
                null == filterParams.getDepartmentId()
                    ? ""
                    : StringUtils.deleteWhitespace(filterParams.getDepartmentId()),
                pageable);
        }

        if (0 < classes.getContent().size()) {
            registeredClasses = classRepo.getRegisteredClassesIn(
                studentId,
                classes
                    .get()
                    .map(aClass -> UUID.fromString(aClass.getId()))
                    .collect(Collectors.toList()));
        }

        return new GetClassListOM(semester.getId(), classes, registeredClasses);
    }

    @Override
    @Transactional
    public SimpleResponse register(UUID classId, String studentId) {
        SimpleResponse res;
        EduClass cls = classRepo.findById(classId).orElse(null);
        if(cls == null){
            res = new SimpleResponse(
                400,
                "class not exists",
                "Không tìm thấy lớp " + classId);
            return res;
        }
        String check = registRepo.checkRegistration(classId, studentId);

        if ("WAITING_FOR_APPROVAL".equals(check) || "APPROVED".equals(check)) {
            res = new SimpleResponse(
                400,
                "invalid register",
                "Bạn đã đăng ký lớp này rồi");
        } else {
            res = createOrUpdateRegist(classId, studentId, RegistStatus.WAITING_FOR_APPROVAL);

            // push notification to admin
            Notifications notifications = new Notifications();
            notifications.setStatusId(Notifications.STATUS_CREATED);
            notifications.setContent("SV " + studentId + " đăng ký vào lớp " + cls.getClassCode());
            notifications.setToUser("admin");// TOBE upgraded
            notifications.setFromUser(studentId);

            notifications  = notificationsRepo.save(notifications);

        }
        return res;
    }

    @Override
    @Transactional
    public Map<String, SimpleResponse> updateRegistStatus(
        UUID classId,
        Set<String> studentIds,
        RegistStatus status
    ) {
        Map<String, SimpleResponse> res = new HashMap<>();
        EduClass cls = classRepo.findById(classId).orElse(null);
        if(cls == null){
            res.put(classId.toString(), new SimpleResponse(
                404,
                "invalid update",
                "Không tìm thấy lớp"));
            return res;
        }
        for (String studentId : studentIds) {
            String check = registRepo.checkRegistration(classId, studentId);

            if (null == check) {
                res.put(studentId, new SimpleResponse(
                    404,
                    "invalid update",
                    "Không tìm thấy sinh viên, lớp hoặc yêu cầu đăng ký"));
            } else {
                switch (check) {
                    case "WAITING_FOR_APPROVAL":
                        // -> APPROVED || REFUSED
                        if (status.equals(RegistStatus.REMOVED)) {
                            res.put(studentId, invalidUpdateRes());
                        } else if (status.equals(RegistStatus.WAITING_FOR_APPROVAL)) {
                            res.put(studentId, new SimpleResponse(200, null, null));
                        } else {
                            res.put(studentId, createOrUpdateRegist(classId, studentId, status));
                        }
                        Notifications notifications = new Notifications();
                        notifications.setToUser(studentId);
                        notifications.setContent("Bạn vừa được phê duyệt tham gia lớp " + cls.getClassCode());
                        notifications.setStatusId(Notifications.STATUS_CREATED);
                        notifications = notificationsRepo.save(notifications);

                        break;
                    case "APPROVED":
                        // -> REMOVED.
                        if (status.equals(RegistStatus.REMOVED)) {
                            res.put(studentId, createOrUpdateRegist(classId, studentId, status));
                        } else if (status.equals(RegistStatus.APPROVED)) {
                            res.put(studentId, new SimpleResponse(200, null, null));
                        } else {
                            res.put(studentId, invalidUpdateRes());
                        }

                        break;
                    case "REFUSED":
                        // -> WAITING_FOR_APPROVAL.
                        if (status.equals(RegistStatus.WAITING_FOR_APPROVAL)) {
                            res.put(studentId, createOrUpdateRegist(classId, studentId, status));
                        } else if (status.equals(RegistStatus.REFUSED)) {
                            res.put(studentId, new SimpleResponse(200, null, null));
                        } else {
                            res.put(studentId, invalidUpdateRes());
                        }
                        break;
                    case "REMOVED":
                        // -> WAITING_FOR_APPROVAL.
                        if (status.equals(RegistStatus.WAITING_FOR_APPROVAL)) {
                            res.put(studentId, createOrUpdateRegist(classId, studentId, status));
                        } else if (status.equals(RegistStatus.REMOVED)) {
                            res.put(studentId, new SimpleResponse(200, null, null));
                        } else {
                            res.put(studentId, invalidUpdateRes());
                        }
                        break;
                }
            }
        }
        return res;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetClassesOfTeacherOM> getClassesOfTeacher(String teacherId) {
        return classRepo.getClassesOfTeacher(teacherId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetClassesOfStudentOM> getClassesOfStudent(String studentId) {
        return classRepo.getClassesDetailOf(
            studentId,
            Arrays.asList(
                RegistStatus.APPROVED.toString(),
                RegistStatus.WAITING_FOR_APPROVAL.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public GetClassDetailOM getClassDetail(UUID id) {
        return classRepo.getDetailOf(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetAssigns4TeacherOM> getAssign4Teacher(UUID classId) {
        return classRepo.getAssignments4Teacher(classId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetAllStuAssignDetail4Teacher> getAllStuAssign4Teacher(UUID classId) {
        List<GetAllStuAssignDetail4Teacher> getAllStuAssignDetail4TeacherList = new ArrayList<>();
        List<GetAllStuAssigns4TeacherOM> getAllStuAssigns4TeacherList = classRepo.getAllStudentAssignments4Teacher(
            classId);
        String tempStudentId = "";
        String tempAssignmentId = "";
        for (GetAllStuAssigns4TeacherOM getAllStuAssigns4TeacherOM : getAllStuAssigns4TeacherList) {
            if (getAllStuAssigns4TeacherOM.getId().equals(tempStudentId)) {
                if (getAllStuAssigns4TeacherOM.getAssignmentId().equals(tempAssignmentId)) {
                } else {
                    tempAssignmentId = getAllStuAssigns4TeacherOM.getAssignmentId();
                    getAllStuAssignDetail4TeacherList
                        .get(getAllStuAssignDetail4TeacherList.size() - 1)
                        .addAssignment(getAllStuAssigns4TeacherOM);
                }
            } else {
                tempStudentId = getAllStuAssigns4TeacherOM.getId();
                tempAssignmentId = getAllStuAssigns4TeacherOM.getAssignmentId();
                GetAllStuAssignDetail4Teacher getAllStuAssignDetail4Teacher = new GetAllStuAssignDetail4Teacher(
                    getAllStuAssigns4TeacherOM);
                getAllStuAssignDetail4TeacherList.add(getAllStuAssignDetail4Teacher);
            }
        }
        return getAllStuAssignDetail4TeacherList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssignmentSubmission> getAssignSubmit4Teacher(UUID classId) {
        return classRepo.getAssignmentsSubmission4Teacher(classId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetAssigns4StudentOM> getAssign4Student(UUID classId) {
        return classRepo.getAssignments4Student(classId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetStudentsOfClassOM> getStudentsOfClass(UUID id) {
        return classRepo.getStudentsOfClass(id, RegistStatus.APPROVED.toString());
    }

    @Override
    public List<GetStudentsOfClassOM> getRegistStudentsOfClass(UUID id) {
        return classRepo.getStudentsOfClass(id, RegistStatus.WAITING_FOR_APPROVAL.toString());
    }

    @Override
    public EduClassUserLoginRole addEduClassUserLoginRole(AddEduClassUserLoginRoleIM input) {
        EduClassUserLoginRole eduClassUserLoginRole = new EduClassUserLoginRole();
        eduClassUserLoginRole.setClassId(input.getClassId());
        eduClassUserLoginRole.setUserLoginId(input.getUserLoginId());
        eduClassUserLoginRole.setRoleId(input.getRoleId());
        eduClassUserLoginRole.setFromDate(new Date());

        eduClassUserLoginRole = eduClassUserLoginRoleRepo.save(eduClassUserLoginRole);

        return eduClassUserLoginRole;
    }

    @Override
    public List<ClassOfUserOM> getClassOfUser(String userLoginId){
        //Page<EduClassUserLoginRole> lstRoles = eduClassUserLoginRoleRepo
        //    .findAllByUserLoginIdAndThruDate(userLoginId, null, pageable);
        //int totalCount = eduClassUserLoginRoleRepo.totalCountByUserLoginIdAndThruDate(userLoginId,null);
        List<EduClassUserLoginRole> lstRoles = eduClassUserLoginRoleRepo.findAllByUserLoginIdAndThruDate(userLoginId,null);

        List<ClassOfUserOM> lst = new ArrayList();
        for(EduClassUserLoginRole er: lstRoles){
            EduClass eduClass = classRepo.findById(er.getClassId()).orElse(null);

            if(eduClass != null) {
                ClassOfUserOM e = new ClassOfUserOM();
                e.setClassId(eduClass.getId());
                e.setClassCode(eduClass.getClassCode());
                e.setCourseId(eduClass.getEduCourse().getId());
                e.setCourseName(eduClass.getEduCourse().getName());
                e.setStatusId(eduClass.getStatusId());
                e.setSemester(eduClass.getSemester().getId()+"");
                e.setCreatedByUserLoginId(eduClass.getTeacher().getUserLoginId());
                lst.add(e);
            }
        }
        //Page<EduClass> page = new PageImpl<>(lst,totalCount,pageable);
        return lst;
    }
    @Transactional
    private SimpleResponse createOrUpdateRegist(UUID classId, String studentId, RegistStatus status) {
        EduClass eduClass = new EduClass();
        UserLogin student = new UserLogin();
        ClassRegistrationId id = new ClassRegistrationId(eduClass, student);
        ClassRegistration registration = new ClassRegistration();

        student.setUserLoginId(studentId);
        eduClass.setId(classId);
        registration.setId(id);
        registration.setStatus(status);

        registRepo.save(registration);
        return new SimpleResponse(200, null, null);
    }

    private SimpleResponse invalidUpdateRes() {
        return new SimpleResponse(
            400,
            "invalid update",
            "Trạng thái mới không phù hợp");
    }
}
