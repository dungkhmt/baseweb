package com.hust.baseweb.applications.postsys.model.postdriver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDriverUpdateInputModel {
    private String postDriverId;
    private String postDriverName;
}
