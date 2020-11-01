package com.hust.baseweb.applications.backlog.controller;

import com.hust.baseweb.applications.backlog.entity.*;
import com.hust.baseweb.applications.backlog.model.*;
import com.hust.baseweb.applications.backlog.service.*;
import com.hust.baseweb.entity.StatusItem;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.repo.StatusItemRepo;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class BacklogControllerAPI {

    private BacklogProjectService backlogProjectService;
    private BacklogTaskService backlogTaskService;
    private BacklogProjectMemberService backlogProjectMemberService;
    private UserService userService;
    private BacklogTaskAssignmentService backlogTaskAssignmentService;
    private BacklogTaskAssignableService backlogTaskAssignableService;
    private BacklogTaskCategoryService backlogTaskCategoryService;
    private BacklogTaskPriorityService backlogTaskPriorityService;
    private StatusItemRepo statusItemRepo;

    private List<UserLoginReduced> getAssignedUserByTaskId(UUID taskId) {
        List<BacklogTaskAssignment> backlogTaskAssignments = backlogTaskAssignmentService.findAllActiveByBacklogTaskId(taskId);

        List<UserLoginReduced> assignedUsers = new ArrayList<>();
        backlogTaskAssignments.forEach((backlogTaskAssignment) -> {
            UserLogin user = userService.findUserLoginByPartyId(backlogTaskAssignment.getAssignedToPartyId());
            assignedUsers.add(new UserLoginReduced(user));
        });
        return assignedUsers;
    }

    private boolean isInProject(String projectId, String userLoginId) {
        UserLogin user = userService.findById(userLoginId);
        UUID userPartyId = user.getParty().getPartyId();

        List<BacklogProjectMember> members = backlogProjectMemberService.findAllByBacklogProjectId(projectId);
        AtomicBoolean isExist = new AtomicBoolean(false);
        for(BacklogProjectMember member : members) {
            if(userPartyId.equals(member.getMemberPartyId())) {
                isExist.set(true);
                break;
            }
        };
        return isExist.get();
    }

    @PostMapping("backlog/create-project")
    public ResponseEntity<BacklogProject> createProject(Principal principal, @RequestBody CreateProjectInputModel input) {
        BacklogProject backlogProject = backlogProjectService.save(input);

        UserLogin userLogin = userService.findById(principal.getName());
        UUID userPartyId = userLogin.getParty().getPartyId();
        CreateBacklogProjectMemberModel backlogProjectMemberInput = new CreateBacklogProjectMemberModel(
            input.getBacklogProjectId(),
            userPartyId
        );
        backlogProjectMemberService.save(backlogProjectMemberInput);
        return ResponseEntity.ok(backlogProject);
    }

    @GetMapping("backlog/get-all-project-by-user")
    public ResponseEntity<List<BacklogProject>> getAllProject(Principal principal) {
        UserLogin userLogin = userService.findById(principal.getName());
        UUID userPartyId = userLogin.getParty().getPartyId();

        return ResponseEntity.ok(backlogProjectService.findAllByMemberPartyId(userPartyId));
    }

    @GetMapping("backlog/get-project-by-id/{backlogProjectId}")
    public ResponseEntity<BacklogProject> getProjectById(Principal principal, @PathVariable String backlogProjectId) {
        boolean isExist = isInProject(backlogProjectId, principal.getName());

        if(isExist) {
            return ResponseEntity.ok(backlogProjectService.findByBacklogProjectId(backlogProjectId));
        } else {
            return ResponseEntity.ok(new BacklogProject());
        }
    }

    @GetMapping("backlog/get-project-detail/{backlogProjectId}")
    public ResponseEntity<List<BacklogTaskWithAssignment>> getProjectDetail(Principal principal, @PathVariable String backlogProjectId) {
        List<BacklogTask> tasks = backlogTaskService.findByBacklogProjectId(backlogProjectId);
        List<BacklogTaskWithAssignment> tasksWithAssignment = new ArrayList<>();

        tasks.forEach((task) -> {
            tasksWithAssignment.add(new BacklogTaskWithAssignment());
            tasksWithAssignment.get(tasksWithAssignment.size() - 1).setBacklogTask(task);

            UUID taskId = task.getBacklogTaskId();
            List<UserLoginReduced> assignedUsers = getAssignedUserByTaskId(taskId);
            tasksWithAssignment.get(tasksWithAssignment.size() - 1).setAssignment(assignedUsers);
        });

        return ResponseEntity.ok(tasksWithAssignment);
    }

    @GetMapping("backlog/get-task-detail/{backlogTaskId}")
    public ResponseEntity<BacklogTaskWithAssignment> getTaskDetail(Principal principal, @PathVariable UUID backlogTaskId) {
        BacklogTask task = backlogTaskService.findByBacklogTaskId(backlogTaskId);
        List<UserLoginReduced> assignedUsers = getAssignedUserByTaskId(backlogTaskId);

        BacklogTaskWithAssignment backlogTaskWithAssignment = new BacklogTaskWithAssignment();
        backlogTaskWithAssignment.setBacklogTask(task);
        backlogTaskWithAssignment.setAssignment(assignedUsers);

        return ResponseEntity.ok(backlogTaskWithAssignment);
    }

    @PostMapping("backlog/add-task")
    public ResponseEntity<BacklogTask> addTask(Principal principal, @RequestBody CreateBacklogTaskInputModel input) throws ParseException {
        return ResponseEntity.ok(backlogTaskService.create(input, principal.getName()));
    }

    @PostMapping("backlog/edit-task")
    public ResponseEntity<BacklogTask> editTask(Principal principal, @RequestBody CreateBacklogTaskInputModel input) {
        return ResponseEntity.ok(backlogTaskService.update(input));
    }

    @PostMapping("backlog/add-assignments")
    public ResponseEntity<List<BacklogTaskAssignment>> addAssignments(Principal principal, @RequestBody CreateBacklogTaskAssignmentInputModel input) {
        return ResponseEntity.ok(backlogTaskAssignmentService.save(input));
    }

    @PostMapping("backlog/add-assignable")
    public ResponseEntity<List<BacklogTaskAssignable>> addAssignable(Principal principal, @RequestBody CreateBacklogTaskAssignableInputModel input) {
        return ResponseEntity.ok(backlogTaskAssignableService.save(input));
    }

    @GetMapping("backlog/get-members-of-project/{backlogProjectId}")
    public ResponseEntity<List<UserLoginReduced>> getMemberOfProject(Principal principal, @PathVariable String backlogProjectId){
        List<BacklogProjectMember> backlogProjectMembers = backlogProjectMemberService.findAllByBacklogProjectId(backlogProjectId);
        List<UserLoginReduced> member = new ArrayList<>();

        backlogProjectMembers.forEach((backlogProjectMember) -> {
            UserLogin user = userService.findUserLoginByPartyId(backlogProjectMember.getMemberPartyId());
            member.add(new UserLoginReduced(user));
        });

        return ResponseEntity.ok(member);
    }

    @GetMapping("backlog/get-assigned-user-by-task-id/{backlogTaskId}")
    public ResponseEntity<List<UserLoginReduced>> getAssignedUserByTaskId(Principal principal, @PathVariable UUID backlogTaskId) {
        List<UserLoginReduced> assignedUsers = getAssignedUserByTaskId(backlogTaskId);

        return ResponseEntity.ok(assignedUsers);
    }

    @GetMapping("backlog/get-assignable-user-by-task-id/{backlogTaskId}")
    public ResponseEntity<List<UserLoginReduced>> getAssignableUserByTaskId(Principal principal, @PathVariable UUID backlogTaskId) {
        List<BacklogTaskAssignable> backlogTaskAssignable = backlogTaskAssignableService.findAllActiveByBacklogTaskId(backlogTaskId);

        List<UserLoginReduced> assignableUsers = new ArrayList<>();
        backlogTaskAssignable.forEach((taskAssignable) -> {
            UserLogin user = userService.findUserLoginByPartyId(taskAssignable.getAssignedToPartyId());
            assignableUsers.add(new UserLoginReduced(user));
        });

        return ResponseEntity.ok(assignableUsers);
    }

    @PostMapping("backlog/add-member")
    public ResponseEntity<BacklogProject> addMember(Principal principal, @RequestBody AddBacklogProjectMemberInputModel input) {
        List<String> newMembersLoginId = input.getUsersLoginId();
        for(String newMember: newMembersLoginId) {
            UserLogin userLogin = userService.findById(newMember);
            if(userLogin != null) {
                UUID userPartyId = userLogin.getParty().getPartyId();
                CreateBacklogProjectMemberModel createProjectMemberModel = new CreateBacklogProjectMemberModel(
                    input.getBacklogProjectId(),
                    userPartyId
                );
                backlogProjectMemberService.save(createProjectMemberModel);
            }
        }

        return ResponseEntity.ok(backlogProjectService.findByBacklogProjectId(input.getBacklogProjectId()));
    }

    @GetMapping("backlog/get-backlog-task-category")
    public ResponseEntity<List<BacklogTaskCategory>> getBacklogCategory(Principal principal) {
        return ResponseEntity.ok(backlogTaskCategoryService.findAll());
    }

    @GetMapping("backlog/get-backlog-task-status")
    public ResponseEntity<List<StatusItem>> getBacklogStatus(Principal principal) {
        return ResponseEntity.ok(statusItemRepo.findAllByStatusIdStartsWith("TASK"));
    }

    @GetMapping("backlog/get-backlog-task-priority")
    public ResponseEntity<List<BacklogTaskPriority>> getTaskPriority(Principal principal) {
        return ResponseEntity.ok(backlogTaskPriorityService.findAll());
    }

    @GetMapping("backlog/get-all-user")
    public ResponseEntity<List<UserLoginReduced>> getAllUserBacklog(Principal principal) {
        List<UserLogin> users = userService.getAllUserLogins();
        List<UserLoginReduced> usersLoginReduced = new ArrayList<>();
        users.forEach(user -> {
            usersLoginReduced.add(new UserLoginReduced(user));
        });
        return ResponseEntity.ok(usersLoginReduced);
    }
}
