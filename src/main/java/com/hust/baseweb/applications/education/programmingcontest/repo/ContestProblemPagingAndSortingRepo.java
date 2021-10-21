package com.hust.baseweb.applications.education.programmingcontest.repo;

import com.hust.baseweb.applications.education.programmingcontest.entity.ContestProblemNew;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.ArrayList;

public interface ContestProblemPagingAndSortingRepo extends PagingAndSortingRepository<ContestProblemNew, String> {
    @Query("select cp.problemName from ContestProblemNew cp")
    ArrayList<String> getProblemNamePaging(Pageable pageable);

}
