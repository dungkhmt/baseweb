package com.hust.baseweb.applications.backlog.service.task;

import com.hust.baseweb.applications.backlog.entity.BacklogTaskAssignment;
import com.hust.baseweb.applications.backlog.model.CreateBacklogTaskAssignmentInputModel;
import com.hust.baseweb.applications.backlog.repo.BacklogTaskAssignmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class BacklogTaskAssignmentServiceImpl implements BacklogTaskAssignmentService {

    @Autowired
    BacklogTaskAssignmentRepo backlogTaskAssignmentRepo;

    @Override
    public BacklogTaskAssignment save(BacklogTaskAssignment taskAssignment) {
        return backlogTaskAssignmentRepo.save(taskAssignment);
    }

    @Override
    public List<BacklogTaskAssignment> create(CreateBacklogTaskAssignmentInputModel input) {
        List<BacklogTaskAssignment> backlogTaskAssignments = new ArrayList<>();

        // add new assignment or modify existed assigment
        for(UUID assignedPartyId : input.getAssignedToPartyId()) {
            if(assignedPartyId == null) break;
            BacklogTaskAssignment assignment = backlogTaskAssignmentRepo.findByBacklogTaskIdAndAndAssignedToPartyId(input.getBacklogTaskId(), assignedPartyId);
            if(assignment == null) {
                if(input.getStartDate() == null) input.setStartDate(new Date());
                backlogTaskAssignments.add(backlogTaskAssignmentRepo.save(
                    new BacklogTaskAssignment(
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

                backlogTaskAssignmentRepo.save(assignment);
            }
        };

        // set status "ASSIGNMENT_INACTIVE" for inactive assignment
        List<BacklogTaskAssignment> assignments = backlogTaskAssignmentRepo.findAllByBacklogTaskId(input.getBacklogTaskId());
        assignments.forEach((assignment) -> {
            AtomicBoolean isExist = new AtomicBoolean(false);

            for(UUID assignedPartyId : input.getAssignedToPartyId()) {
                if(assignedPartyId.equals(assignment.getAssignedToPartyId())) {
                    isExist.set(true);
                    break;
                }
            }

            if(!isExist.get()) {
                assignment.setStatusId("ASSIGNMENT_INACTIVE");

                backlogTaskAssignmentRepo.save(assignment);
            }
        });
        return backlogTaskAssignments;
    }

    @Override
    public List<BacklogTaskAssignment> findAllByBacklogTaskId(UUID backlogTaskId) {
        return backlogTaskAssignmentRepo.findAllByBacklogTaskId(backlogTaskId);
    }

    @Override
    public List<BacklogTaskAssignment> findAllActiveByBacklogTaskId(UUID backlogTaskId) {
        return backlogTaskAssignmentRepo.findAllByBacklogTaskIdAndStatusIdEquals(backlogTaskId, "ASSIGNMENT_ACTIVE");
    }
}
