package com.hust.baseweb.applications.education.classmanagement.service;

import com.hust.baseweb.applications.education.classmanagement.entity.EduClassSession;

import java.util.List;
import java.util.UUID;

public interface EduClassSessionService {
    EduClassSession save(UUID classId, String sessionName, String description, String userLoginId);
    List<EduClassSession> findAllByClassId(UUID classId);
}
