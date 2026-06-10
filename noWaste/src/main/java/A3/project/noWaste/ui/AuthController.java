package A3.project.noWaste.ui;


import A3.project.noWaste.config.TokenConfig;
import A3.project.noWaste.domain.User;
import A3.project.noWaste.dto.requests.LoginRequest;
import A3.project.noWaste.dto.responses.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager manager;
    private final TokenConfig tokenConfig;

    public AuthController(AuthenticationManager manager, TokenConfig tokenConfig) {
        this.manager = manager;
        this.tokenConfig = tokenConfig;
    }

    // user login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication authentication = manager.authenticate(userAndPass);

        User user = (User) authentication.getPrincipal();
        String token = tokenConfig.generateToken(user);

        return ResponseEntity.ok(new LoginResponse(token));
    }

}
