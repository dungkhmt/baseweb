package com.hust.baseweb.applications.education.programsubmisson.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "contest_problem_test")
public class ContestProblemTest {
    @Id
    @Column(name = "problem_test_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID problemTestId;

    @Column(name="problem_id")
    private String problemId;

    @Column(name = "problem_test_filename")
    private String problemTestFilename;

    @Column(name = "problem_test_point")
    private int problemTestPoint;

}
