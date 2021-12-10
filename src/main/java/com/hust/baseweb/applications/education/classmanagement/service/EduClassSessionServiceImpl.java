package com.hust.baseweb.applications.education.classmanagement.service;

import com.hust.baseweb.applications.education.classmanagement.entity.EduClassSession;
import com.hust.baseweb.applications.education.classmanagement.repo.EduClassSessionRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))

public class EduClassSessionServiceImpl implements EduClassSessionService{
    private EduClassSessionRepo eduClassSessionRepo;

    @Override
    public EduClassSession save(UUID classId, String sessionName, String description, String userLoginId) {
        EduClassSession o = new EduClassSession();
        o.setClassId(classId);
        o.setSessionName(sessionName);
        o.setCreatedByUserLoginId(userLoginId);
        o.setDescription(description);
        o.setStatusId(EduClassSession.STATUS_CREATED);
        o = eduClassSessionRepo.save(o);
        return o;
    }

    @Override
    public List<EduClassSession> findAllByClassId(UUID classId) {
        List<EduClassSession> lst = eduClassSessionRepo.findAllByClassId(classId);
        return lst;
    }
}
