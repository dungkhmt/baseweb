package com.hust.baseweb.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.Person;
import com.hust.baseweb.entity.SecurityGroup;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.entity.PartyType.PartyTypeEnum;
import com.hust.baseweb.entity.Status.StatusEnum;
import com.hust.baseweb.model.PersonModel;
import com.hust.baseweb.model.querydsl.SearchCriteria;
import com.hust.baseweb.model.querydsl.SortAndFiltersInput;
import com.hust.baseweb.model.querydsl.SortCriteria;
import com.hust.baseweb.repo.PartyRepo;
import com.hust.baseweb.repo.PartyTypeRepo;
import com.hust.baseweb.repo.PersonRepo;
import com.hust.baseweb.repo.SecurityGroupRepo;
import com.hust.baseweb.repo.StatusRepo;
import com.hust.baseweb.repo.UserLoginRepo;
import com.hust.baseweb.rest.user.DPerson;
import com.hust.baseweb.rest.user.PredicateBuilder;
import com.hust.baseweb.rest.user.SortBuilder;
import com.hust.baseweb.rest.user.UserRestRepository;
import com.querydsl.core.types.dsl.BooleanExpression;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    public static final String module = UserService.class.getName();
    @Autowired
    private UserLoginRepo userLoginRepo;
    @Autowired
    private UserRestRepository userRestRepository;
    @Autowired
    private PartyService partyService;
    @Autowired
    private PartyTypeRepo partyTypeRepo;
    @Autowired
    private PartyRepo partyRepo;
    @Autowired
    private StatusRepo statusRepo;
    @Autowired
    private PersonRepo personRepo;
    @Autowired
    private SecurityGroupRepo securityGroupRepo;

    @Override
    public UserLogin findById(String userLoginId) {
        return userLoginRepo.findByUserLoginId(userLoginId);
    }

    public List<UserLogin> getAllUserLogins() {
        return userLoginRepo.findAll();
    }

    @Override
    @Transactional
    public UserLogin save(String userName, String password) throws Exception {
        Party p = partyService.save("PERSON");
        UserLogin ul = new UserLogin(userName, password, null, true);
        ul.setParty(p);
        if (userLoginRepo.existsById(userName)) {
            System.out.println(module + "::save, userName " + userName + " EXISTS!!!");
            throw new RuntimeException();
        }
        return userLoginRepo.save(ul);
    }

    @Override
    @Transactional
    public Party save(PersonModel personModel, String createdBy) throws Exception {
        Party party = new Party(personModel.getPartyCode(), partyTypeRepo.getOne(PartyTypeEnum.PERSON.name()), "",
                statusRepo.findById(StatusEnum.PARTY_ENABLED.name()).get(), false, userLoginRepo.getOne(createdBy));
        party = partyRepo.save(party);
        personRepo.save(new Person(party.getPartyId(), personModel.getFirstName(), personModel.getMiddleName(),
                personModel.getLastName(), personModel.getGender(), personModel.getBirthDate()));
        List<SecurityGroup> roles = new ArrayList<>();
        roles = personModel.getRoles().stream().map(r -> securityGroupRepo.findById(r).get())
                .collect(Collectors.toList());
        UserLogin ul = new UserLogin(personModel.getUserName(), personModel.getPassword(), roles, true);
        ul.setParty(party);
        if (userLoginRepo.existsById(personModel.getUserName())) {
            throw new RuntimeException();
        }
        userLoginRepo.save(ul);
        return party;
    }

    @Override
    public Page<DPerson> findAllPerson(Pageable page, SortAndFiltersInput query) {
        BooleanExpression exp = null;
        List<SearchCriteria> fNew = new ArrayList<>();

        fNew.add(new SearchCriteria("type.id", ":", PartyTypeEnum.PERSON.name()));
        if (query != null) {
            // SortCriteria [] sorts= query.getSort();
            SearchCriteria[] filters = query.getFilters();
            fNew.addAll(Arrays.asList(filters));
        }
        PredicateBuilder builder = new PredicateBuilder();
        for (SearchCriteria sc : fNew) {
            builder.with(sc.getKey(), sc.getOperation(), sc.getValue());
        }
        exp = builder.build();
        // SortBuilder driverSortBuilder = new SortBuilder();
        // for (int i = 0; i < sorts.length; i++) {
        // driverSortBuilder.add(sorts[i].getField(), sorts[i].isAsc());
        // }
        // Sort sort = driverSortBuilder.build();
        return userRestRepository.findAll(exp, page);
    }
}
