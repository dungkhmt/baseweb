package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.document.SolverConfigParam;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface SolverConfigParamRepo extends MongoRepository<SolverConfigParam, Long> {

    SolverConfigParam findFirstByThruDateNull();

    List<SolverConfigParam> findAllByThruDateNull();
}
