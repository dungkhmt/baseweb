package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.FacilityRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface FacilityRoleService {

    List<FacilityRole.ApiOutputModel> getAll();

    Page<FacilityRole.ApiOutputModel> getAll(Pageable pageable);

    List<FacilityRole.ApiOutputModel> getAllByFacilityId(String facilityId);

    FacilityRole.ApiOutputModel create(FacilityRole.ApiInputModel inputModel);

    boolean delete(String facilityRoleId);
}
