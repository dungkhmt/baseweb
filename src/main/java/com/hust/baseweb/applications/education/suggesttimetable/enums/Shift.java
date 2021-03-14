package com.hust.baseweb.applications.education.suggesttimetable.enums;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public enum Shift {
    MORNING("SÁNG"),
    AFTERNOON("CHIỀU"),
    EVENING("TỐI");

    private final String description;

    Shift(String description) {
        this.description = description;
    }

    public static Shift of(@NotNull String shift) {
        if (null == shift) {
            throw new IllegalArgumentException(
                "No shift with description \"null\" found, shifts can only be \"SÁNG\", \"CHIỀU\", \"TỐI\"");
        }

        String normalizeShift = StringUtils.deleteWhitespace(shift).toUpperCase();
        switch (normalizeShift) {
            case "SÁNG":
                return MORNING;
            case "CHIỀU":
                return AFTERNOON;
            case "TỐI":
                return EVENING;
            default:
                throw new IllegalArgumentException("No shift with description \"" +
                                                   shift +
                                                   "\" found, shifts can only be \"SÁNG\", \"CHIỀU\", \"TỐI\"");
        }
    }
}
