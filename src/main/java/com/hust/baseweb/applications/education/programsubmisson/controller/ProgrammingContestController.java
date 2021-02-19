package com.hust.baseweb.applications.education.programsubmisson.controller;

import com.hust.baseweb.applications.education.programsubmisson.entity.ProgrammingContest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor_ = @Autowired)
@Log4j2
public class ProgrammingContestController {
    @GetMapping("get-programming-contest-types")
    public ResponseEntity<?> getProgrammingContestTypes(){
        List<String> types = new ArrayList();
        types.add(ProgrammingContest.CONTEST_TYPE_PARTICIPANT_IDENTICAL);
        types.add(ProgrammingContest.CONTEST_TYPE_PARTICIPANT_SPECIFIC);
        return ResponseEntity.ok().body(types);
    }
}
