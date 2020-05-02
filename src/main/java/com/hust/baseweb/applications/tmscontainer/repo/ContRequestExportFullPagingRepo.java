package com.hust.baseweb.applications.tmscontainer.repo;

import com.hust.baseweb.applications.tmscontainer.entity.ContRequestExportFull;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface ContRequestExportFullPagingRepo extends PagingAndSortingRepository<ContRequestExportFull, UUID> {
}