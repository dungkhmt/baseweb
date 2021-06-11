package com.hust.baseweb.applications.backlog.repo;

import com.hust.baseweb.entity.UserLogin;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BacklogUserLoginRepo extends JpaRepository<UserLogin, String> {

    @Query(value = "select distinct ul.*\n" +
                   "from user_login ul inner join public.user_login_security_group ulsg\n" +
                   "on ul.user_login_id = ulsg.user_login_id\n" +
                   "join person ps on ul.party_id = ps.party_id\n" +
                   "where ulsg.group_id = ?2\n" +
                   "and (\n" +
                   "\tlower(ps.first_name) like lower(concat(?3, '%'))\n" +
                   "\tor lower(ps.middle_name) like lower(concat(?3, '%'))\n" +
                   "\tor lower(ps.last_name) like lower(concat(?3, '%'))\n" +
                   "\tor lower(ul.user_login_id) like lower(concat(?3, '%'))\n" +
                   ")\n" +
                   "and ul.party_id not in \n" +
                   "(select bpm.member_party_id from backlog_project_member bpm\n" +
                   "where bpm.backlog_project_id = ?1) order by ul.user_login_id asc",
           nativeQuery = true)
    List<UserLogin> findAllNotMember(UUID projectId, String groupId, String searchString, Pageable pageable);
}
