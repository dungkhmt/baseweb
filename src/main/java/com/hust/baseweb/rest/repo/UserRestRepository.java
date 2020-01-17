package com.hust.baseweb.rest.repo;

import java.util.UUID;

import com.hust.baseweb.rest.entity.UserCombineEntity;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = true)
public interface UserRestRepository extends PagingAndSortingRepository<UserCombineEntity,UUID> {
}

