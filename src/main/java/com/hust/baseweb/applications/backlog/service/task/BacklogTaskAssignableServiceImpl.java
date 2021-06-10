package com.hust.baseweb.applications.backlog.service.task;

import com.hust.baseweb.applications.backlog.entity.BacklogTaskAssignable;
import com.hust.baseweb.applications.backlog.model.CreateBacklogTaskAssignableInputModel;
import com.hust.baseweb.applications.backlog.repo.BacklogTaskAssignableRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BacklogTaskAssignableServiceImpl implements BacklogTaskAssignableService {

    BacklogTaskAssignableRepo backlogTaskAssignableRepo;

    @Override
    public List<BacklogTaskAssignable> save(CreateBacklogTaskAssignableInputModel input) {
        List<BacklogTaskAssignable> backlogTaskAssignments = new ArrayList<>();
        if (input.getAssignedToPartyId() == null) {
            input.setAssignedToPartyId(new ArrayList<>());
        }
        // add new assignment or modify existed assigment
        for (UUID assignedPartyId : input.getAssignedToPartyId()) {
            if (assignedPartyId == null) {
                break;
            }
            BacklogTaskAssignable assignment = backlogTaskAssignableRepo.findByBacklogTaskIdAndAndAssignedToPartyId(
                input.getBacklogTaskId(),
                assignedPartyId);
            if (assignment == null) {
                if (input.getStartDate() == null) {
                    input.setStartDate(new Date());
                }
                backlogTaskAssignments.add(backlogTaskAssignableRepo.save(
                    new BacklogTaskAssignable(
                        input.getBacklogTaskId(),
                        assignedPartyId,
                        "ASSIGNMENT_ACTIVE",
                        input.getStartDate(),
                        input.getFinishedDate()
                    )
                ));
            } else {
                assignment.setFinishedDate(input.getFinishedDate());
                assignment.setStatusId("ASSIGNMENT_ACTIVE");

                backlogTaskAssignableRepo.save(assignment);
            }
        }
        ;

        // set status "ASSIGNMENT_INACTIVE" for inactive assignment
        List<BacklogTaskAssignable> assignments = backlogTaskAssignableRepo.findAllByBacklogTaskId(input.getBacklogTaskId());
        assignments.forEach((assignment) -> {
            AtomicBoolean isExist = new AtomicBoolean(false);

            for (UUID assignedPartyId : input.getAssignedToPartyId()) {
                if (assignedPartyId.equals(assignment.getAssignedToPartyId())) {
                    isExist.set(true);
                    break;
                }
            }

            if (!isExist.get()) {
                assignment.setStatusId("ASSIGNMENT_INACTIVE");

                backlogTaskAssignableRepo.save(assignment);
            }
        });
        return backlogTaskAssignments;
    }

    @Override
    public List<BacklogTaskAssignable> findAllByBacklogTaskId(UUID backlogTaskId) {
        return backlogTaskAssignableRepo.findAllByBacklogTaskId(backlogTaskId);
    }

    @Override
    public List<BacklogTaskAssignable> findAllActiveByBacklogTaskId(UUID backlogTaskId) {
        return backlogTaskAssignableRepo.findAllByBacklogTaskIdAndStatusIdEquals(backlogTaskId, "ASSIGNMENT_ACTIVE");
    }
}
