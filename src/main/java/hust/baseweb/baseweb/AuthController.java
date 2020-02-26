package hust.baseweb.baseweb;

import hust.baseweb.baseweb.entity.SecurityPermission;
import hust.baseweb.baseweb.entity.UserLogin;
import hust.baseweb.baseweb.repository.SecurityPermissionRepository;
import hust.baseweb.baseweb.repository.UserLoginRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@Getter
@RequiredArgsConstructor
class LoginResponse {
    private final UserLogin userLogin;
    private final List<SecurityPermission> securityPermissions;
}

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {
    private UserLoginRepository userLoginRepository;
    private SecurityPermissionRepository securityPermissionRepository;

    @PostMapping("/api/login")
    public LoginResponse login(Principal principal) {
        UserLogin userLogin = userLoginRepository.findByUsername(principal.getName());
        List<SecurityPermission> permissions = securityPermissionRepository.getAllByUserId(userLogin.getId());
        return new LoginResponse(userLogin, permissions);
    }

    @GetMapping("/api/test")
    public String test() {
        return "test string";
    }

}
