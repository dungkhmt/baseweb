package hust.baseweb.baseweb;

import hust.baseweb.baseweb.entity.SecurityPermission;
import hust.baseweb.baseweb.entity.UserLogin;
import hust.baseweb.baseweb.model.GetSecurityPermission;
import hust.baseweb.baseweb.model.GetUserLogin;
import hust.baseweb.baseweb.repository.SecurityPermissionRepository;
import hust.baseweb.baseweb.repository.UserLoginRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
class LoginResponse {
    private final GetUserLogin userLogin;
    private final List<String> securityPermissions;
}

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {
    private UserLoginRepository userLoginRepository;
    private SecurityPermissionRepository securityPermissionRepository;

    @PostMapping("/api/login")
    public LoginResponse login(Principal principal) {
        GetUserLogin userLogin = userLoginRepository.getUserLoginByUsername(principal.getName());
        List<GetSecurityPermission> permissions = securityPermissionRepository.getAllByUserId(userLogin.getId());
        return new LoginResponse(
                userLogin,
                permissions.stream()
                        .map(GetSecurityPermission::getName)
                        .collect(Collectors.toList())
        );
    }
}
