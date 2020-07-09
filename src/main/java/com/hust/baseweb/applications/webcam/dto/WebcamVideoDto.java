package com.hust.baseweb.applications.webcam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WebcamVideoDto {

    private String objectId;

    private String userLoginId;

    private Date createdDate;

    private String contentId;
}
