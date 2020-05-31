package com.hust.baseweb.applications.tmscontainer.repo;

import com.hust.baseweb.applications.tmscontainer.entity.ContRequestImportEmpty;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface ContRequestImportEmptyPagingRepo extends PagingAndSortingRepository<ContRequestImportEmpty, UUID> {

}
