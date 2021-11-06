package com.hust.baseweb.applications.education.programmingcontest.repo;

import com.hust.baseweb.applications.education.programmingcontest.entity.ProblemSourceCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemSourceCodeRepo extends JpaRepository<ProblemSourceCode, String> {
}
