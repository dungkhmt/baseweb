package com.hust.baseweb.service;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.PartyType.PartyTypeEnum;
import com.hust.baseweb.entity.Person;
import com.hust.baseweb.entity.SecurityGroup;
import com.hust.baseweb.entity.Status.StatusEnum;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.model.PersonModel;
import com.hust.baseweb.model.PersonUpdateModel;
import com.hust.baseweb.model.querydsl.SearchCriteria;
import com.hust.baseweb.model.querydsl.SortAndFiltersInput;
import com.hust.baseweb.repo.*;
import com.hust.baseweb.rest.user.DPerson;
import com.hust.baseweb.rest.user.PredicateBuilder;
import com.hust.baseweb.rest.user.UserRestBriefProjection;
import com.hust.baseweb.rest.user.UserRestRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class UserServiceImpl implements UserService {
    public static final String module = UserService.class.getName();
    private UserLoginRepo userLoginRepo;
    private UserRestRepository userRestRepository;
    private PartyService partyService;
    private PartyTypeRepo partyTypeRepo;
    private PartyRepo partyRepo;
    private StatusRepo statusRepo;
    private PersonRepo personRepo;
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
        Party party = partyService.save("PERSON");
        UserLogin userLogin = new UserLogin(userName, password, null, true);
        userLogin.setParty(party);
        if (userLoginRepo.existsById(userName)) {
            System.out.println(module + "::save, userName " + userName + " EXISTS!!!");
            throw new RuntimeException();
        }
        return userLoginRepo.save(userLogin);
    }

    @Override
    @Transactional
    public Party save(PersonModel personModel) throws Exception {
        Party party = new Party(personModel.getPartyCode(), partyTypeRepo.getOne(PartyTypeEnum.PERSON.name()), "",
                statusRepo.findById(StatusEnum.PARTY_ENABLED.name()).orElseThrow(NoSuchElementException::new),
                false);
        party = partyRepo.save(party);
        personRepo.save(new Person(party.getPartyId(), personModel.getFirstName(), personModel.getMiddleName(),
                personModel.getLastName(), personModel.getGender(), personModel.getBirthDate()));
        List<SecurityGroup> roles = new ArrayList<>();

        log.info("save, roles = " + personModel.getRoles().size());

        roles = personModel.getRoles().stream().map(r -> securityGroupRepo.findById(r).get())
                .collect(Collectors.toList());
        UserLogin userLogin = new UserLogin(personModel.getUserName(), personModel.getPassword(), roles, true);
        userLogin.setParty(party);
        if (userLoginRepo.existsById(personModel.getUserName())) {
            throw new RuntimeException();
        }
        userLoginRepo.save(userLogin);
        return party;
    }

    @Override
    public Page<DPerson> findAllPerson(Pageable page, SortAndFiltersInput query) {
        BooleanExpression expression = null;
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
        expression = builder.build();
        // SortBuilder driverSortBuilder = new SortBuilder();
        // for (int i = 0; i < sorts.length; i++) {
        // driverSortBuilder.add(sorts[i].getField(), sorts[i].isAsc());
        // }
        // Sort sort = driverSortBuilder.build();
        return userRestRepository.findAll(expression, page);
    }

    @Override
    public Page<UserRestBriefProjection> findPersonByFullName(Pageable page, String sString) {
        return userRestRepository.findByTypeAndStatusAndFullNameLike(page, PartyTypeEnum.PERSON.name(),
                StatusEnum.PARTY_ENABLED.name(), sString);
    }

    @Override
    public DPerson findByPartyId(String partyId) {
        return userRestRepository.findById(UUID.fromString(partyId)).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Party update(PersonUpdateModel personUpdateModel, UUID partyId) {
        Person person = personRepo.getOne(partyId);
        person.setBirthDate(personUpdateModel.getBirthDate());
        person.setFirstName(personUpdateModel.getFirstName());
        person.setLastName(personUpdateModel.getLastName());
        person.setMiddleName(personUpdateModel.getMiddleName());
        personRepo.save(person);
        Party party = partyRepo.getOne(partyId);
        party.setPartyCode(personUpdateModel.getPartyCode());
        UserLogin u = party.getUserLogin();
        u.setRoles(personUpdateModel.getRoles().stream().map(r -> securityGroupRepo.getOne(r)).collect(Collectors.toList()));
        userLoginRepo.save(u);
        return partyRepo.findById(person.getPartyId()).get();
    }

    @Override
    public UserLogin findUserLoginByPartyId(UUID partyId) {
        Party party = partyService.findByPartyId(partyId);
        return userLoginRepo.findByParty(party).get(0);
    }
}
