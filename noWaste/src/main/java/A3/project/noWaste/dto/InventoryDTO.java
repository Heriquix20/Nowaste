package A3.project.noWaste.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDTO {

    private Integer id;

    @NotBlank(message = "O nome do inventário é obrigatório")
    private String name;

    @NotNull(message = "O usuário é obrigatório")
    private Integer userId;

}
