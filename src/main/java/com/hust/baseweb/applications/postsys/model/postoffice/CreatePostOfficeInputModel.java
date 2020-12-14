package com.hust.baseweb.applications.postsys.model.postoffice;

import com.poiji.annotation.ExcelCellName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostOfficeInputModel {
    @ExcelCellName("postOfficeId")
    private String postOfficeId;
    @ExcelCellName("postOfficeName")
    private String postOfficeName;
    @ExcelCellName("postOfficeLevel")
    private int postOfficeLevel;
    private UUID contactMechId;
    @ExcelCellName("address")
    private String address;
    @ExcelCellName("latitude")
    private Double latitude;
    @ExcelCellName("longtitude")
    private Double longtitude;
}
