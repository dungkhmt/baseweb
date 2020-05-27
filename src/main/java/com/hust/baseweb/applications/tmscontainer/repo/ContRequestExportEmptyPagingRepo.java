package com.hust.baseweb.applications.tmscontainer.repo;

import com.hust.baseweb.applications.tmscontainer.entity.ContRequestExportEmpty;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface ContRequestExportEmptyPagingRepo extends PagingAndSortingRepository<ContRequestExportEmpty, UUID> {

}
