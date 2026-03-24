package A3.project.noWaste.service;

import A3.project.noWaste.config.JWTUserData;
import A3.project.noWaste.config.TokenConfig;
import A3.project.noWaste.domain.User;
import A3.project.noWaste.exceptions.ObjectNotFoundException;
import A3.project.noWaste.infra.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Service
public class VerificationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenConfig tokenConfig;


    // Pegar token do cabeçalho Authorization
    public String processarComAuthorization() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        } else {
            throw new RuntimeException("Valor do cabeçalho é nulo");
        }
    }

    public Integer pegarIdUsuario() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        JWTUserData userData = (JWTUserData) auth.getPrincipal();

        return userRepository.findByEmail(userData.getEmail())
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"))
                .getId();
    }

    // verificar usuario
    public User verifyUser() {
        Integer userId = pegarIdUsuario();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));
        return user;
    }
}
