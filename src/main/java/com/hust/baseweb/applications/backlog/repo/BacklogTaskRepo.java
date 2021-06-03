package com.hust.baseweb.applications.backlog.repo;

import com.hust.baseweb.applications.backlog.entity.BacklogTask;
import com.hust.baseweb.applications.backlog.model.ProjectFilterParamsModel;
import io.lettuce.core.dynamic.annotation.Param;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;


public interface BacklogTaskRepo extends JpaRepository<BacklogTask, UUID> {

    @NotNull
    BacklogTask save(@NotNull BacklogTask backlogTask);

    List<BacklogTask> findByBacklogProjectId(UUID backlogProjectId);

    @Query(value="select distinct bt.* from " +
                "backlog_task as bt left outer join (select * from backlog_task_assignment where status_id = 'ASSIGNMENT_ACTIVE') as bta on bt.backlog_task_id = bta.backlog_task_id " +
           "where " +
                "bt.backlog_project_id = :backlogProjectId " +
                "and bt.backlog_task_category_id in (select backlog_task_category_id from backlog_task_category where lower(backlog_task_category_name) like lower(CONCAT( :#{#filter.categoryName},'%'))) " +
                "and bt.status_id in (select status_id from status_item where status_type_id = 'BACKLOG_STATUS' and lower(description) like lower(concat( :#{#filter.statusName}, '%'))) " +
                "and bt.priority_id in (select backlog_task_priority_id from backlog_task_priority where lower(backlog_task_priority_name) like lower(CONCAT( :#{#filter.priorityName},'%'))) " +
                "and lower(bt.backlog_task_name) like lower(concat( :#{#filter.backlogTaskName},'%')) " +
                "and lower(bt.created_by_user_login_id) like lower(concat( :#{#filter.createdByUser},'%')) " +
                "and (case when :#{#filter.assignment} != '' then bta.assigned_to_party_id in (select party_id from user_login where lower(user_login_id) like lower(concat( :#{#filter.assignment},'%'))) else true end)",
           nativeQuery = true)
    Page<BacklogTask> findByBacklogProjectId(
        @Param("backlogProjectId") UUID backlogProjectId,
        Pageable pageable,
        @Param("filter") ProjectFilterParamsModel filter
    );

    @Query(value = "select distinct bt.*\n" +
                   "from\n" +
                   "\tbacklog_task as bt join (select * from backlog_task_assignment where status_id = 'ASSIGNMENT_ACTIVE') as bta\n" +
                   "\ton bt.backlog_task_id = bta.backlog_task_id\n" +
                   "where\n" +
                   "\tbt.backlog_project_id = :backlogProjectId\n" +
                   "\tand bta.assigned_to_party_id = :assignedPartyId\n" +
                   "\tand bta.status_id='ASSIGNMENT_ACTIVE'" +
                   "and bt.backlog_task_category_id in (select backlog_task_category_id from backlog_task_category where lower(backlog_task_category_name) like lower(CONCAT( :#{#filter.categoryName},'%'))) " +
                   "and bt.status_id in (select status_id from status_item where status_type_id = 'BACKLOG_STATUS' and lower(description) like lower(concat( :#{#filter.statusName}, '%'))) " +
                   "and bt.priority_id in (select backlog_task_priority_id from backlog_task_priority where lower(backlog_task_priority_name) like lower(CONCAT( :#{#filter.priorityName},'%'))) " +
                   "and lower(bt.backlog_task_name) like lower(concat( :#{#filter.backlogTaskName},'%')) ",
           nativeQuery = true)
    Page<BacklogTask> findByBacklogProjectIdAndPartyAssigned(
        @Param("backlogProjectId") UUID backlogProjectId,
        @Param("assignedPartyId") UUID assignedPartyId,
        @Param("filter") ProjectFilterParamsModel filter,
        Pageable pageable
    );

    @Query(value = "select distinct bt.*\n" +
                   "from\n" +
                   "\tbacklog_task as bt left outer join\n" +
                   "\t(select * from backlog_task_assignment where status_id = 'ASSIGNMENT_ACTIVE') as bta\n" +
                   "\ton bt.backlog_task_id = bta.backlog_task_id\n" +
                   "where\n" +
                   "\tbt.backlog_project_id = :backlogProjectId\n" +
                   "\tand bt.created_by_user_login_id = :userLoginId\n" +
                   "\tand bt.status_id = 'TASK_OPEN'\n" +
                   "and bt.backlog_task_category_id in (select backlog_task_category_id from backlog_task_category where lower(backlog_task_category_name) like lower(CONCAT( :#{#filter.categoryName},'%'))) " +
                   "and bt.status_id in (select status_id from status_item where status_type_id = 'BACKLOG_STATUS' and lower(description) like lower(concat( :#{#filter.statusName}, '%'))) " +
                   "and bt.priority_id in (select backlog_task_priority_id from backlog_task_priority where lower(backlog_task_priority_name) like lower(CONCAT( :#{#filter.priorityName},'%'))) " +
                   "and lower(bt.backlog_task_name) like lower(concat( :#{#filter.backlogTaskName},'%')) " +
                   "group by bt.backlog_task_id\n" +
                   "having count(bta.backlog_task_id) = 0",
           nativeQuery = true)
    Page<BacklogTask> findOpeningTaskByCreatedUserLogin(
        @Param("backlogProjectId") UUID backlogProjectId,
        @Param("userLoginId") String userLoginId,
        @Param("filter") ProjectFilterParamsModel filter,
        Pageable pageable
    );

    BacklogTask findByBacklogTaskId(UUID backlogTaskId);
}
