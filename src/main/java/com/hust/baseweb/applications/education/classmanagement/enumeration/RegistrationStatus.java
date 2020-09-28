package com.hust.baseweb.applications.education.classmanagement.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RegistrationStatus {

    WAITING_FOR_APPROVAL("Chờ phê duyệt"),
    APPROVED("Đã phê duyệt"),
    REFUSED("Đã từ chối"),
    REMOVED("Đã bị xoá"),
    NONE(null);

    private String value;

    public static RegistrationStatus of(String value) {
        for (RegistrationStatus status : values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        return NONE;
    }
}
