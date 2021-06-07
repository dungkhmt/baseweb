package com.hust.baseweb.applications.backlog.entity;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CompositeProjectMember implements Serializable {

    private UUID backlogProjectId;
    private UUID memberPartyId;
}
