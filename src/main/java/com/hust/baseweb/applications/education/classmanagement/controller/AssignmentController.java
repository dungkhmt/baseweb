package com.hust.baseweb.applications.education.classmanagement.controller;

import com.hust.baseweb.applications.education.classmanagement.service.AssignmentServiceImpl;
import com.hust.baseweb.applications.education.classmanagement.service.storage.FileSystemStorageServiceImpl;
import com.hust.baseweb.applications.education.classmanagement.service.storage.exception.StorageException;
import com.hust.baseweb.applications.education.exception.SimpleResponse;
import com.hust.baseweb.applications.education.model.CreateAssignmentIM;
import com.hust.baseweb.applications.education.model.GetFilesIM;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.UUID;

@Log4j2
@Controller
@RequestMapping("/edu/assignment")
@AllArgsConstructor(onConstructor_ = @Autowired)
public class AssignmentController {

    private FileSystemStorageServiceImpl storageService;

    private AssignmentServiceImpl assignService;

    @PostMapping("/{id}/submission")
    public ResponseEntity<?> submitAssign(
        Principal principal,
        @PathVariable UUID id,
        @RequestParam("file") MultipartFile file
    ) {
        SimpleResponse res;

        try {
            res = assignService.saveSubmission(principal.getName(), id, file);
        } catch (JpaSystemException e) {
            if ("fk_assignment_submission_assignment"
                .equals(e.getRootCause().getMessage().substring(94, 129))) {
                res = new SimpleResponse(
                    400,
                    "not exist",
                    "Bài tập không tồn tại");
            } else {
                res = new SimpleResponse(
                    500,
                    "unknown",
                    null);
            }
        }

        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/{id}/download-file/{filename:.+}")
    @ResponseBody
    public void downloadFile(@PathVariable UUID id, @PathVariable String filename, HttpServletResponse response) {
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + filename + "\"");
        response.setContentType("application/octet-stream");

        try (InputStream is = storageService.loadFileAsResource(filename, id.toString())) {
            IOUtils.copyLarge(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/{id}/submissions")
    public ResponseEntity<String> getSubmissionsOf(
        Principal principal,
        @PathVariable UUID id,
        @Valid @RequestBody GetFilesIM getFilesIM
    ) {
        String response = assignService.getSubmissionsOf(id.toString(), getFilesIM.getStudentIds());

        return null == response ? ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("File đang trong quá trình nén") : ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}/student")
    public ResponseEntity<?> getAssignDetail(Principal principal, @PathVariable UUID id) {
        return ResponseEntity.ok().body(assignService.getAssignmentDetail(id, principal.getName()));
    }

    @GetMapping("/{id}/teacher")
    public ResponseEntity<?> getAssignDetail4Teacher(@PathVariable UUID id) {
        return ResponseEntity.ok().body(assignService.getAssignmentDetail4Teacher(id));
    }

    // CRUD.
    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @PostMapping
    public ResponseEntity<?> createAssign(@RequestBody CreateAssignmentIM im) {
        SimpleResponse res = assignService.createAssignment(im);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAssign(@PathVariable UUID id, @RequestBody CreateAssignmentIM im) {
        SimpleResponse res = assignService.updateAssignment(id, im);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAssign(@PathVariable UUID id) {
        SimpleResponse res;

        try {
            res = assignService.deleteAssignment(id);
        } catch (DataIntegrityViolationException e) {
            res = new SimpleResponse(
                400,
                "not allowed",
                "Không thể xoá bài tập vì đã có sinh viên nộp bài");
        }

        return ResponseEntity.status(res.getStatus()).body(res);
    }

    // Handle exception.
    @ExceptionHandler(StorageException.class)
    public ResponseEntity<?> handleStorageException(StorageException e) {
        SimpleResponse res = new SimpleResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Failed to store file",
            e.getMessage());

        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
