package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.entity.FacilityRole;
import com.hust.baseweb.applications.logistics.repo.FacilityRepo;
import com.hust.baseweb.applications.logistics.repo.FacilityRoleRepo;
import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.RoleType;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.repo.PartyRepo;
import com.hust.baseweb.repo.RoleTypeRepo;
import com.hust.baseweb.repo.UserLoginRepo;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Service
@Log4j2
public class FacilityRoleServiceImpl implements FacilityRoleService {

    private final FacilityRoleRepo facilityRoleRepo;
    private final FacilityRepo facilityRepo;
    private final UserLoginRepo userLoginRepo;
    private final RoleTypeRepo roleTypeRepo;
    private final PartyRepo partyRepo;

    public FacilityRoleServiceImpl(
        FacilityRoleRepo facilityRoleRepo,
        FacilityRepo facilityRepo,
        UserLoginRepo userLoginRepo,
        RoleTypeRepo roleTypeRepo,
        PartyRepo partyRepo
    ) {
        this.facilityRoleRepo = facilityRoleRepo;
        this.facilityRepo = facilityRepo;
        this.userLoginRepo = userLoginRepo;
        this.roleTypeRepo = roleTypeRepo;
        this.partyRepo = partyRepo;
    }

    @Override
    public List<FacilityRole.ApiOutputModel> getAll() {
        List<FacilityRole> facilityRoles = facilityRoleRepo.findAll();

        return facilityRoles.stream().map(buildToApiOutputModelFunction(facilityRoles)).collect(Collectors.toList());
    }

    @Override
    public Page<FacilityRole.ApiOutputModel> getAll(Pageable pageable) {
        Page<FacilityRole> facilityRolePage = facilityRoleRepo.findAll(pageable);
        return facilityRolePage.map(buildToApiOutputModelFunction(facilityRolePage.getContent()));
    }

    @Override
    public List<FacilityRole.ApiOutputModel> getAllByFacilityId(String facilityId) {
        List<FacilityRole> facilityRoles = facilityRoleRepo
            .findAllByFacility(facilityRepo.findById(facilityId).orElseThrow(NoSuchElementException::new));

        return facilityRoles
            .stream()
            .map(buildToApiOutputModelFunction(facilityRoles))
            .collect(Collectors.toList());
    }

    @Override
    public List<FacilityRole.ApiOutputModel> getAllFacilitySalesman(String facilityId) {
        RoleType roleType = roleTypeRepo.findByRoleTypeId("SALESMAN_SELL_FROM_FACILITY");
        Facility facility = facilityRepo.findByFacilityId(facilityId);
        List<FacilityRole> facilityRoles = facilityRoleRepo
            .findAllByFacilityAndRoleTypeAndThruDate(facility, roleType, null);
        if (facilityRoles == null) {
            return null;
        }

        return facilityRoles
            .stream()
            .map(buildToApiOutputModelFunction(facilityRoles))
            .collect(Collectors.toList());
    }

    /**
     * Xây dựng hàm chuyển đổi từ facility roles thành api output model
     *
     * @param facilityRoles danh sách data tham gia xây dựng hàm
     * @return hàm chuyển đổi
     */
    @NotNull
    private Function<FacilityRole, FacilityRole.ApiOutputModel> buildToApiOutputModelFunction(List<FacilityRole> facilityRoles) {
        Map<String, UUID> userLoginIdToPartyId = facilityRoles
            .stream()
            .map(FacilityRole::getUserLogin)
            .distinct()
            .collect(Collectors.toMap(
                UserLogin::getUserLoginId,
                userLogin -> userLogin.getParty().getPartyId(),
                (o, n) -> o));

        Map<UUID, Party> partyMap = partyRepo
            .findAllByPartyIdIn(userLoginIdToPartyId.values().stream().distinct().collect(Collectors.toList()))
            .stream()
            .collect(Collectors.toMap(Party::getPartyId, p -> p, (o, n) -> o));

        return facilityRole -> facilityRole.toOutputApiModel(
            partyMap.get(userLoginIdToPartyId.get(facilityRole.getUserLogin().getUserLoginId())).getName());
    }

    @Override
    public FacilityRole.ApiOutputModel create(FacilityRole.ApiInputModel inputModel) {
        Date now = new Date();

        Facility facility = facilityRepo
            .findById(inputModel.getFacilityId())
            .orElseThrow(NoSuchElementException::new);
        UserLogin userLogin = userLoginRepo
            .findById(inputModel.getUserLoginId())
            .orElseThrow(NoSuchElementException::new);

        RoleType roleType = roleTypeRepo.findByRoleTypeId("SALESMAN_SELL_FROM_FACILITY");

        List<FacilityRole> facilityRoles = facilityRoleRepo.findAllByFacilityAndUserLoginAndRoleTypeAndThruDate(
            facility,
            userLogin,
            roleType,
            null);
        if (facilityRoles != null && facilityRoles.size() > 0) {
            FacilityRole facilityRole = facilityRoles.get(0);
            log.info("create, facilityRole exists!!! with " +
                     facilityRole.getFacilityRoleId() +
                     ", thruDate = " +
                     facilityRole.getThruDate());
            return buildToApiOutputModelFunction(Collections.singletonList(facilityRole)).apply(facilityRole);
        }

        //FacilityRole facilityRole = facilityRoleRepo.findAllByFacilityAndUserLogin(facility, userLogin);
        //if (facilityRole != null) {
        //    return buildToApiOutputModelFunction(Collections.singletonList(facilityRole)).apply(facilityRole);
        //}

        FacilityRole newFacilityRole = new FacilityRole();
        newFacilityRole.setUserLogin(userLogin);
        newFacilityRole.setFacility(facility);

        newFacilityRole.setFromDate(now);
        newFacilityRole.setRoleType(roleTypeRepo
                                        .findById("SALESMAN_SELL_FROM_FACILITY")
                                        .orElseThrow(NoSuchElementException::new));

        newFacilityRole = facilityRoleRepo.save(newFacilityRole);
        return buildToApiOutputModelFunction(Collections.singletonList(newFacilityRole)).apply(newFacilityRole);
    }

    @Override
    public boolean delete(String facilityRoleId) {
        Optional<FacilityRole> facilityRoleOptional = facilityRoleRepo.findById(UUID.fromString(facilityRoleId));
        if (facilityRoleOptional.isPresent()) {
            //facilityRoleRepo.delete(facilityRoleOptional.get());
            FacilityRole facilityRole = facilityRoleOptional.get();
            facilityRole.setThruDate(new Date());
            facilityRoleRepo.save(facilityRole);
            return true;
        }
        return false;
    }
}
