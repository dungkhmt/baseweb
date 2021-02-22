package com.hust.baseweb.applications.education.suggesttimetable.repo;


import com.hust.baseweb.applications.education.suggesttimetable.entity.EduClass;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EduClassRepo implements IClassRepo {

    @Delegate
    private IClassMongoRepo classRepo;

    @Override
    public List<EduClass> saveAll(List<EduClass> eduClasses) {
        return classRepo.saveAll(eduClasses);
    }
}
