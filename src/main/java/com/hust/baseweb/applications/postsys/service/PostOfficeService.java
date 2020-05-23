package com.hust.baseweb.applications.postsys.service;

import com.hust.baseweb.applications.postsys.entity.PostOffice;
import com.hust.baseweb.applications.postsys.model.postoffice.CreatePostOfficeInputModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostOfficeService {
    PostOffice save(CreatePostOfficeInputModel input);

    List<PostOffice> findAll();

    PostOffice findByPostOfficeId(String postOfficeId);

    void deleteByPostOfficeId(String postOfficeId);
}
