package com.hust.baseweb.applications.backlog.controller;

import com.hust.baseweb.applications.backlog.entity.*;
import com.hust.baseweb.applications.backlog.model.*;
import com.hust.baseweb.applications.backlog.service.Storage.BacklogFileStorageServiceImpl;
import com.hust.baseweb.applications.backlog.service.project.BacklogProjectMemberService;
import com.hust.baseweb.applications.backlog.service.project.BacklogProjectService;
import com.hust.baseweb.applications.backlog.service.task.*;
import com.hust.baseweb.entity.StatusItem;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.repo.StatusItemRepo;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.text.ParseException;
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

    private boolean isInProject(String projectId, String userLoginId) {
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
        ;
        return isExist.get();
    }

    @PostMapping("backlog/create-project")
    public ResponseEntity<?> createProject(
        Principal principal,
        @RequestBody CreateProjectInputModel input
    ) {
        BacklogProject backlogProject = backlogProjectService.save(input);

        if(backlogProject == null) return (ResponseEntity<?>) ResponseEntity.noContent();

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

        if (isExist) {
            return ResponseEntity.ok(backlogProjectService.findByBacklogProjectId(backlogProjectId));
        } else {
            return ResponseEntity.ok(new BacklogProject());
        }
    }

    @GetMapping("backlog/get-project-detail/{backlogProjectId}")
    public ResponseEntity<List<BacklogTaskWithAssignmentAndAssignable>> getProjectDetail(
        Principal principal,
        @PathVariable String backlogProjectId
    ) {
        List<BacklogTask> tasks = backlogTaskService.findByBacklogProjectId(backlogProjectId);
        List<BacklogTaskWithAssignmentAndAssignable> tasksWithAssignment = new ArrayList<>();

        tasks.forEach((task) -> {
            tasksWithAssignment.add(new BacklogTaskWithAssignmentAndAssignable());
            tasksWithAssignment.get(tasksWithAssignment.size() - 1).setBacklogTask(task);

            UUID taskId = task.getBacklogTaskId();
            List<UserLoginReduced> assignedUsers = getAssignedUserByTaskId(taskId);
            List<UserLoginReduced> assignableUsers = getAssignableUserByTaskId(taskId);
            tasksWithAssignment.get(tasksWithAssignment.size() - 1).setAssignment(assignedUsers);
            tasksWithAssignment.get(tasksWithAssignment.size() - 1).setAssignable(assignableUsers);
        });

        return ResponseEntity.ok(tasksWithAssignment);
    }

    @GetMapping("backlog/get-task-detail/{backlogTaskId}")
    public ResponseEntity<BacklogTaskWithAssignmentAndAssignable> getTaskDetail(
        Principal principal,
        @PathVariable UUID backlogTaskId
    ) {
        BacklogTask task = backlogTaskService.findByBacklogTaskId(backlogTaskId);
        List<UserLoginReduced> assignedUsers = getAssignedUserByTaskId(backlogTaskId);
        List<UserLoginReduced> assignableUsers = getAssignableUserByTaskId(backlogTaskId);

        BacklogTaskWithAssignmentAndAssignable backlogTaskWithAssignmentAndAssignable = new BacklogTaskWithAssignmentAndAssignable();
        backlogTaskWithAssignmentAndAssignable.setBacklogTask(task);
        backlogTaskWithAssignmentAndAssignable.setAssignment(assignedUsers);
        backlogTaskWithAssignmentAndAssignable.setAssignable(assignableUsers);

        return ResponseEntity.ok(backlogTaskWithAssignmentAndAssignable);
    }

    @PostMapping("backlog/add-task")
    public ResponseEntity<BacklogTask> addTask(
        Principal principal,
        @RequestBody CreateBacklogTaskInputModel input
        ) throws ParseException {
        return ResponseEntity.ok(backlogTaskService.create(input, principal.getName()));
    }

    @PostMapping("backlog/edit-task")
    public ResponseEntity<BacklogTask> editTask(Principal principal, @RequestBody CreateBacklogTaskInputModel input) throws IOException {
        return ResponseEntity.ok(backlogTaskService.update(input));
    }

    @PostMapping("backlog/add-assignments")
    public ResponseEntity<List<BacklogTaskAssignment>> addAssignments(
        Principal principal,
        @RequestBody
            CreateBacklogTaskAssignmentInputModel input
    ) {
        return ResponseEntity.ok(backlogTaskAssignmentService.save(input));
    }

    @PostMapping("backlog/add-assignable")
    public ResponseEntity<List<BacklogTaskAssignable>> addAssignable(
        Principal principal,
        @RequestBody
            CreateBacklogTaskAssignableInputModel input
    ) {
        return ResponseEntity.ok(backlogTaskAssignableService.save(input));
    }

    @GetMapping("backlog/get-members-of-project/{backlogProjectId}")
    public ResponseEntity<List<UserLoginReduced>> getMemberOfProject(
        Principal principal,
        @PathVariable String backlogProjectId
    ) {
        List<BacklogProjectMember> backlogProjectMembers = backlogProjectMemberService.findAllByBacklogProjectId(
            backlogProjectId);
        List<UserLoginReduced> member = new ArrayList<>();

        backlogProjectMembers.forEach((backlogProjectMember) -> {
            UserLogin user = userService.findUserLoginByPartyId(backlogProjectMember.getMemberPartyId());
            member.add(new UserLoginReduced(user));
        });

        return ResponseEntity.ok(member);
    }

    @GetMapping("backlog/get-assigned-user-by-task-id/{backlogTaskId}")
    public ResponseEntity<List<UserLoginReduced>> getAssignedUserByTaskId(
        Principal principal,
        @PathVariable UUID backlogTaskId
    ) {
        List<UserLoginReduced> assignedUsers = getAssignedUserByTaskId(backlogTaskId);

        return ResponseEntity.ok(assignedUsers);
    }

    @GetMapping("backlog/get-assignable-user-by-task-id/{backlogTaskId}")
    public ResponseEntity<List<UserLoginReduced>> getAssignableUserByTaskId(
        Principal principal,
        @PathVariable UUID backlogTaskId
    ) {
        List<BacklogTaskAssignable> backlogTaskAssignable = backlogTaskAssignableService.findAllActiveByBacklogTaskId(
            backlogTaskId);

        List<UserLoginReduced> assignableUsers = new ArrayList<>();
        backlogTaskAssignable.forEach((taskAssignable) -> {
            UserLogin user = userService.findUserLoginByPartyId(taskAssignable.getAssignedToPartyId());
            assignableUsers.add(new UserLoginReduced(user));
        });

        return ResponseEntity.ok(assignableUsers);
    }

    @PostMapping("backlog/add-member")
    public ResponseEntity<BacklogProject> addMember(
        Principal principal,
        @RequestBody AddBacklogProjectMemberInputModel input
    ) {
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

    @PostMapping("backlog/upload-task-attachment-files/{taskId}")
    public ResponseEntity<String> uploadTaskAttachment(
        Principal principal,
        @PathVariable UUID taskId,
        @RequestParam("file") MultipartFile[] files
    ) {
        String message = "";
        try {
            List<String> fileNames = new ArrayList<>();

            // get prefix for file name same as prefix in backlog_task table
            BacklogTask task = backlogTaskService.findByBacklogTaskId(taskId);
            String[] savedNames = task.getAttachmentPaths().split(";");

            Arrays.asList(files).stream().forEach(file -> {
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
