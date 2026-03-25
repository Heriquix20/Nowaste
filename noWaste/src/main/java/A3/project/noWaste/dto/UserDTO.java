package A3.project.noWaste.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Integer id;

    @NotBlank(message = "O username é obrigatório")
    private String Username;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email digitado inválido")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Senha não pode ser nula")
    private String password;

}
