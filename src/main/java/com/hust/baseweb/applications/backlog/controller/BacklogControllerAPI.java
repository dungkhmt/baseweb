package com.hust.baseweb.applications.backlog.controller;

import com.hust.baseweb.applications.backlog.entity.*;
import com.hust.baseweb.applications.backlog.model.*;
import com.hust.baseweb.applications.backlog.service.Storage.BacklogFileStorageServiceImpl;
import com.hust.baseweb.applications.backlog.service.project.BacklogProjectMemberService;
import com.hust.baseweb.applications.backlog.service.project.BacklogProjectService;
import com.hust.baseweb.applications.backlog.service.task.*;
import com.hust.baseweb.entity.Person;
import com.hust.baseweb.entity.StatusItem;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.repo.StatusItemRepo;
import com.hust.baseweb.service.PersonService;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
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
    private BacklogFileStorageServiceImpl storageService;
    private StatusItemRepo statusItemRepo;
    private PersonService personService;


    private List<UserLoginReduced> getAssignedUserByTaskId(UUID taskId) {
        List<BacklogTaskAssignment> backlogTaskAssignments = backlogTaskAssignmentService.findAllActiveByBacklogTaskId(
            taskId);

        List<UserLoginReduced> assignedUsers = new ArrayList<>();
        backlogTaskAssignments.forEach((backlogTaskAssignment) -> {
            UserLogin user = userService.findUserLoginByPartyId(backlogTaskAssignment.getAssignedToPartyId());
            assignedUsers.add(new UserLoginReduced(user));
        });
        return assignedUsers;
    }

    private List<UserLoginReduced> getAssignableUserByTaskId(UUID taskId) {
        List<BacklogTaskAssignable> backlogTaskAssignables = backlogTaskAssignableService.findAllActiveByBacklogTaskId(
            taskId);

        List<UserLoginReduced> assignedUsers = new ArrayList<>();
        backlogTaskAssignables.forEach((backlogTaskAssignable) -> {
            UserLogin user = userService.findUserLoginByPartyId(backlogTaskAssignable.getAssignedToPartyId());
            assignedUsers.add(new UserLoginReduced(user));
        });
        return assignedUsers;
    }

    private boolean isProjectMember(UUID projectId, String userLoginId) {
        UserLogin user = userService.findById(userLoginId);
        UUID userPartyId = user.getParty().getPartyId();

        List<BacklogProjectMember> members = backlogProjectMemberService.findAllByBacklogProjectId(projectId);
        AtomicBoolean isExist = new AtomicBoolean(false);
        for (BacklogProjectMember member : members) {
            if (userPartyId.equals(member.getMemberPartyId())) {
                isExist.set(true);
                break;
            }
        }
        return isExist.get();
    }

    private List<BacklogTaskWithAssignmentAndAssignable> tasksToTaskWithAssigns(List<BacklogTask> tasks) {
        List<BacklogTaskWithAssignmentAndAssignable> tasksWithAssignment = new ArrayList<>();

        tasks.forEach((task) -> {
            tasksWithAssignment.add(new BacklogTaskWithAssignmentAndAssignable());
            tasksWithAssignment.get(tasksWithAssignment.size() - 1).setBacklogTask(task);

            UUID taskId = task.getBacklogTaskId();
            List<UserLoginReduced> assignedUsers = getAssignedUserByTaskId(taskId);
            assignedUsers.forEach(user -> {
                Person person = personService.findByPartyId(user.getPartyId());
                user.setPerson(person);
            });

            List<UserLoginReduced> assignableUsers = getAssignableUserByTaskId(taskId);
            assignableUsers.forEach(user -> {
                Person person = personService.findByPartyId(user.getPartyId());
                user.setPerson(person);
            });
            tasksWithAssignment.get(tasksWithAssignment.size() - 1).setAssignment(assignedUsers);
            tasksWithAssignment.get(tasksWithAssignment.size() - 1).setAssignable(assignableUsers);
        });

        return tasksWithAssignment;
    }

    @PostMapping("backlog/create-project")
    public ResponseEntity<?> createProject(
        Principal principal,
        @RequestBody CreateProjectInputModel input
    ) {
        BacklogProject backlogProject = backlogProjectService.save(input);

        if(backlogProject == null) return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);

        UserLogin userLogin = userService.findById(principal.getName());
        UUID userPartyId = userLogin.getParty().getPartyId();
        CreateBacklogProjectMemberModel backlogProjectMemberInput = new CreateBacklogProjectMemberModel(
            backlogProject.getBacklogProjectId(),
            userPartyId
        );
        backlogProjectMemberService.save(backlogProjectMemberInput);

        log.info("created project, projectId = " + backlogProject.getBacklogProjectId());
        return ResponseEntity.ok(backlogProject);
    }

    @GetMapping("backlog/get-all-project-by-user")
    public ResponseEntity<List<BacklogProject>> getAllProject(Principal principal) {
        UserLogin userLogin = userService.findById(principal.getName());
        UUID userPartyId = userLogin.getParty().getPartyId();

        log.info("get all project by user, userLoginId = " + principal.getName());
        return ResponseEntity.ok(backlogProjectService.findAllByMemberPartyId(userPartyId));
    }

    @GetMapping("backlog/get-project-by-id/{backlogProjectId}")
    public ResponseEntity<BacklogProject> getProjectById(Principal principal, @PathVariable UUID backlogProjectId) {
        boolean isMember = isProjectMember(backlogProjectId, principal.getName());

        if (isMember) {
            log.info("get project information, projectId = " + backlogProjectId);
            return ResponseEntity.status(HttpStatus.OK).body(backlogProjectService.findByBacklogProjectId(backlogProjectId));
        } else {
            log.warn("userLoginId = " + principal.getName() + " is not member of project, projectId = " + backlogProjectId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new BacklogProject());
        }
    }

    @GetMapping("backlog/get-project-detail/{backlogProjectId}")
    public ResponseEntity<List<BacklogTaskWithAssignmentAndAssignable>> getProjectDetail(
        Principal principal,
        @PathVariable UUID backlogProjectId
    ) {
        boolean isMember = isProjectMember(backlogProjectId, principal.getName());
        if(!isMember) {
            log.warn("get project detail failed, userLoginId = " + principal.getName() + " doesn't have permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ArrayList<>());
        }

        List<BacklogTask> tasks = backlogTaskService.findByBacklogProjectId(backlogProjectId);
        List<BacklogTaskWithAssignmentAndAssignable> tasksWithAssignment = tasksToTaskWithAssigns(tasks);

        log.info("get project detail, projectId = " + backlogProjectId);
        return ResponseEntity.status(HttpStatus.OK).body(tasksWithAssignment);
    }

    @GetMapping("backlog/get-project-detail-by-page/{backlogProjectId}")
    public ResponseEntity<Page<BacklogTaskWithAssignmentAndAssignable>> getProjectDetail(
        Principal principal,
        @PathVariable UUID backlogProjectId,
        Pageable pageable,
        @RequestParam(required = false) String backlogTaskName,
        @RequestParam(required = false) String categoryName,
        @RequestParam(required = false) String statusName,
        @RequestParam(required = false) String priorityName,
        @RequestParam(required = false) String assignment
    ) {
        boolean isMember = isProjectMember(backlogProjectId, principal.getName());
        if(!isMember) {
            log.warn("get project detail failed, userLoginId = " + principal.getName() + " doesn't have permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new PageImpl<>(new ArrayList<>(), pageable, 0));
        }

        ProjectFilterParamsModel filter = new ProjectFilterParamsModel(backlogTaskName, categoryName, statusName, priorityName, assignment);
        Page<BacklogTask> tasks = backlogTaskService.findByBacklogProjectId(backlogProjectId, pageable, filter);
        Page<BacklogTaskWithAssignmentAndAssignable> tasksWithAssignment = new PageImpl<>(tasksToTaskWithAssigns(tasks.getContent()), pageable, tasks.getTotalElements());

        log.info("get project detail, pageNumber = " + pageable.getPageNumber() + ", pageSize = " + pageable.getPageSize() + ", projectId = " + backlogProjectId);
        return ResponseEntity.status(HttpStatus.OK).body(tasksWithAssignment);
    }

    @GetMapping("backlog/get-my-task/{backlogProjectId}")
    public ResponseEntity<Page<BacklogTaskWithAssignmentAndAssignable>> getMyTask(
        Principal principal,
        @PathVariable UUID backlogProjectId,
        Pageable pageable,
        @RequestParam(required = false) String backlogTaskName,
        @RequestParam(required = false) String categoryName,
        @RequestParam(required = false) String statusName,
        @RequestParam(required = false) String priorityName,
        @RequestParam(required = false) String assignment
    ) {
        boolean isMember = isProjectMember(backlogProjectId, principal.getName());
        if(!isMember) {
            log.warn("get my tasks failed, userLoginId = " + principal.getName() + " doesn't have permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new PageImpl<>(new ArrayList<>(), pageable, 0));
        }

        UserLogin userLogin = userService.findById(principal.getName());
        UUID userPartyId = userLogin.getParty().getPartyId();

        ProjectFilterParamsModel filter = new ProjectFilterParamsModel(backlogTaskName, categoryName, statusName, priorityName, assignment);
        Page<BacklogTask> tasks = backlogTaskService.findByBacklogProjectIdAndPartyAssigned(backlogProjectId, userPartyId, filter, pageable);
        Page<BacklogTaskWithAssignmentAndAssignable> tasksWithAssignment = new PageImpl<>(tasksToTaskWithAssigns(tasks.getContent()), pageable, tasks.getTotalElements());

        log.info("get my tasks, pageNumber = " + pageable.getPageNumber() + ", pageSize = " + pageable.getPageSize() + ", projectId = " + backlogProjectId);
        return ResponseEntity.status(HttpStatus.OK).body(tasksWithAssignment);
    }

    @GetMapping("backlog/get-opening-task/{backlogProjectId}")
    public ResponseEntity<Page<BacklogTaskWithAssignmentAndAssignable>> getOpeningTask(
        Principal principal,
        @PathVariable UUID backlogProjectId,
        Pageable pageable,
        @RequestParam(required = false) String backlogTaskName,
        @RequestParam(required = false) String categoryName,
        @RequestParam(required = false) String statusName,
        @RequestParam(required = false) String priorityName,
        @RequestParam(required = false) String assignment
    ) {
        boolean isMember = isProjectMember(backlogProjectId, principal.getName());
        if(!isMember) {
            log.warn("get opening task failed, userLoginId = " + principal.getName() + " doesn't have permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new PageImpl<>(new ArrayList<>(), pageable, 0));
        }

        ProjectFilterParamsModel filter = new ProjectFilterParamsModel(backlogTaskName, categoryName, statusName, priorityName, assignment);
        Page<BacklogTask> tasks = backlogTaskService.findOpeningTaskByCreatedUserLogin(backlogProjectId, principal.getName(), filter, pageable);
        Page<BacklogTaskWithAssignmentAndAssignable> tasksWithAssignment = new PageImpl<>(tasksToTaskWithAssigns(tasks.getContent()), pageable, tasks.getTotalElements());

        log.info("get opening tasks, pageNumber = " + pageable.getPageNumber() + ", pageSize = " + pageable.getPageSize() + ", projectId = " + backlogProjectId);
        return ResponseEntity.status(HttpStatus.OK).body(tasksWithAssignment);
    }

    @GetMapping("backlog/get-task-detail/{backlogTaskId}")
    public ResponseEntity<BacklogTaskWithAssignmentAndAssignable> getTaskDetail(
        Principal principal,
        @PathVariable UUID backlogTaskId
    ) {
        BacklogTask task = backlogTaskService.findByBacklogTaskId(backlogTaskId);
        boolean isMember = isProjectMember(task.getBacklogProjectId(), principal.getName());
        if(!isMember) {
            log.info("get task detail, taskId = " + backlogTaskId + ", userLoginId = " + principal.getName() + " don't have permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        List<UserLoginReduced> assignedUsers = getAssignedUserByTaskId(backlogTaskId);
        List<UserLoginReduced> assignableUsers = getAssignableUserByTaskId(backlogTaskId);

        BacklogTaskWithAssignmentAndAssignable backlogTaskWithAssignmentAndAssignable = new BacklogTaskWithAssignmentAndAssignable();
        backlogTaskWithAssignmentAndAssignable.setBacklogTask(task);
        backlogTaskWithAssignmentAndAssignable.setAssignment(assignedUsers);
        backlogTaskWithAssignmentAndAssignable.setAssignable(assignableUsers);

        log.info("get task detail, taskId = " + backlogTaskId);
        return ResponseEntity.ok(backlogTaskWithAssignmentAndAssignable);
    }

    @PostMapping("backlog/add-task")
    public ResponseEntity<BacklogTask> addTask(Principal principal, @RequestBody CreateBacklogTaskInputModel input) {
        BacklogTask task = backlogTaskService.create(input, principal.getName());
        log.info("created task, taskId = " + task.getBacklogTaskId() + " by userLoginId = " + principal.getName());
        return ResponseEntity.ok(task);
    }

    @PostMapping("backlog/edit-task")
    public ResponseEntity<BacklogTask> editTask(Principal principal, @RequestBody CreateBacklogTaskInputModel input) {
        try {
            BacklogTask updatedTask = backlogTaskService.update(input);
            log.info("edit task successful, taskId = " + updatedTask.getBacklogTaskId());
            return ResponseEntity.ok(updatedTask);
        } catch(Exception e) {
            log.error("edit task " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("backlog/update-task-status")
    public ResponseEntity<String> updateTaskStatus(Principal principal, @RequestParam("taskId") UUID taskId, @RequestParam("newStatus") String newStatus) {
        BacklogTask task = backlogTaskService.findByBacklogTaskId(taskId);
        List<BacklogTaskAssignment> assignments = backlogTaskAssignmentService.findAllActiveByBacklogTaskId(taskId);
        UUID principalPartyId = userService.findById(principal.getName()).getParty().getPartyId();

        if(task.getBacklogTaskId() != null) {
            if(!(
                principal.getName().equals(task.getCreatedByUserLoginId()) ||
               (assignments.size() > 0 && assignments.stream().filter(e -> principalPartyId.equals(e.getAssignedToPartyId())).findAny().orElse(null) != null)
            )) {
                log.warn("update status task failed, userLoginId = " + principal.getName() + " doesn't have permission on taskId = " + taskId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            if(!newStatus.equals(task.getStatusId())) {
                for (BacklogTaskAssignment assignment : assignments) {
                    // change compare value if import other status in database
                    switch (newStatus) {
                        case "TASK_RESOLVED":
                        case "TASK_CLOSED":
                            Date finishedDate = new Date();
                            assignment.setFinishedDate(finishedDate);
                            backlogTaskAssignmentService.save(assignment);
                            break;
                        case "TASK_OPEN":
                            break;
                        case "TASK_INPROGRESS":
                            Date startDate = new Date();
                            assignment.setStartDate(startDate);
                            backlogTaskAssignmentService.save(assignment);
                            break;
                    }
                }
            }
            task.setStatusId(newStatus);
            BacklogTask updatedTask = backlogTaskService.save(task);
            log.info("update status task successful, " + taskId + " to " + newStatus);
            return ResponseEntity.status(HttpStatus.OK).body("Update successful");
        } else {
            log.info("update status task failed. Task id not found. TaskId = " + taskId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("taskId not found");
        }
    }

    @PostMapping("backlog/add-assignments")
    public ResponseEntity<List<BacklogTaskAssignment>> addAssignments(Principal principal, @RequestBody CreateBacklogTaskAssignmentInputModel input) {
        String userCreatedTask = backlogTaskService.findByBacklogTaskId(input.getBacklogTaskId()).getCreatedByUserLoginId();
        if(!userCreatedTask.equals(principal.getName())) {
            log.warn("add assignment failed, userLoginId = " + principal.getName() + " doesn't have permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // change case value if import other status in database
        switch (input.getStatusId()) {
            case "TASK_RESOLVED":
            case "TASK_CLOSED":
                Date finished = new Date();
                input.setFinishedDate(finished);
                break;
            case "TASK_OPEN":
                break;
            case "TASK_INPROGRESS":
                Date start = new Date();
                input.setStartDate(start);
                break;
        }
        List<BacklogTaskAssignment> assignments = backlogTaskAssignmentService.create(input);
        log.info("add assignment, taskId = " + input.getBacklogTaskId() +", partyId = " + input.getAssignedToPartyId());
        return ResponseEntity.ok(assignments);
    }

    @PostMapping("backlog/add-multi-task-assignments")
    public void addMultipleTaskAssignments(Principal principal, @RequestBody List<CreateBacklogTaskAssignmentInputModel> input) {
        for(CreateBacklogTaskAssignmentInputModel assignment: input) {
            addAssignments(principal, assignment);
        }
    }

    @PostMapping("backlog/add-assignable")
    public ResponseEntity<List<BacklogTaskAssignable>> addAssignable(Principal principal, @RequestBody CreateBacklogTaskAssignableInputModel input) {
        String userCreatedTask = backlogTaskService.findByBacklogTaskId(input.getBacklogTaskId()).getCreatedByUserLoginId();
        if(!userCreatedTask.equals(principal.getName())) {
            log.warn("add assignables failed, userLoginId = " + principal.getName() + " doesn't have permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        List<BacklogTaskAssignable> assignables = backlogTaskAssignableService.save(input);
        log.info("add assignables, taskId = " + input.getBacklogTaskId());
        return ResponseEntity.ok(assignables);
    }

    @GetMapping("backlog/get-members-of-project/{backlogProjectId}")
    public ResponseEntity<List<UserLoginReduced>> getMemberOfProject(Principal principal, @PathVariable UUID backlogProjectId) {
        boolean isMember = isProjectMember(backlogProjectId, principal.getName());

        if(!isMember) {
            log.warn("get members of projectId = " + backlogProjectId + ", userLoginId = " + principal.getName() + " does not have permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        List<BacklogProjectMember> backlogProjectMembers = backlogProjectMemberService.findAllByBacklogProjectId(backlogProjectId);
        List<UserLoginReduced> member = new ArrayList<>();

        backlogProjectMembers.forEach((backlogProjectMember) -> {
            UserLogin user = userService.findUserLoginByPartyId(backlogProjectMember.getMemberPartyId());
            Person person = personService.findByPartyId(user.getParty().getPartyId());
            member.add(new UserLoginReduced(user, person));
        });

        log.info("get members of projectId = " + backlogProjectId);
        return ResponseEntity.ok(member);
    }

    @GetMapping("backlog/get-assigned-user-by-task-id/{backlogTaskId}")
    public ResponseEntity<List<UserLoginReduced>> getAssignedUserByTaskId(Principal principal, @PathVariable UUID backlogTaskId) {
        UUID projectId = backlogTaskService.findByBacklogTaskId(backlogTaskId).getBacklogProjectId();
        boolean isMember = isProjectMember(projectId, principal.getName());
        if(!isMember) {
            log.warn("get assigned user by taskId failed, userLoginId = " + principal.getName() + " doesn't have permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        List<UserLoginReduced> assignedUsers = getAssignedUserByTaskId(backlogTaskId);
        log.info("get assigned user by taskId successful, taskId = " + backlogTaskId);
        return ResponseEntity.ok(assignedUsers);
    }

    @GetMapping("backlog/get-assignable-user-by-task-id/{backlogTaskId}")
    public ResponseEntity<List<UserLoginReduced>> getAssignableUserByTaskId(Principal principal, @PathVariable UUID backlogTaskId) {
        UUID projectId = backlogTaskService.findByBacklogTaskId(backlogTaskId).getBacklogProjectId();
        boolean isMember = isProjectMember(projectId, principal.getName());
        if(!isMember) {
            log.warn("get assignables user by taskId failed, userLoginId = " + principal.getName() + " doesn't have permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        List<BacklogTaskAssignable> backlogTaskAssignable = backlogTaskAssignableService.findAllActiveByBacklogTaskId(backlogTaskId);

        List<UserLoginReduced> assignableUsers = new ArrayList<>();
        backlogTaskAssignable.forEach((taskAssignable) -> {
            UserLogin user = userService.findUserLoginByPartyId(taskAssignable.getAssignedToPartyId());
            assignableUsers.add(new UserLoginReduced(user));
        });
        log.info("get assignables user by taskId successful, taskId = " + backlogTaskId);
        return ResponseEntity.ok(assignableUsers);
    }

    @PostMapping("backlog/add-member")
    public ResponseEntity<BacklogProject> addMember(Principal principal, @RequestBody AddBacklogProjectMemberInputModel input) {
        boolean isMember = isProjectMember(input.getBacklogProjectId(), principal.getName());
        if(!isMember) {
            log.warn("add member to projectId = " + input.getBacklogProjectId() + " failed. User " + principal.getName() + " doesn't have permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        List<String> newMembersLoginId = input.getUsersLoginId();
        for (String newMember : newMembersLoginId) {
            UserLogin userLogin = userService.findById(newMember);
            if (userLogin != null) {
                UUID userPartyId = userLogin.getParty().getPartyId();
                CreateBacklogProjectMemberModel createProjectMemberModel = new CreateBacklogProjectMemberModel(
                    input.getBacklogProjectId(),
                    userPartyId
                );
                backlogProjectMemberService.save(createProjectMemberModel);
            }
        }
        log.info("add member to projectId = " + input.getBacklogProjectId() + "successful");
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
            Person person = personService.findByPartyId(user.getParty().getPartyId());
            usersLoginReduced.add(new UserLoginReduced(user, person));
        });
        return ResponseEntity.ok(usersLoginReduced);
    }

    @PostMapping("backlog/upload-task-attachment-files/{taskId}")
    public ResponseEntity<String> uploadTaskAttachment(Principal principal, @PathVariable UUID taskId, @RequestParam("file") MultipartFile[] files) {
        UUID projectId = backlogTaskService.findByBacklogTaskId(taskId).getBacklogProjectId();
        boolean isMember = isProjectMember(projectId, principal.getName());
        if(!isMember) {
            log.warn("upload attachment files failed, userLoginId = " + principal.getName() + "doesn't have permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        String message = "";
        try {
            List<String> fileNames = new ArrayList<>();

            // get prefix for file name same as prefix in backlog_task table
            BacklogTask task = backlogTaskService.findByBacklogTaskId(taskId);
            String[] savedNames = task.getAttachmentPaths().split(";");

            Arrays.asList(files).forEach(file -> {
                try {
                    StringBuilder prefix = new StringBuilder();
                    for(String savedName : savedNames){
                        if(savedName.substring(savedName.indexOf("-") + 1).equals(file.getOriginalFilename())) {
                            prefix.append(savedName, 0, savedName.indexOf("-"));
                            break;
                        }
                    }
                    if(prefix.length() > 0) {
                        storageService.store(file, "", prefix + "-" + file.getOriginalFilename());
                        fileNames.add(file.getOriginalFilename());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            message = "Uploaded the files successfully: " + fileNames;
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "Fail to upload files!";
            log.error( "Fail to upload files!", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @GetMapping("backlog/download-attachment-files/{filename:.+}")
    @ResponseBody
    public void downloadTaskAttachment(HttpServletResponse response, @PathVariable String filename) {
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + filename + "\"");
        response.setContentType("application/octet-stream");

        try (InputStream is = storageService.loadFileAsResource(filename, "")) {
            IOUtils.copyLarge(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("backlog/suggest-assignment")
    public ResponseEntity<List<AssignSuggestionResponseModel>> suggestAssignment(
        Principal principal,
        @RequestBody List<UUID> taskId
    ) {
        System.out.println("input: ");
        for (UUID id : taskId) {
            System.out.println(id);
        }

        // get task and assignable information
        ArrayList<AssignmentSuggestionSolverInput> suggestionSolverInput = new ArrayList<>();
        for (UUID id : taskId) {
            AssignmentSuggestionSolverInput input = new AssignmentSuggestionSolverInput();

            // get task information
            BacklogTask task = backlogTaskService.findByBacklogTaskId(id);
            input.setStartDate(task.getFromDate());
            input.setEndDate(task.getDueDate());
            input.setBacklogTaskId(id);

            // get assignable party id
            List<BacklogTaskAssignable> assignable = backlogTaskAssignableService.findAllActiveByBacklogTaskId(id);
            ArrayList<UUID> assignablePartyIds = new ArrayList<>();
            for (BacklogTaskAssignable element : assignable) {
                assignablePartyIds.add(element.getAssignedToPartyId());
            }
            input.setAssignablePartyIds(assignablePartyIds);

            suggestionSolverInput.add(input);
        }

        // call solver api
        final String uri = "http://localhost:8776/solver/solve";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ArrayList<AssignmentSuggestionSolverInput>> request = new HttpEntity<ArrayList<AssignmentSuggestionSolverInput>>(
            suggestionSolverInput);
        AssignmentSuggestionSolverOutput result = restTemplate.postForObject(
            uri,
            request,
            AssignmentSuggestionSolverOutput.class);

        // create ResponseEntity
        List<AssignSuggestionResponseModel> suggestion = new ArrayList<>();
        assert result != null;
        if (result.getAssignmentSuggestion() != null && result.getAssignmentSuggestion().size() > 0) {
            for (Map.Entry<UUID, UUID> entry : result.getAssignmentSuggestion().entrySet()) {
                UUID suggestionTaskId = entry.getKey();
                UUID suggestionPartyId = entry.getValue();
                BacklogTask task = backlogTaskService.findByBacklogTaskId(suggestionTaskId);
                UserLogin user = userService.findUserLoginByPartyId(suggestionPartyId);
                UserLoginReduced userReduced = new UserLoginReduced(user);

                suggestion.add(new AssignSuggestionResponseModel(task, userReduced));
            }
        }

        // delete
        System.out.println("result: ");
        for (AssignSuggestionResponseModel assign : suggestion) {
            System.out.println("suggest: " + assign.getBacklogTask().getBacklogTaskName() +
                               " " + assign.getUserSuggestion().getUserLoginId());
        }

        return ResponseEntity.ok(suggestion);
    }
}
