package com.hust.baseweb.applications.backlog.entity;

import com.hust.baseweb.applications.backlog.model.CreateBacklogProjectMemberModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="backlog_project_member")
public class BacklogProjectMember {

    public BacklogProjectMember(CreateBacklogProjectMemberModel input){
        backlogProjectId = input.getBacklogProjectId();
        memberPartyId = input.getMemberPartyId();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="backlog_project_member_id")
    private UUID backlogProjectMemberId;

    @Column(name="backlog_project_id")
    private String backlogProjectId;

    @Column(name="member_party_id")
    private UUID memberPartyId;
}
