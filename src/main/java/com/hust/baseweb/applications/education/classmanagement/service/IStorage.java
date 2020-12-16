package com.hust.baseweb.applications.education.classmanagement.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface IStorage {

    void uploadToOneDrive(MultipartFile file);
}
