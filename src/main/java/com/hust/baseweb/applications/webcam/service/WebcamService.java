package com.hust.baseweb.applications.webcam.service;

import com.hust.baseweb.applications.webcam.dto.WebcamVideoDto;
import org.bson.types.ObjectId;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface WebcamService {

    WebcamVideoDto upload(MultipartFile multipartFile, String userLoginId);

    InputStreamResource get(ObjectId objectId);

    List<WebcamVideoDto> all();

    List<WebcamVideoDto> all(String userLoginId);
}
