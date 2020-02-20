package com.hust.baseweb.repo;

import java.util.UUID;

import com.hust.baseweb.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * PersonRepo
 */
public interface PersonRepo  extends JpaRepository<Person,UUID>{

}