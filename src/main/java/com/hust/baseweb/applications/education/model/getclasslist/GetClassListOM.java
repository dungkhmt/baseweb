package com.hust.baseweb.applications.education.model.getclasslist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
public class GetClassListOM {

    private short semesterId;

    private Page<ClassOM> page;
}
