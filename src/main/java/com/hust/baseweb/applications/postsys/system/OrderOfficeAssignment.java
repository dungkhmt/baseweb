package com.hust.baseweb.applications.postsys.system;

import org.springframework.scheduling.annotation.Scheduled;

public class OrderOfficeAssignment {

    @Scheduled(cron = "*/5 * * * *")
    public void orderOfficeAssign() {
        // select all post office

        // select all post post order
    }

    @Scheduled(cron = "*/5 * * * *")
    public void orderPostmanAssign() {
        // select all post office

        // select all post post order

        // select all postman
    }

}
