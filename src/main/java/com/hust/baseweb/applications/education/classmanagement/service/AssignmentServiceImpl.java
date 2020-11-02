package com.hust.baseweb.applications.education.classmanagement.service;

import com.hust.baseweb.applications.education.classmanagement.service.storage.FileSystemStorageServiceImpl;
import com.hust.baseweb.applications.education.classmanagement.service.storage.StorageProperties;
import com.hust.baseweb.applications.education.entity.Assignment;
import com.hust.baseweb.applications.education.entity.EduClass;
import com.hust.baseweb.applications.education.exception.ResponseSecondType;
import com.hust.baseweb.applications.education.model.CreateAssignmentIM;
import com.hust.baseweb.applications.education.model.GetSubmissionsOM;
import com.hust.baseweb.applications.education.model.getassignmentdetail.GetAssignmentDetailOM;
import com.hust.baseweb.applications.education.model.getassignmentdetail4teacher.GetAssignmentDetail4TeacherOM;
import com.hust.baseweb.applications.education.model.getassignmentdetail4teacher.Submission;
import com.hust.baseweb.applications.education.repo.AssignmentRepo;
import com.hust.baseweb.applications.education.repo.AssignmentSubmissionRepo;
import com.hust.baseweb.applications.education.repo.ClassRepo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@Log4j2
@Service
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    private AssignmentRepo assignRepo;

    @Autowired
    private AssignmentSubmissionRepo submissionRepo;

    @Autowired
    private ClassRepo classRepo;

    @Autowired
    private FileSystemStorageServiceImpl storageService;

    private final String rootPath;

    @Autowired
    public AssignmentServiceImpl(StorageProperties properties) {
        rootPath = properties.getRootPath() + properties.getClassManagementDataPath();
    }

    @Override
    @Transactional(readOnly = true)
    public GetAssignmentDetailOM getAssignmentDetail(UUID id, String studentId) {
        return new GetAssignmentDetailOM(
            assignRepo.getAssignmentDetail(id),
            submissionRepo.getSubmitedFilenameOf(id, studentId));
    }

    @Override
    @Transactional(readOnly = true)
    public GetAssignmentDetail4TeacherOM getAssignmentDetail4Teacher(UUID assignmentId) {
        UUID classId = UUID.fromString(assignRepo.getClassIdOf(assignmentId));
        List<Submission> submissions = assignRepo.getStudentSubmissionsOf(assignmentId);
        int noOfStudents = classRepo.getNoStudentsOf(classId);

        String noSubmissions = noOfStudents == 0 ? "0/0" : submissions.size() +
                                                           "/" +
                                                           noOfStudents +
                                                           " (" +
                                                           100 * submissions.size() / noOfStudents +
                                                           "%)";

        return new GetAssignmentDetail4TeacherOM(
            assignRepo.getAssignmentDetail(assignmentId),
            submissions,
            noSubmissions);
    }

    @Override
    @Transactional(readOnly = true)
    public String getSubmissionsOf(String assignmentId, List<String> studentIds) {
        List<GetSubmissionsOM> submissions = assignRepo.getSubmissionsOf(
            UUID.fromString(assignmentId),
            new HashSet<>(studentIds));

        try {
            storageService.deleteIfExists(Paths.get(rootPath + assignmentId + "\\" + assignmentId + ".zip"));

            File outputZipFile = new File(rootPath + assignmentId + "\\" + assignmentId + ".zip");
            List<File> fileToAdd = new ArrayList<>();

            for (GetSubmissionsOM submission : submissions) {
                fileToAdd.add(new File(rootPath +
                                       assignmentId +
                                       "\\" +
                                       submission.getStudentId() +
                                       storageService.getFileExtension(submission.getOriginalFileName())));
            }

            storageService.zipFiles(outputZipFile, fileToAdd);
            return assignmentId + ".zip";
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public ResponseSecondType deleteAssignment(UUID id) {
        int isAssignExist = assignRepo.isAssignExist(id);

        if (0 == isAssignExist) {
            /*return new ResponseSecondType(
                404,
                "not exist",
                "Bài tập chưa được tạo hoặc đã bị xoá trước đó");*/
            return new ResponseSecondType(
                200,
                null,
                null);
        } else {
            Integer check = submissionRepo.checkSubmission(id);

            if (0 == check) {
                assignRepo.deleteAssignment(id);
                return new ResponseSecondType(200, null, null);
            } else {
                return new ResponseSecondType(
                    400,
                    "not allowed",
                    "Không thể xoá bài tập vì đã có sinh viên nộp bài");
            }
        }
    }

    @Override
    @Transactional
    public ResponseSecondType createAssignment(CreateAssignmentIM im) {
        // create folder for storing file

        // Save metadata.
        Date deadline = im.getDeadline();

        if (deadline.compareTo(new Date()) < 1) {
            return new ResponseSecondType(
                400,
                "require future date",
                "Vui lòng chọn thời điểm trong tương lai");
        }

        EduClass eduClass;

        if (1 == classRepo.isClassExist(im.getClassId())) {
            eduClass = new EduClass();
            eduClass.setId(im.getClassId());
        } else {
            return new ResponseSecondType(
                400,
                "class not exist",
                "Lớp chưa được tạo hoặc đã bị xoá trước đó");
        }

        Assignment assignment = new Assignment();

        assignment.setName(StringUtils.normalizeSpace(im.getName()));
        assignment.setSubject(im.getSubject());
        assignment.setDeadLine(deadline);
        assignment.setEduClass(eduClass);

        assignRepo.save(assignment);

        return new ResponseSecondType(200, null, null);
    }

    @Override
    public ResponseSecondType updateAssignment(UUID id, CreateAssignmentIM im) {
        Date deadline = im.getDeadline();

        if (deadline.compareTo(new Date()) < 1) {
            return new ResponseSecondType(
                400,
                "require future date",
                "Vui lòng chọn thời điểm trong tương lai");
        }

        Assignment assignment = assignRepo.findById(id).orElse(null);

        if (null == assignment) {
            return new ResponseSecondType(
                400,
                "not exist",
                "Bài tập chưa được tạo hoặc đã bị xoá trước đó");
        } else {
            assignment.setName(StringUtils.normalizeSpace(im.getName()));
            assignment.setSubject(im.getSubject());
            assignment.setDeadLine(deadline);

            assignRepo.save(assignment);

            return new ResponseSecondType(200, null, null);
        }
    }
}
