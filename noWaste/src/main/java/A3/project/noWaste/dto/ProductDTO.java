package A3.project.noWaste.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Integer Id;

    @NotBlank(message = "O nome do produto é obrigatório")
    private String name;

    @NotBlank(message = "A categoria é obrigatória")
    private String category;

    private String brand;
}
