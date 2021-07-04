package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.entity.FacilityRole;
import com.hust.baseweb.entity.RoleType;
import com.hust.baseweb.entity.UserLogin;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface FacilityRoleRepo extends JpaRepository<FacilityRole, UUID> {

    @NotNull
    Page<FacilityRole> findAll(@NotNull Pageable pageable);

    List<FacilityRole> findAllByFacility(Facility facility);

    FacilityRole findAllByFacilityAndUserLogin(Facility facility, UserLogin userLogin);

    List<FacilityRole> findAllByFacilityAndRoleTypeAndThruDate(Facility facility, RoleType roleType, Date thruDate);

    List<FacilityRole> findAllByFacilityAndUserLoginAndRoleTypeAndThruDate(
        Facility facility,
        UserLogin userLogin,
        RoleType roleType,
        Date thruDate
    );
}
