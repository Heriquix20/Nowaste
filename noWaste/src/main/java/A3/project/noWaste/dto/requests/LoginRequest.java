package A3.project.noWaste.dto.requests;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(@NotEmpty(message = "Email é obrigatório") String email,
                           @NotEmpty(message = "Senha é obrigatória") String password) {

}
