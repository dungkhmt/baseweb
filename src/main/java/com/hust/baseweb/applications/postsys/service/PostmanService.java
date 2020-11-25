package com.hust.baseweb.applications.postsys.service;

import com.hust.baseweb.applications.postsys.entity.Postman;
import com.hust.baseweb.applications.postsys.repo.PostmanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostmanService {
    @Autowired
    PostmanRepo postmanRepo;
    public List<Postman> findByPostOfficeId(String postOfficeId) {
        return postmanRepo.findByPostOfficeId(postOfficeId);
    }
}
