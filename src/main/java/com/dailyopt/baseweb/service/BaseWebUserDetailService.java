package com.dailyopt.baseweb.service;

import com.dailyopt.baseweb.entity.UserLogin;
import com.dailyopt.baseweb.repo.UserLoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class BaseWebUserDetailService implements UserDetailsService {
    @Autowired
    private UserLoginRepo userLoginRepo;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserLogin user = userLoginRepo.findByUserLoginId(s);
        return new User(user.getUserLoginId(), user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRoles().stream().map(sg->{
                    return sg.getId();
                }).toArray(String[]::new)));
    }
}
