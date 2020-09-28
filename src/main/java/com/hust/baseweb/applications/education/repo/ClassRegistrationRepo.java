package com.hust.baseweb.applications.education.repo;

import com.hust.baseweb.applications.education.entity.ClassRegistration;
import com.hust.baseweb.applications.education.entity.ClassRegistrationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRegistrationRepo extends JpaRepository<ClassRegistration, ClassRegistrationId> {
}
