package com.hust.baseweb.applications.education.programsubmisson.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "contest_problem")
public class ContestProblem {

    public static final String CONTEST_PROBLEM_LEVEL_BEGINNER = "BEGINNER";
    public static final String CONTEST_PROBLEM_LEVEL_INTERMEDIATE = "INTERMEDIATE";
    public static final String CONTEST_PROBLEM_LEVEL_ADVANCED = "ADVANCED";
    public static final String CONTEST_PROBLEM_LEVEL_PROFESSIONAL = "PROFESSIONAL";


    public static final String CONTEST_PROBLEM_CATEGORY_NAIVE = "NAIVE";
    public static final String CONTEST_PROBLEM_CATEGORY_DIRECT = "DIRECT";
    public static final String CONTEST_PROBLEM_CATEGORY_EXHAUSTIVE = "EXHAUSTIVE";
    public static final String CONTEST_PROBLEM_CATEGORY_DYNAMICPROGRAMMING = "DYNAMICPROGRAMMING";
    public static final String CONTEST_PROBLEM_CATEGORY_GREEDY = "GREEDY";
    public static final String CONTEST_PROBLEM_CATEGORY_DIVIDE_AND_CONQUER = "DIVIDE_AND_CONQUER";
    public static final String CONTEST_PROBLEM_CATEGORY_GRAPHS = "GRAPHS";
    public static final String CONTEST_PROBLEM_CATEGORY_BFS = "BFS";
    public static final String CONTEST_PROBLEM_CATEGORY_DFS = "DFS";


    @Id
    @Column(name = "problem_id")
    private String problemId;

    @Column(name = "problem_name")
    private String problemName;

    @Column(name = "problem_statement")
    private String problemStatement;

    @Column(name = "time_limit")
    private int timeLimit;

    @Column(name = "level_id")
    private String levelId;

    @Column(name = "category_id")
    private String categoryId;

}
