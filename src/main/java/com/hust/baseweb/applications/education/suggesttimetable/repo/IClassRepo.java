package com.hust.baseweb.applications.education.suggesttimetable.repo;


import com.hust.baseweb.applications.education.suggesttimetable.entity.EduClass;

import java.util.List;

public interface IClassRepo {
    void saveAll(List<EduClass> eduClass);
}
