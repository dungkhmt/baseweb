package com.hust.baseweb.applications.education.quiztest.controller;

import com.hust.baseweb.applications.education.quiztest.entity.EduTestQuizGroupParticipationAssignment;
import com.hust.baseweb.applications.education.quiztest.model.quiztestgroupparticipant.AddParticipantToQuizTestGroupInputModel;
import com.hust.baseweb.applications.education.quiztest.model.quiztestgroupparticipant.RemoveParticipantToQuizTestGroupInputModel;
import com.hust.baseweb.applications.education.quiztest.service.EduTestQuizGroupParticipationAssignmentService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Log4j2
@RestController
@Validated
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EduTestQuizGroupParticipationAssignmentController {

    private EduTestQuizGroupParticipationAssignmentService eduTestQuizGroupParticipationAssignmentService;

    @PostMapping("/add-participant-to-quiz-test-group")
    public ResponseEntity<?> addParticipantToQuizTestGroup(
        Principal principal, @RequestBody
        AddParticipantToQuizTestGroupInputModel input
    ) {
        log.info("addParticipantToQuizTestGroup, groupId = " +
                 input.getQuizTestGroupId() +
                 " participantId = " +
                 input.getParticipantUserLoginId());

        // them ban ghi vao bang EduTestQuizGroupParticipationAssignment
        EduTestQuizGroupParticipationAssignment eduTestQuizGroupParticipationAssignment
            = eduTestQuizGroupParticipationAssignmentService.assignParticipant2QuizTestGroup(input);
        return ResponseEntity.ok().body(eduTestQuizGroupParticipationAssignment);
    }

    @PostMapping("/remove-participant-from-quiz-test-group")
    public ResponseEntity<?> removeParticipantFromQuizTestGroup(
        Principal principal, @RequestBody
        RemoveParticipantToQuizTestGroupInputModel input
    ) {
        log.info("removeParticipantFromQuizTestGroup, groupId = " +
                 input.getQuizTestGroupId() +
                 " participantId = " +
                 input.getParticipantUserLoginId());

        // them ban ghi vao bang EduTestQuizGroupParticipationAssignment
        boolean ok
            = eduTestQuizGroupParticipationAssignmentService.removeParticipantFromQuizTestGroup(input);

        return ResponseEntity.ok().body(ok);
    }

}
