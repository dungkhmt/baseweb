package com.hust.baseweb.rest.user;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.model.PersonModel;
import com.hust.baseweb.model.dto.DTOPerson;
import com.hust.baseweb.model.querydsl.SearchCriteria;
import com.hust.baseweb.model.querydsl.SortAndFiltersInput;
import com.hust.baseweb.service.UserService;
import com.hust.baseweb.utils.CommonUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController
 */
// @RepositoryRestController
// @ExposesResourceFor(DPerson.class)
@RestController
public class UserController {
    public static Logger LOG = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @PostMapping(path = "/user")
    public ResponseEntity<?> save(@RequestBody PersonModel personModel, Principal principal) {
        // Resources<String> resources = new Resources<String>(producers);\\
        Party party;
        try {
            party = userService.save(personModel, principal.getName());
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        // DPerson person=
        try {
            return ResponseEntity.created(new URI("str")).body(party);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @GetMapping(path = "/users")
    public ResponseEntity<?> getUsers(Pageable page,
            @RequestParam(name = "search", required = false) String searchString,
            @RequestParam(name = "filter", required = false) String filterString) {
    	LOG.info("::getUsers, searchString = " + searchString);
    	
    	Page<DPerson> pg = userService.findAllPerson(page,null);
        List<DTOPerson> lst = new ArrayList<DTOPerson>();
        List<DPerson> lPerson = pg.getContent();
        lst = lPerson.stream().map(p -> new DTOPerson(p)).collect(Collectors.toList());
        Page<DTOPerson> dtoPerson = new PageImpl<DTOPerson>(lst, page, pg.getTotalElements());
        return ResponseEntity.ok().body(dtoPerson);
    }
    
    /*
    @GetMapping(path = "/users")
    public ResponseEntity<?> getUsers(Pageable page,
            @RequestParam(name = "filtering", required = false) String filterString) {
        SortAndFiltersInput sortAndFiltersInput = null;
        if (filterString != null) {
            String[] filterSpl = filterString.split(",");
            SearchCriteria[] searchCriterias = new SearchCriteria[filterSpl.length];
            for (int i = 0; i < filterSpl.length; i++) {
                String tmp = filterSpl[i];
                if (tmp != null) {
                    Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?)-");// (\w+?)(:|<|>)(\w+?),
                    Matcher matcher = pattern.matcher(tmp + "-");
                    while (matcher.find()) {
                        LOG.info(matcher.group(0));
                        searchCriterias[i] = new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3));
                    }
                }
            }
            sortAndFiltersInput = new SortAndFiltersInput(searchCriterias, null);
            sortAndFiltersInput = CommonUtils.rebuildQueryDsl(DTOPerson.mapPair, sortAndFiltersInput);
            LOG.info(sortAndFiltersInput.toString());
        }
        LOG.info("::getUsers, pages = " + page.toString());
        Page<DPerson> pg = userService.findAllPerson(page, sortAndFiltersInput);
        List<DTOPerson> lst = new ArrayList<DTOPerson>();
        List<DPerson> lPerson = pg.getContent();
        lst = lPerson.stream().map(p -> new DTOPerson(p)).collect(Collectors.toList());
        Page<DTOPerson> dtoPerson = new PageImpl<DTOPerson>(lst, page, pg.getTotalElements());
        return ResponseEntity.ok().body(dtoPerson);
    }
	*/
}