package com.hust.baseweb.service;

<<<<<<< f08dc7070064a7308996696a19e3e7fc13924f73
=======
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

>>>>>>> update edit user
import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.model.PersonModel;
import com.hust.baseweb.model.PersonUpdateModel;
import com.hust.baseweb.model.querydsl.SortAndFiltersInput;
import com.hust.baseweb.rest.user.DPerson;
import com.hust.baseweb.rest.user.UserRestBriefProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    public UserLogin findById(String userLoginId);

    public DPerson findByPartyId(String partyId);

    public Page<DPerson> findAllPerson(Pageable page, SortAndFiltersInput query);

    public Page<UserRestBriefProjection> findPersonByFullName(Pageable page, String sString);

    public List<UserLogin> getAllUserLogins();

    public UserLogin save(String userName, String password) throws Exception;
<<<<<<< f08dc7070064a7308996696a19e3e7fc13924f73

    public Party save(PersonModel personModel, String createdBy) throws Exception;
=======
    public Party save(PersonModel personModel,String createdBy) throws Exception;
    public Party update(PersonUpdateModel personUpdateModel,UUID partyId,String updateBy); 
>>>>>>> update edit user
}
