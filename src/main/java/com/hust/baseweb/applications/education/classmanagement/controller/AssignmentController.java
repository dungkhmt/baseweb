package com.hust.baseweb.applications.education.classmanagement.controller;

import com.hust.baseweb.applications.education.classmanagement.service.AssignmentServiceImpl;
import com.hust.baseweb.applications.education.classmanagement.service.storage.FileSystemStorageServiceImpl;
import com.hust.baseweb.applications.education.classmanagement.service.storage.exception.StorageFileNotFoundException;
import com.hust.baseweb.applications.education.model.GetFilesIM;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AssignmentController {

    private FileSystemStorageServiceImpl storageService;

    private AssignmentServiceImpl assignmentService;

    @PostMapping("/{id}/submission")
    public ResponseEntity<?> submitAssignment(
        Principal principal,
        @PathVariable UUID id,
        @RequestParam("file") MultipartFile file
    ) {
        storageService.store(file, id, principal.getName());

        return ResponseEntity.ok().body("Tải lên thành công tệp '" + file.getOriginalFilename() + "'");
    }

    /**
     * Return a password-protected zip file contains the submitted files of students with ids in {@link GetFilesIM}.
     * If the submissions for all students with ids in {@link GetFilesIM} not found, the zip file will be empty
     *
     * @param principal
     * @param id         assignmentId
     * @param getFilesIM contains list of student ids
     * @param response   a zip file.
     */
    @PostMapping("/{id}/submission/files")
    public void getFiles(
        Principal principal,
        @PathVariable UUID id,
        @Valid @RequestBody GetFilesIM getFilesIM,
        HttpServletResponse response
    ) {
        InputStream is = assignmentService.getFiles(id.toString(), getFilesIM.getStudentIds());

        response.setHeader(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=" + id.toString() + ".zip");
        response.setContentType(MediaType.parseMediaType("application/zip").toString());

        try {
            IOUtils.copyLarge(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/{id}/student")
    public ResponseEntity<?> getAssignmentDetail(Principal principal, @PathVariable UUID id) {
        return ResponseEntity.ok().body(assignmentService.getAssignmentDetail(id, principal.getName()));
    }

    @GetMapping("/{id}/teacher")
    public ResponseEntity<?> getAssignmentDetail4Teacher(@PathVariable UUID id) {
        return ResponseEntity.ok().body(assignmentService.getAssignmentDetail4Teacher(id));
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
