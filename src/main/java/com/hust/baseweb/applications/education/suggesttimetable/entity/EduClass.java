package com.hust.baseweb.applications.education.suggesttimetable.entity;

import com.hust.baseweb.applications.education.suggesttimetable.enums.EShift;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.time.DayOfWeek;
import java.util.List;

@Getter
@Document(collection = "class")
public class EduClass {

    @Id
    private BigInteger id;

    @NotNull
    private final Integer classId;

    @NotNull
    private final Integer attachedClassId;

    @NotNull
    private final String courseId;

    @NotNull
    private final Integer credit;

    @Nullable
    private final String note;

    @NotNull
    private final DayOfWeek dayOfWeek;

    @NotNull
    private final Integer startTime;

    @NotNull
    private final Integer endTime;

    @NotNull
    private final EShift shift;

    @NotNull
    private final String weeks;

    @Transient
    @Getter(value = AccessLevel.NONE)
    private List<Integer> weeksList = null;

    @NotNull
    private final String room;

    private final boolean needExperiment;

    @Nullable
    private final Integer numRegistration;

    @Nullable
    private final Integer maxQuantity;

    @NotNull
    private final String status, classType, managementId;

    public EduClass(
        @NotNull Integer classId,
        @NotNull Integer attachedClassId,
        @NotNull String courseId,
        @NotNull Integer credit,
        @Nullable String note,
        @NotNull DayOfWeek dayOfWeek,
        @NotNull Integer startTime,
        @NotNull Integer endTime,
        @NotNull EShift shift,
        @NotNull String weeks,
        @NotNull String room,
        boolean needExperiment,
        @Nullable Integer numRegistration,
        @Nullable Integer maxQuantity,
        @NotNull String status,
        @NotNull String classType,
        @NotNull String managementId
    ) {
        this.classId = classId;
        this.attachedClassId = attachedClassId;
        this.courseId = courseId;
        this.credit = credit;
        this.note = note;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.shift = shift;
        this.weeks = weeks;
        this.room = room;
        this.needExperiment = needExperiment;
        this.numRegistration = numRegistration;
        this.maxQuantity = maxQuantity;
        this.status = status;
        this.classType = classType;
        this.managementId = managementId;
    }

    private List<Integer> convertWeeksToList() {
        // TODO by: datpd
        return null;
    }

    public List<Integer> getWeeksList() {
        if (null == weeksList) {
            weeksList = this.convertWeeksToList();
        }

        return weeksList;
    }

    /**
     * Check the time overlap of 2 classes
     *
     * @param eduClass
     * @return {@code true} if 2 classes overlap, {@code false} if otherwise
     */
    public boolean overlap(@Nullable EduClass eduClass) {
        // TODO by: datpd
        if (null == eduClass) {
            return false;
        }

        if (this == eduClass) {
            return true;
        }

        if (this.endTime < eduClass.getStartTime() ||
            this.startTime > eduClass.getEndTime()) { // Time does not overlap.
            return false;
        } else {
            // Check common elements of 2 list of weeks
        }

        return false;
    }
}
