package A3.project.noWaste.service;

import A3.project.noWaste.config.JWTUserData;
import A3.project.noWaste.config.TokenConfig;
import A3.project.noWaste.domain.User;
import A3.project.noWaste.exceptions.UserNotFoundException;
import A3.project.noWaste.infra.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class VerificationService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final TokenConfig tokenConfig;


    public VerificationService(UserRepository userRepository, TokenConfig tokenConfig) {
        this.userRepository = userRepository;
        this.tokenConfig = tokenConfig;
    }


    public Integer getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getPrincipal() == null) {
            throw new UserNotFoundException("Usuário não autenticado");
        }
        JWTUserData userData = (JWTUserData) auth.getPrincipal();
        return userData.getUserId();
    }

    public User verifyUser() {
        Integer userId = getUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
    }
}

