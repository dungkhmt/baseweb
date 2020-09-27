package com.hust.baseweb.applications.education.entity;

import com.hust.baseweb.applications.education.classmanagement.enumeration.RegistrationStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "edu_class_registration")
@EntityListeners(AuditingEntityListener.class)
public class ClassRegistration {

    @EmbeddedId
    private ClassRegistrationId id;

    private RegistrationStatus status;

    @LastModifiedDate
    private Date lastUpdatedStamp;

    @CreatedDate
    private Date createdStamp;
}
