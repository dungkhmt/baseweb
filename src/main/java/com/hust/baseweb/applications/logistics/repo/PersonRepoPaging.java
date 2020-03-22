package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.entity.Person;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface PersonRepoPaging extends PagingAndSortingRepository<Person, UUID> {
}
