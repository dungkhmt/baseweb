package com.hust.baseweb.applications.notifications.controller;

import com.hust.baseweb.applications.notifications.model.GetNotificationsOM;
import com.hust.baseweb.applications.notifications.service.NotificationsService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/notification")
@Validated
public class NotificationController {

    private final NotificationsService notificationsService;

    /**
     * @param toUser
     * @param page
     * @param size
     * @return
     */
    @GetMapping(params = {"page", "size"})
    public ResponseEntity<?> getNotifications(
        @CurrentSecurityContext(expression = "authentication.name") String toUser,
        @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
        @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        GetNotificationsOM om = new GetNotificationsOM(
            notificationsService.getNotifications(page, size),
            notificationsService.countNumUnreadNotification(toUser));

        return ResponseEntity.ok().body(om);
    }
}
