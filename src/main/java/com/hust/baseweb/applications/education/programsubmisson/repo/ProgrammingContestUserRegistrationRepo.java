package com.hust.baseweb.applications.education.programsubmisson.repo;

import com.hust.baseweb.applications.education.programsubmisson.entity.CompositeProgrammingContestUserRegistrationId;
import com.hust.baseweb.applications.education.programsubmisson.entity.ProgrammingContestUserRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface ProgrammingContestUserRegistrationRepo extends JpaRepository<ProgrammingContestUserRegistration, CompositeProgrammingContestUserRegistrationId> {
    ProgrammingContestUserRegistration save(ProgrammingContestUserRegistration programmingContestUserRegistration);
    List<ProgrammingContestUserRegistration> findByUserLoginIdAndStatusId(String userLoginId, String statusId);
    List<ProgrammingContestUserRegistration> findByContestIdAndStatusId(String contestId, String statusId);
    List<ProgrammingContestUserRegistration> findByUserLoginId(String userLoginId);
    List<ProgrammingContestUserRegistration> findByContestId(String contestId);
    List<ProgrammingContestUserRegistration> findByStatusId(String statusId);

    ProgrammingContestUserRegistration findByContestIdAndUserLoginId(String contestId, String userLoginId);

    @Transactional
    @Modifying
    @Query(value="update programming_contest_user_registration as p set p.status_id = ?3 where p.contest_id = ?1 and p.user_login_id = ?2", nativeQuery=true)
    public void updateStatus(String contestId, String userLoginId, String statusId);

}
