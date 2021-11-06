package com.hust.baseweb.applications.education.programmingcontest.entity;

import com.hust.baseweb.entity.UserLogin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "contest_new")
public class Contest {
    @Id
    @Column(name = "contest_id")
    private String contestId;

    @Column(name = "contest_name")
    private String contestName;

    @OneToOne
    @JoinColumn(name = "user_create_id", referencedColumnName = "user_login_id")
    private UserLogin userLogin;

    @Column(name = "contest_solving_time")
    private int contestSolvingTime;

    @JoinTable(
            name = "contest_contest_problem",
            joinColumns = @JoinColumn(name = "contest_id", referencedColumnName = "contest_id"),
            inverseJoinColumns = @JoinColumn(name = "problem_id", referencedColumnName = "problem_id")
    )
    @OneToMany(fetch = FetchType.LAZY)
    private List<ContestProblemNew> contestProblemNews;
}
