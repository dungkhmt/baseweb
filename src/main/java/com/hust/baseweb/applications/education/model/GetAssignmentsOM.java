package com.hust.baseweb.applications.education.model;

import java.util.Date;

public interface GetAssignmentsOM {

    String getId();

    String getName();

    Date getOpenTime();

    Date getCloseTime();

    boolean getDeleted();
}
