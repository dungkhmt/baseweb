package com.hust.baseweb.applications.backlog.entity;

import com.hust.baseweb.applications.backlog.model.CreateBacklogProjectMemberModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@IdClass(CompositeProjectMember.class)
@Table(name="backlog_project_member")
public class BacklogProjectMember {

    public BacklogProjectMember(CreateBacklogProjectMemberModel input){
        backlogProjectId = input.getBacklogProjectId();
        memberPartyId = input.getMemberPartyId();
    }

    @Id
    @Column(name="backlog_project_id")
    private UUID backlogProjectId;

    @Id
    @Column(name="member_party_id")
    private UUID memberPartyId;

    @Column(name="created_stamp", insertable = false, updatable = false)
    private Date createdStamp;
}
