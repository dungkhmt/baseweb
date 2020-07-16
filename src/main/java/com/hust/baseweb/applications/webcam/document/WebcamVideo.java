package com.hust.baseweb.applications.webcam.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("webcam_videos")
public class WebcamVideo {

    @Id
    private ObjectId objectId;

    private String userLoginId;

    private Date createdDate;

    private UUID contentId;
}
