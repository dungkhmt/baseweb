package com.hust.baseweb.applications.education.quiztest.controller;

import com.hust.baseweb.applications.education.quiztest.entity.EduTestQuizGroup;
import com.hust.baseweb.applications.education.quiztest.model.quiztestgroup.GenerateQuizTestGroupInputModel;
import com.hust.baseweb.applications.education.quiztest.service.EduQuizTestGroupService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;

@Log4j2
@Controller
@Validated
@AllArgsConstructor(onConstructor = @__(@Autowired))
@CrossOrigin
public class EduQuizTestGroupController {
    private EduQuizTestGroupService eduQuizTestGroupService;

    @PostMapping("/generate-quiz-test-group")
    public ResponseEntity<?> generateQuizTestGroup(Principal principal, @RequestBody
        GenerateQuizTestGroupInputModel input){
        List<EduTestQuizGroup> eduTestQuizGroups = eduQuizTestGroupService.generateQuizTestGroups(input);

        return ResponseEntity.ok().body(eduTestQuizGroups);
    }
}
