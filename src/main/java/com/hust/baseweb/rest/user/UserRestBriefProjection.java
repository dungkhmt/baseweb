package com.hust.baseweb.rest.user;

import java.util.Date;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

/**
 * UserRestBriefProjection
 */
@Projection(name = "brief", types = { DPerson.class })
public interface UserRestBriefProjection {
    String getPartyCode();

    @Value("#{target.person.firstName} #{target.person.lastName}")
    String getFullName();

    @Value("#{target.userLogin.userLoginId}")
    String getUserLoginId();

    @Value("#{target.createdDate}")
    Date getCreatedDate();

    @Value("#{target.type.id}")
    String getPartyType();

  //  @Value("#{target.status.description}")
    String getStatus();
    String getPartyId();

}