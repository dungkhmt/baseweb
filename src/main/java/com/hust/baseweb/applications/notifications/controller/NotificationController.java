package com.hust.baseweb.applications.notifications.controller;

import com.hust.baseweb.applications.notifications.model.GetNotificationsOM;
import com.hust.baseweb.applications.notifications.model.UpdateMultipleNotificationStatusBody;
import com.hust.baseweb.applications.notifications.service.NotificationsService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Map;
import java.util.UUID;

import static com.hust.baseweb.applications.notifications.entity.Notifications.STATUS_READ;
import static com.hust.baseweb.applications.notifications.service.NotificationsService.SSE_EVENT_HEARTBEAT;
import static com.hust.baseweb.applications.notifications.service.NotificationsService.subscriptions;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/notification")
@Validated
public class NotificationController {

    private final NotificationsService notificationsService;

    /**
     * @param toUser
     * @return
     */
    @GetMapping("/subscription")
    public SseEmitter events(@CurrentSecurityContext(expression = "authentication.name") String toUser) {
//        log.info(toUser + " subscribes at " + getCurrentDateTime());

        if (subscriptions.containsKey(toUser)) {
            return subscriptions.get(toUser);
        } else {
            SseEmitter subscription = new SseEmitter(Long.MAX_VALUE);
            Runnable callback = () -> subscriptions.remove(toUser);

            subscription.onTimeout(callback); // OK
            subscription.onCompletion(callback); // OK
            subscription.onError((exception) -> { // Must consider carefully, but currently OK
                subscriptions.remove(toUser);
                log.info("onError fired with exception: " + exception);
            });

            subscriptions.put(toUser, subscription);
            return subscription;
        }
    }

    /**
     * To keep connection alive
     */
    @Async
    @Scheduled(fixedRate = 40000)
    public void sendHeartbeatSignal() {
        subscriptions.forEach((toUser, subscription) -> {
            try {
                subscription.send(SseEmitter.event().name(SSE_EVENT_HEARTBEAT).data("keep alive"));
//                log.info("SENT HEARBEAT SIGNAL AT: " + getCurrentDateTime());
            } catch (Exception e) {
                // Currently, nothing need be done here
            }
        });
    }

    /**
     * @param toUser
     * @param page
     * @param size
     */
    @GetMapping(params = {"page", "size"})
    public ResponseEntity<?> getNotifications(
        @CurrentSecurityContext(expression = "authentication.name") String toUser,
        @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
        @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        GetNotificationsOM om = new GetNotificationsOM(
            notificationsService.getNotifications(toUser, page, size),
            notificationsService.countNumUnreadNotification(toUser));

        return ResponseEntity.ok().body(om);
    }

    /**
     * Update status of notifications whose id equal {@code id}.
     *
     * @param id   notification id
     * @param body request body
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable UUID id, @RequestBody Map<String, Object> body) {
        Object value = body.get("status");
        String status = null == value ? null : value.toString();

        if (!STATUS_READ.equals(status)) {
            return ResponseEntity.badRequest().body("Invalid status");
        } else {
            notificationsService.updateStatus(id, status);
            return ResponseEntity.ok().body(null);
        }
    }

    /**
     * Update multiple notifications status of current log in user.
     * Currently used for marking all notifications as read.
     *
     * @param userId
     * @param body   request body
     */
    @PatchMapping("/status")
    public ResponseEntity<?> updateMultipleNotificationsStatus(
        @CurrentSecurityContext(expression = "authentication.name") String userId,
        @RequestBody UpdateMultipleNotificationStatusBody body
    ) {
        if (!STATUS_READ.equals(body.getStatus())) {
            return ResponseEntity.badRequest().body("Invalid status");
        } else {
            notificationsService.updateMultipleNotificationsStatus(
                userId,
                STATUS_READ,
                body.getBeforeOrAt());
            return ResponseEntity.ok().body(null);
        }
    }
}
