package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.FacilityRole;
import com.hust.baseweb.applications.logistics.repo.FacilityRepo;
import com.hust.baseweb.applications.logistics.repo.FacilityRoleRepo;
import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.repo.PartyRepo;
import com.hust.baseweb.repo.RoleTypeRepo;
import com.hust.baseweb.repo.UserLoginRepo;
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
        FacilityRole facilityRole = new FacilityRole();
        Date now = new Date();

        facilityRole.setFacility(facilityRepo
                                     .findById(inputModel.getFacilityId())
                                     .orElseThrow(NoSuchElementException::new));
        facilityRole.setFromDate(now);
        facilityRole.setUserLogin(userLoginRepo
                                      .findById(inputModel.getUserLoginId())
                                      .orElseThrow(NoSuchElementException::new));
        facilityRole.setRoleType(roleTypeRepo
                                     .findById("SALESMAN_SELL_FROM_FACILITY")
                                     .orElseThrow(NoSuchElementException::new));

        facilityRole = facilityRoleRepo.save(facilityRole);
        return buildToApiOutputModelFunction(Collections.singletonList(facilityRole)).apply(facilityRole);
    }

    @Override
    public boolean delete(String facilityRoleId) {
        Optional<FacilityRole> facilityRoleOptional = facilityRoleRepo.findById(UUID.fromString(facilityRoleId));
        if (facilityRoleOptional.isPresent()) {
            facilityRoleRepo.delete(facilityRoleOptional.get());
            return true;
        }
        return false;
    }
}
