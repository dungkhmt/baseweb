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
import org.springframework.http.HttpStatus;
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
        String response = assignmentService.getSubmissionsOf(id.toString(), getFilesIM.getStudentIds());

        return null == response ? ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("File đang trong quá trình nén") : ResponseEntity.ok().body(response);
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

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok().body("OK");
    }
}
