package com.hust.baseweb.applications.education.programmingcontest.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "problem_source_code_new")
public class ProblemSourceCode {
    @Id
    @Column(name = "problem_source_code_id")
    private String problemSourceCodeId;

    @Column(name = "base_source")
    private String baseSource;

    @Column(name = "main_source")
    private String mainSource;

    @Column(name = "problem_function_default_source")
    private String problemFunctionDefaultSource;

    @Column(name = "problem_function_solution")
    private String problemFunctionSolution;

    @Column(name = "language")
    private String language;

//    @JoinTable(name = "contest_problem_problem_source_code",
//            joinColumns = @JoinColumn(name = "problem_source_code_id", referencedColumnName = "problem_source_code_id"),
//            inverseJoinColumns = @JoinColumn(name = "problem_id", referencedColumnName = "problem_id")
//    )
//    @ManyToOne(fetch = FetchType.LAZY)
//    private ContestProblem contestProblem;

    @JoinColumn(name = "contest_problem_id", referencedColumnName = "problem_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ContestProblemNew contestProblem;

    public String createSolutionSourceCode(){
        return this.getBaseSource() + "\n" + this.getProblemFunctionSolution() + "\n" + this.getMainSource();
    }
}
