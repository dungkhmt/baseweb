package com.hust.baseweb.applications.backlog.service;

import com.hust.baseweb.applications.backlog.entity.BacklogProjectMember;
import com.hust.baseweb.applications.backlog.model.CreateBacklogProjectMemberModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface BacklogProjectMemberService {
    BacklogProjectMember save(CreateBacklogProjectMemberModel input);
    List<BacklogProjectMember> findAllByBacklogProjectId(String backlogProjectId);
    List<BacklogProjectMember> findAllByMemberPartyId(UUID partyId);
}
