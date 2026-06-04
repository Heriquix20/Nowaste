package A3.project.noWaste.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Integer id;

    @NotBlank(message = "O nome do produto é obrigatório")
    private String name;

    @NotNull(message = "O peso do produto é obrigatório")
    @Positive(message = "O peso do produto deve ser maior que zero")
    private Double weight;

    @NotBlank(message = "A categoria do produto é obrigatória")
    private String category;

    @NotBlank(message = "A marca do produto é obrigatória")
    private String brand;

    private String weightUnit;
}
