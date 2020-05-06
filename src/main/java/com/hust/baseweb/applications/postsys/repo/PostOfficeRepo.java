package com.hust.baseweb.applications.postsys.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.baseweb.applications.postsys.entity.PostOffice;

public interface PostOfficeRepo extends JpaRepository<PostOffice, String>{
	PostOffice save(PostOffice postOffice);
	PostOffice findByPostOfficeId(String postOfficeId);
	List<PostOffice> findAll();
	void deleteById(String postOfficeId);
}
