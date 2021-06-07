package com.hust.baseweb.applications.postsys.service;

import com.hust.baseweb.applications.postsys.entity.PostPackageType;
import com.hust.baseweb.applications.postsys.repo.PostPackageTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostPackageTypeService {

    @Autowired
    PostPackageTypeRepo postPackageTypeRepo;

    public List<PostPackageType> getListPostPackageType() {
        return postPackageTypeRepo.findAll();
    }
}
