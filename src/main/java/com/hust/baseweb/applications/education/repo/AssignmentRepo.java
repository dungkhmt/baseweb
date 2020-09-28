package com.hust.baseweb.applications.education.repo;

import com.hust.baseweb.applications.education.entity.Assignment;
import com.hust.baseweb.applications.education.model.GetSubmissionsOM;
import com.hust.baseweb.applications.education.model.getassignmentdetail.AssignmentDetailOM;
import com.hust.baseweb.applications.education.model.getassignmentdetail4teacher.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface AssignmentRepo extends JpaRepository<Assignment, UUID> {

    @Query(value = "select ea.assignment_name \"name\", \n" +
                   "\tea.subject, \n" +
                   "\tea.dead_line deadLine, \n" +
                   "\tea.created_stamp createdStamp \n" +
                   "from edu_assignment ea \n" +
                   "where ea.id = ?1",
           nativeQuery = true)
    AssignmentDetailOM getAssignmentDetail(UUID id);

    @Query(value = "select eas.student_id studentId,\n" +
                   "\tconcat(p.last_name, ' ', p.middle_name, ' ', p.first_name) \"name\",\n" +
                   "\teas.last_updated_stamp submissionDate\n" +
                   "from edu_assignment_submission eas \n" +
                   "\tinner join user_login ul on eas.student_id = ul.user_login_id \n" +
                   "\tinner join person p on ul.party_id = p.party_id \n" +
                   "where eas.assignment_id = ?1\n" +
                   "order by eas.last_updated_stamp desc",
           nativeQuery = true)
    List<Submission> getStudentSubmissionsOf(UUID assignmentId);

    @Query(value = "select cast(class_id as varchar) from edu_assignment ea where ea.id = ?1", nativeQuery = true)
    String getClassIdOf(UUID assignmentId);

    @Query(value = "select eas.student_id studentId,\n" +
                   "\teas.original_file_name originalFileName\n" +
                   "from edu_assignment_submission eas \n" +
                   "where eas.assignment_id = ?1 and eas.student_id in ?2",
           nativeQuery = true)
    List<GetSubmissionsOM> getSubmissionsOf(UUID assignmentId, Set<String> studentIds);
}
