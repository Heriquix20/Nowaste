package A3.project.noWaste.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchDTO {

    private Integer id;

    @NotBlank(message = "O código do lote e obrigatório")
    private String code;

    @NotNull(message = "A quantidade e obrigatoria")
    @Min(value = 1, message = "A quantidade deve ser maior que zero")
    private Integer quantity;

    @NotNull(message = "A data de validade e obrigatoria")
    @FutureOrPresent(message = "A data de validade nao pode ser no passado")
    private LocalDate expirationDate;

    private Double totalWeight;

    @NotNull(message = "O produto e obrigatorio")
    private Integer productId;
}
