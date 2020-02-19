package com.hust.baseweb.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.model.PersonModel;
import com.hust.baseweb.model.querydsl.SortAndFiltersInput;
import com.hust.baseweb.rest.user.DPerson;
import com.hust.baseweb.rest.user.UserRestBriefProjection;

public interface UserService {
    public UserLogin findById(String userLoginId);
    public DPerson findByPartyId(String partyId);
    public Page<DPerson> findAllPerson(Pageable page, SortAndFiltersInput query);
    public Page<UserRestBriefProjection> findPersonByFullName(Pageable page, String sString);
    public List<UserLogin> getAllUserLogins();
    public UserLogin save(String userName, String password) throws Exception;
    public Party save(PersonModel personModel,String createdBy) throws Exception;
}
