package com.hust.baseweb.applications.postsys.controller;

import com.hust.baseweb.applications.postsys.model.postsolve.PostOfficeVrpSolveInputModel;
import com.hust.baseweb.applications.postsys.service.SolverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SolverController {
    @Autowired SolverService solverService;

    @PostMapping("/post-office-vrp-solve")
    public ResponseEntity<?> postOfficeVrpSolve(@RequestBody PostOfficeVrpSolveInputModel postOfficeVrpSolveInputModel) {
        return ResponseEntity.ok().body(solverService.postOfficeVrpSolve(postOfficeVrpSolveInputModel));
    }
}
