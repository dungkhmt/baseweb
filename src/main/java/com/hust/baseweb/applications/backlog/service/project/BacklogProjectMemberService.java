package com.hust.baseweb.applications.backlog.service.project;

import com.hust.baseweb.applications.backlog.entity.BacklogProjectMember;
import com.hust.baseweb.applications.backlog.model.AddBacklogProjectMemberInputModel;
import com.hust.baseweb.applications.backlog.model.CreateBacklogProjectMemberModel;
import com.hust.baseweb.entity.UserLogin;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface BacklogProjectMemberService {
    BacklogProjectMember save(CreateBacklogProjectMemberModel input);
    List<BacklogProjectMember> findAllByBacklogProjectId(UUID backlogProjectId);
    List<BacklogProjectMember> findAllByMemberPartyId(UUID partyId);
    List<UserLogin> findAllNotMember(UUID projectId, String searchString, Pageable pageable);
    String addMember(AddBacklogProjectMemberInputModel input);
}
