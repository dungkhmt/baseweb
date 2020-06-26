package com.hust.baseweb.service;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.entity.UserRegister;
import com.hust.baseweb.model.PersonModel;
import com.hust.baseweb.model.PersonUpdateModel;
import com.hust.baseweb.model.querydsl.SortAndFiltersInput;
import com.hust.baseweb.rest.user.DPerson;
import com.hust.baseweb.rest.user.UserRestBriefProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserLogin findById(String userLoginId);

    DPerson findByPartyId(String partyId);

    Page<DPerson> findAllPerson(Pageable page, SortAndFiltersInput query);

    Page<UserRestBriefProjection> findPersonByFullName(Pageable page, String sString);

    List<UserLogin> getAllUserLogins();

    UserLogin createAndSaveUserLogin(String userName, String password);
    UserLogin updatePassword(UserLogin user,String password);

    Party createAndSaveUserLogin(PersonModel personModel) throws Exception;

    Party update(PersonUpdateModel personUpdateModel, UUID partyId);


    UserLogin findUserLoginByPartyId(UUID partyId);

    UserRegister.OutputModel registerUser(UserRegister.InputModel inputModel);

    boolean approveRegisterUser(String userLoginId);

    List<UserRegister.OutputModel> findAllRegisterUser();

}
