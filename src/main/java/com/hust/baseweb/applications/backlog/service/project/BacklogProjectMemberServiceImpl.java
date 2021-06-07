package com.hust.baseweb.applications.backlog.service.project;

import com.hust.baseweb.applications.backlog.entity.BacklogProjectMember;
import com.hust.baseweb.applications.backlog.eumeration.BacklogEnum;
import com.hust.baseweb.applications.backlog.model.AddBacklogProjectMemberInputModel;
import com.hust.baseweb.applications.backlog.model.CreateBacklogProjectMemberModel;
import com.hust.baseweb.applications.backlog.repo.BacklogProjectMemberRepo;
import com.hust.baseweb.applications.backlog.repo.BacklogProjectRepo;
import com.hust.baseweb.applications.backlog.repo.BacklogUserLoginRepo;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.repo.UserLoginRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BacklogProjectMemberServiceImpl implements BacklogProjectMemberService {

    BacklogProjectRepo backlogProjectRepo;
    BacklogProjectMemberRepo backlogProjectMemberRepo;
    BacklogUserLoginRepo backlogUserLoginRepo;
    UserLoginRepo userLoginRepo;

    @Override
    public BacklogProjectMember save(CreateBacklogProjectMemberModel input) {
        if (backlogProjectMemberRepo.existsByBacklogProjectIdAndMemberPartyId(
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

    @Override
    public List<UserLogin> findAllNotMember(UUID projectId, String searchString, Pageable pageable) {
        return backlogUserLoginRepo.findAllNotMember(
            projectId,
            BacklogEnum.BACKLOG_GROUP_PERMISSION.getValue(),
            searchString,
            pageable);
    }

    @Override
    @Transactional
    public String addMember(AddBacklogProjectMemberInputModel input) {
        List<String> newMembersLoginId = input.getUsersLoginId();
        for (String userLoginId : newMembersLoginId) {
            UserLogin userLogin = userLoginRepo.findByUserLoginId(userLoginId);
            List<String> perms = userLoginRepo.findGroupPermsByUserLoginId(userLoginId);

            if (userLogin != null && perms.contains(BacklogEnum.BACKLOG_GROUP_PERMISSION.getValue())) {
                UUID userPartyId = userLogin.getParty().getPartyId();
                CreateBacklogProjectMemberModel createProjectMemberModel = new CreateBacklogProjectMemberModel(
                    input.getBacklogProjectId(),
                    userPartyId
                );

                this.save(createProjectMemberModel);
            } else {
                return "FAILED";
            }
        }
        return "SUCCESSFUL";
    }
}
