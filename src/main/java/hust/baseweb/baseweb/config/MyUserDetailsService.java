package hust.baseweb.baseweb.config;

import hust.baseweb.baseweb.entity.SecurityPermission;
import hust.baseweb.baseweb.entity.UserLogin;
import hust.baseweb.baseweb.repository.SecurityPermissionRepository;
import hust.baseweb.baseweb.repository.UserLoginRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MyUserDetailsService implements UserDetailsService {
    private SecurityPermissionRepository securityPermissionRepository;
    private UserLoginRepository userLoginRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserLogin user = userLoginRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("UserLogin not found");
        }

        List<GrantedAuthority> grantedAuthorities = securityPermissionRepository
                .getAllByUserId(user.getId())
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toList());

        return new UserPrincipal(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
