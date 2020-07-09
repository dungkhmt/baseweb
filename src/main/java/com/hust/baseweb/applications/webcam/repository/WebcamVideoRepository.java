package com.hust.baseweb.applications.webcam.repository;

import com.hust.baseweb.applications.webcam.document.WebcamVideo;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface WebcamVideoRepository extends MongoRepository<WebcamVideo, ObjectId> {

    List<WebcamVideo> findAllByUserLoginId(String userLoginId);

}
