package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.entity.FacilityRole;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface FacilityRoleRepo extends JpaRepository<FacilityRole, UUID> {

    @NotNull
    Page<FacilityRole> findAll(@NotNull Pageable pageable);

    List<FacilityRole> findAllByFacility(Facility facility);
}
