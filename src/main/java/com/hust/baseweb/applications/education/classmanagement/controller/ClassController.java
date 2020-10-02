package com.hust.baseweb.applications.education.classmanagement.controller;

import com.hust.baseweb.applications.education.classmanagement.service.ClassServiceImpl;
import com.hust.baseweb.applications.education.exception.ResponseSecondType;
import com.hust.baseweb.applications.education.model.GetListStudentsOfClassOM;
import com.hust.baseweb.applications.education.model.RegistIM;
import com.hust.baseweb.applications.education.model.UpdateRegistStatusIM;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.UUID;

@Controller
@RequestMapping("/edu/class")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ClassController {

    private ClassServiceImpl classService;

    @GetMapping
    public ResponseEntity<?> getClassListOfCurrentSemester(
        @RequestParam
        @Min(value = 0, message = "Số trang có giá trị không âm") Integer page,
        @RequestParam
        @Min(value = 0, message = "Kích thước trang có giá trị không âm") Integer size
    ) {
        if (null == page) {
            page = 0;
        }

        if (null == size) {
            size = 20;
        }

        return classService.getClassListOfCurrentSemester(page, size);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistIM im) {
        ResponseSecondType res = classService.register(im.getClassId(), im.getStudentId());
        return ResponseEntity.status(res.getStatus()).body(res.getMessage());
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<GetListStudentsOfClassOM> getListStuOfClass(@PathVariable UUID id) {
        return null;
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/{id}/registered-students")
    public ResponseEntity<GetListStudentsOfClassOM> getListRegistStuOfClass(@PathVariable UUID id) {
        return null;
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/registration-status")
    public ResponseEntity<String> updateRegisStatus(@Valid @RequestBody UpdateRegistStatusIM im) {
        ResponseSecondType res = classService.updateRegistStatus(im.getClassId(), im.getStudentId(), im.getStatus());
        return ResponseEntity.status(res.getStatus()).body(res.getMessage());
    }
}
