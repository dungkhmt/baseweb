package com.hust.baseweb.applications.backlog.service.project;

import com.hust.baseweb.applications.backlog.entity.BacklogProjectMember;
import com.hust.baseweb.applications.backlog.model.CreateBacklogProjectMemberModel;
import com.hust.baseweb.applications.backlog.repo.BacklogProjectMemberRepo;
import com.hust.baseweb.applications.backlog.repo.BacklogProjectRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BacklogProjectMemberServiceImpl implements BacklogProjectMemberService {

    BacklogProjectRepo backlogProjectRepo;
    BacklogProjectMemberRepo backlogProjectMemberRepo;

    @Override
    public BacklogProjectMember save(CreateBacklogProjectMemberModel input) {
        if(backlogProjectMemberRepo.existsByBacklogProjectIdAndMemberPartyId(
            input.getBacklogProjectId(),
            input.getMemberPartyId()
        )) {
            return new BacklogProjectMember();
        }
        BacklogProjectMember x = new BacklogProjectMember(input);
        return backlogProjectMemberRepo.save(x);
    }

    @Override
    public List<BacklogProjectMember> findAllByBacklogProjectId(UUID backlogProjectId) {
        return backlogProjectMemberRepo.findAllByBacklogProjectId(backlogProjectId);
    }

    @Override
    public List<BacklogProjectMember> findAllByMemberPartyId(UUID partyId) {
        return backlogProjectMemberRepo.findAllByMemberPartyId(partyId);
    }
}
