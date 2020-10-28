package com.hust.baseweb.applications.education.classmanagement.service;

import com.hust.baseweb.applications.education.classmanagement.enumeration.RegistStatus;
import com.hust.baseweb.applications.education.entity.ClassRegistration;
import com.hust.baseweb.applications.education.entity.ClassRegistrationId;
import com.hust.baseweb.applications.education.entity.EduClass;
import com.hust.baseweb.applications.education.entity.Semester;
import com.hust.baseweb.applications.education.exception.ResponseSecondType;
import com.hust.baseweb.applications.education.model.*;
import com.hust.baseweb.applications.education.model.getclasslist.ClassOM;
import com.hust.baseweb.applications.education.model.getclasslist.GetClassListOM;
import com.hust.baseweb.applications.education.repo.ClassRegistrationRepo;
import com.hust.baseweb.applications.education.repo.ClassRepo;
import com.hust.baseweb.applications.education.repo.SemesterRepo;
import com.hust.baseweb.entity.UserLogin;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ClassServiceImpl implements ClassService {

    private ClassRepo classRepo;

    private SemesterRepo semesterRepo;

    private ClassRegistrationRepo registRepo;

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
            classes = classRepo.findBySemester(semester.getId(), pageable);
        } else {
            classes = classRepo.findBySemesterWithFilters(
                semester.getId(),
                null == filterParams.getCode() ? "" : filterParams.getCode().toString(),
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
    public ResponseSecondType register(UUID classId, String studentId) {
        ResponseSecondType res;
        String check = registRepo.checkRegistration(classId, studentId);

        if ("WAITING_FOR_APPROVAL".equals(check) || "APPROVED".equals(check)) {
            res = new ResponseSecondType(
                400,
                "invalid register",
                "Bạn đã đăng ký lớp này rồi");
        } else {
            res = createOrUpdateRegist(classId, studentId, RegistStatus.WAITING_FOR_APPROVAL);
        }
        return res;
    }

    @Override
    @Transactional
    public Map<String, ResponseSecondType> updateRegistStatus(
        UUID classId,
        Set<String> studentIds,
        RegistStatus status
    ) {
        Map<String, ResponseSecondType> res = new HashMap<>();

        for (String studentId : studentIds) {
            String check = registRepo.checkRegistration(classId, studentId);

            if (null == check) {
                res.put(studentId, new ResponseSecondType(
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
                            res.put(studentId, new ResponseSecondType(200, null, null));
                        } else {
                            res.put(studentId, createOrUpdateRegist(classId, studentId, status));
                        }
                        break;
                    case "APPROVED":
                        // -> REMOVED.
                        if (status.equals(RegistStatus.REMOVED)) {
                            res.put(studentId, createOrUpdateRegist(classId, studentId, status));
                        } else if (status.equals(RegistStatus.APPROVED)) {
                            res.put(studentId, new ResponseSecondType(200, null, null));
                        } else {
                            res.put(studentId, invalidUpdateRes());
                        }
                        break;
                    case "REFUSED":
                        // -> WAITING_FOR_APPROVAL.
                        if (status.equals(RegistStatus.WAITING_FOR_APPROVAL)) {
                            res.put(studentId, createOrUpdateRegist(classId, studentId, status));
                        } else if (status.equals(RegistStatus.REFUSED)) {
                            res.put(studentId, new ResponseSecondType(200, null, null));
                        } else {
                            res.put(studentId, invalidUpdateRes());
                        }
                        break;
                    case "REMOVED":
                        // -> WAITING_FOR_APPROVAL.
                        if (status.equals(RegistStatus.WAITING_FOR_APPROVAL)) {
                            res.put(studentId, createOrUpdateRegist(classId, studentId, status));
                        } else if (status.equals(RegistStatus.REMOVED)) {
                            res.put(studentId, new ResponseSecondType(200, null, null));
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
    public List<GetAssignmentsOM> getAssignments(UUID classId) {
        return classRepo.getAssignments(classId);
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

    @Transactional
    private ResponseSecondType createOrUpdateRegist(UUID classId, String studentId, RegistStatus status) {
        EduClass eduClass = new EduClass();
        UserLogin student = new UserLogin();
        ClassRegistrationId id = new ClassRegistrationId(eduClass, student);
        ClassRegistration registration = new ClassRegistration();

        student.setUserLoginId(studentId);
        eduClass.setId(classId);
        registration.setId(id);
        registration.setStatus(status);

        registRepo.save(registration);
        return new ResponseSecondType(200, null, null);
    }

    private ResponseSecondType invalidUpdateRes() {
        return new ResponseSecondType(
            400,
            "invalid update",
            "Trạng thái mới không phù hợp");
    }
}
