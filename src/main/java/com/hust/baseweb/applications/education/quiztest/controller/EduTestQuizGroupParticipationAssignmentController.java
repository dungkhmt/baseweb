package com.hust.baseweb.applications.education.quiztest.controller;

import com.hust.baseweb.applications.education.quiztest.model.quiztestgroupparticipant.AddParticipantToQuizTestGroupInputModel;
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

@Log4j2
@Controller
@Validated
@AllArgsConstructor(onConstructor = @__(@Autowired))
//@CrossOrigin


public class EduTestQuizGroupParticipationAssignmentController {

    @PostMapping("/add-participant-to-quiz-test-group")
    public ResponseEntity<?> addParticipantToQuizTestGroup(Principal principal, @RequestBody
                                                           AddParticipantToQuizTestGroupInputModel input
                                                           ){
        // them ban ghi vao bang EduTestQuizGroupParticipationAssignment

        return ResponseEntity.ok().body("ok");
    }
}
