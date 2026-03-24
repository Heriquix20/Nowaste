package A3.project.noWaste.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDTO {

    private Integer Id;

    @NotBlank(message = "O nome do inventário é obrigatório")
    private String name;

}
