package com.hust.baseweb.applications.education.programmingcontest.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "contest_problem_new")
public class ContestProblemNew {
    @Id
    @Column(name = "problem_id")
    private String problemId;

//    @JoinTable(name = "contest_problem_problem_source_code",
//        joinColumns = @JoinColumn(name = "problem_id", referencedColumnName = "problem_id"),
//        inverseJoinColumns = @JoinColumn(name = "problem_source_code_id", referencedColumnName = "problem_source_code_id")
//    )
//    @OneToMany(fetch = FetchType.LAZY)
//    private List<ProblemSourceCode> problemSourceCode;

    @OneToMany(mappedBy = "contestProblem")
    private Set<ProblemSourceCode> problemSourceCodes;

    @Column(name = "problem_name", unique = true)
    private String problemName;

    @Column(name = "problem_description")
    private String problemDescription;

//    @OneToOne
//    @JoinColumn(name = "created_by_user_login_id", referencedColumnName = "user_login_id")
//    private UserLogin userLogin;

    @Column(name = "time_limit")
    private int timeLimit;

    @Column(name = "memory_limit")
    private int memoryLimit;

    @Column(name = "level_id")
    private String levelId;

    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "correct_solution_source_code")
    private String correctSolutionSourceCode;

    @Column(name = "correct_solution_language")
    private String correctSolutionLanguage;

    @Column(name = "solution")
    private String solution;

//    @OneToMany(mappedBy = "contestProblem")
//    private Set<TestCase> testCases;
//    @JoinTable(name = "contest_problem_test_case",
//            joinColumns = @JoinColumn(name = "problem_id", referencedColumnName = "problem_id"),
//            inverseJoinColumns = @JoinColumn(name = "test_case_id", referencedColumnName = "test_case_id")
//    )
//    @OneToMany(fetch = FetchType.LAZY)
//    private List<TestCase> testCases;

}
