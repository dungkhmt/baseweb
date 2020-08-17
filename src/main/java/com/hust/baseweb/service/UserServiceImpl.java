package com.hust.baseweb.service;

import com.hust.baseweb.entity.*;
import com.hust.baseweb.entity.PartyType.PartyTypeEnum;
import com.hust.baseweb.entity.Status.StatusEnum;
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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
@Transactional
@javax.transaction.Transactional
public class UserServiceImpl implements UserService {

    public static final String module = UserService.class.getName();
    private final UserLoginRepo userLoginRepo;
    private final UserRestRepository userRestRepository;
    private final PartyService partyService;
    private final PartyTypeRepo partyTypeRepo;
    private final PartyRepo partyRepo;
    private final StatusRepo statusRepo;
    private final PersonRepo personRepo;
    private final SecurityGroupRepo securityGroupRepo;
    private final UserRegisterRepo userRegisterRepo;
    private final StatusItemRepo statusItemRepo;
    private final JavaMailSender javaMailSender;

    private final static ExecutorService EMAIL_EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

    @Override
    public UserLogin findById(String userLoginId) {
        return userLoginRepo.findByUserLoginId(userLoginId);
    }

    public List<UserLogin> getAllUserLogins() {
        return userLoginRepo.findAll();
    }

    @Override
    @Transactional
    public UserLogin createAndSaveUserLogin(String userName, String password) {

        Party party = partyService.save("PERSON");
        UserLogin userLogin = new UserLogin(userName, password, null, true);
        userLogin.setParty(party);
        if (userLoginRepo.existsById(userName)) {
//            System.out.println(module + "::save, userName " + userName + " EXISTS!!!");
            throw new EntityExistsException("userLoginId = " + userLogin.getUserLoginId() + ", already exist!");
        }
        return userLoginRepo.save(userLogin);
    }

    @Override
    public UserLogin updatePassword(UserLogin user, String password) {
        user.setPassword(password);
        return userLoginRepo.save(user);
    }

    @Override
    @Transactional
    public Party createAndSaveUserLogin(PersonModel personModel) {

        Party party = new Party(personModel.getPartyCode(), partyTypeRepo.getOne(PartyTypeEnum.PERSON.name()), "",
                                statusRepo
                                    .findById(StatusEnum.PARTY_ENABLED.name())
                                    .orElseThrow(NoSuchElementException::new),
                                false);
        party = partyRepo.save(party);
        personRepo.save(new Person(party.getPartyId(), personModel.getFirstName(), personModel.getMiddleName(),
                                   personModel.getLastName(), personModel.getGender(), personModel.getBirthDate()));

        Set<SecurityGroup> roles = securityGroupRepo.findAllByGroupIdIn(personModel.getRoles());

        log.info("save, roles = " + personModel.getRoles().size());

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

        u.setRoles(securityGroupRepo.findAllByGroupIdIn(personUpdateModel.getRoles()));

        userLoginRepo.save(u);
        return partyRepo.findById(person.getPartyId()).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public UserLogin findUserLoginByPartyId(UUID partyId) {
        Party party = partyService.findByPartyId(partyId);
        return userLoginRepo.findByParty(party).get(0);
    }

    @Override
    public UserRegister.OutputModel registerUser(UserRegister.InputModel inputModel) {
        String userLoginId = inputModel.getUserLoginId();
        String email = inputModel.getEmail();

        if (userRegisterRepo.existsByUserLoginIdOrEmail(userLoginId, email) || userLoginRepo.existsById(userLoginId)) {
            return new UserRegister.OutputModel();
        }
        StatusItem userRegistered = statusItemRepo
            .findById("USER_REGISTERED")
            .orElseThrow(NoSuchElementException::new);
        UserRegister userRegister = inputModel.createUserRegister(userRegistered);
        userRegister = userRegisterRepo.save(userRegister);

        EMAIL_EXECUTOR_SERVICE.execute(() -> sendEmail(email, userLoginId));

        return userRegister.toOutputModel();
    }

    private void sendEmail(String email, String userLoginId) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);

        simpleMailMessage.setSubject("Đăng ký thành công - SSCM - Quản lý chuỗi cung ứng");
        simpleMailMessage.setText(String.format(
            "Bạn đã đăng ký thành công tài khoản tại hệ thống với tên đăng nhập %s, " +
            "vui lòng chờ cho đến khi được quản trị viên phê duyệt. \nXin cảm ơn!",
            userLoginId));
        javaMailSender.send(simpleMailMessage);
    }

    @Override
    public boolean approveRegisterUser(String userLoginId) {
        UserRegister userRegister = userRegisterRepo.findById(userLoginId).orElse(null);
        if (userRegister == null) {
            return false;
        }

        try {
//            createAndSaveUserLogin(userRegister.getUserLoginId(), userRegister.getPassword());

            try {
                createAndSaveUserLogin(new PersonModel(
                        userRegister.getUserLoginId(),
                        userRegister.getPassword(),
                        new ArrayList<>(),
                        userRegister.getUserLoginId(),
                        userRegister.getFirstName(),
                        userRegister.getLastName(),
                        userRegister.getMiddleName(),
                        null,
                        null));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            return false;
        }
        StatusItem userApproved = statusItemRepo.findById("USER_APPROVED").orElseThrow(NoSuchElementException::new);
        userRegister.setStatusItem(userApproved);
        userRegisterRepo.save(userRegister);
        return true;
    }

    @Override
    public List<UserRegister.OutputModel> findAllRegisterUser() {

        StatusItem userRegistered = null;
        try {
            userRegistered = statusItemRepo
                    .findById("USER_REGISTERED")
                    .orElseThrow(NoSuchElementException::new);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userRegistered != null) {
            List<UserRegister> userRegisters = userRegisterRepo.findAllByStatusItem(userRegistered);
            return userRegisters.stream().map(UserRegister::toOutputModel).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }


        /*
        StatusItem userRegistered = statusItemRepo
            .findById("USER_REGISTERED")
            .orElseThrow(NoSuchElementException::new);
        List<UserRegister> userRegisters = userRegisterRepo.findAllByStatusItem(userRegistered);
        return userRegisters.stream().map(UserRegister::toOutputModel).collect(Collectors.toList());
        */

    }

}
