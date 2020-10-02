package com.hust.baseweb.applications.education.classmanagement.service;

import com.hust.baseweb.applications.education.classmanagement.enumeration.RegistStatus;
import com.hust.baseweb.applications.education.entity.Class;
import com.hust.baseweb.applications.education.entity.ClassRegistration;
import com.hust.baseweb.applications.education.entity.ClassRegistrationId;
import com.hust.baseweb.applications.education.entity.Semester;
import com.hust.baseweb.applications.education.exception.ResponseSecondType;
import com.hust.baseweb.applications.education.model.GetListStudentsOfClassOM;
import com.hust.baseweb.applications.education.model.getclasslist.ClassOM;
import com.hust.baseweb.applications.education.model.getclasslist.GetClassListOM;
import com.hust.baseweb.applications.education.repo.ClassRegistrationRepo;
import com.hust.baseweb.applications.education.repo.ClassRepo;
import com.hust.baseweb.applications.education.repo.SemesterRepo;
import com.hust.baseweb.entity.UserLogin;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ClassServiceImpl implements ClassService {

    private ClassRepo classRepo;

    private SemesterRepo semesterRepo;

    private ClassRegistrationRepo registRepo;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> getClassListOfCurrentSemester(int page, int size) {
        Semester semester = semesterRepo.findByActiveTrue();
        Page<ClassOM> classes = classRepo.findBySemesterId(semester.getId(), PageRequest.of(page, size));

        return ResponseEntity.ok().body(new GetClassListOM(semester.getId(), classes));
    }

    @Override
    @Transactional
    public ResponseSecondType register(UUID classId, String studentId) {
        ResponseSecondType res;
        String check = registRepo.checkRegiastration(classId, studentId);

        if ("WAITING_FOR_APPROVAL".equals(check) || "APPROVED".equals(check)) {
            res = new ResponseSecondType(
                400,
                "invalid register",
                "Bạn đã đăng ký lớp này rồi");
            return res;
        } else {
            return createOrUpdateRegist(classId, studentId, RegistStatus.WAITING_FOR_APPROVAL);
        }
    }

    @Override
    public GetListStudentsOfClassOM getListStudentsOfClass(UUID id) {
        return null;
    }

    @Override
    @Transactional
    public ResponseSecondType updateRegistStatus(UUID classId, String studentId, RegistStatus status) {
        ResponseSecondType res = null;
        String check = registRepo.checkRegiastration(classId, studentId);

        if (null == check) {
            res = new ResponseSecondType(
                404,
                "invalid update",
                "Không tìm thấy sinh viên, lớp hoặc yêu cầu đăng ký");
        } else {
            switch (check) {
                case "WAITING_FOR_APPROVAL":
                    // -> APPROVED || REFUSED
                    if (status.equals(RegistStatus.REMOVED)) {
                        res = invalidUpdateRes();
                    } else if (status.equals(RegistStatus.WAITING_FOR_APPROVAL)) {
                        res = new ResponseSecondType(200, null, null);
                    } else {
                        res = createOrUpdateRegist(classId, studentId, status);
                    }
                    break;
                case "APPROVED":
                    // -> REMOVED.
                    if (status.equals(RegistStatus.REMOVED)) {
                        res = createOrUpdateRegist(classId, studentId, status);
                    } else if (status.equals(RegistStatus.APPROVED)) {
                        res = new ResponseSecondType(200, null, null);
                    } else {
                        res = invalidUpdateRes();
                    }
                    break;
                case "REFUSED":
                    // -> WAITING_FOR_APPROVAL.
                    if (status.equals(RegistStatus.WAITING_FOR_APPROVAL)) {
                        res = createOrUpdateRegist(classId, studentId, status);
                    } else if (status.equals(RegistStatus.REFUSED)) {
                        res = new ResponseSecondType(200, null, null);
                    } else {
                        res = invalidUpdateRes();
                    }
                    break;
                case "REMOVED":
                    // -> WAITING_FOR_APPROVAL.
                    if (status.equals(RegistStatus.WAITING_FOR_APPROVAL)) {
                        res = createOrUpdateRegist(classId, studentId, status);
                    } else if (status.equals(RegistStatus.REMOVED)) {
                        res = new ResponseSecondType(200, null, null);
                    } else {
                        res = invalidUpdateRes();
                    }
                    break;
            }
        }
        return res;
    }

    @Transactional
    private ResponseSecondType createOrUpdateRegist(UUID classId, String studentId, RegistStatus status) {
        Class aClass = new Class();
        UserLogin student = new UserLogin();
        ClassRegistrationId id = new ClassRegistrationId(aClass, student);
        ClassRegistration registration = new ClassRegistration();

        student.setUserLoginId(studentId);
        aClass.setId(classId);
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
