package com.hust.baseweb.applications.tmscontainer.repo;

import com.hust.baseweb.applications.tmscontainer.entity.ContRequestImportFull;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface ContRequestImportFullPagingRepo extends PagingAndSortingRepository<ContRequestImportFull, UUID> {

}
