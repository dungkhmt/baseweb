package com.hust.baseweb.rest.user;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.model.PersonModel;
import com.hust.baseweb.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController
 */
//@RepositoryRestController
//@ExposesResourceFor(DPerson.class)
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(path = "/user")
    public ResponseEntity<?> save(@RequestBody PersonModel personModel, Principal principal) {
        // Resources<String> resources = new Resources<String>(producers);\\
        Party party;
        try {
            party = userService.save(personModel, principal.getName());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        // DPerson person=
        try {
            return ResponseEntity.created(new URI("str")).body(party);
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    
}