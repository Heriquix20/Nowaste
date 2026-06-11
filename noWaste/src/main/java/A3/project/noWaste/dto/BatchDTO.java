package A3.project.noWaste.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
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

    private String code;

    @NotNull(message = "A quantidade não pode ser nula")
    @Min(value = 1, message = "A quantidade deve ser maior que zero")
    private Integer quantity;

    @NotNull(message = "A data de validade é obrigatoria")
    @FutureOrPresent(message = "A data de validade não pode ser no passado")
    private LocalDate expirationDate;

    private Double totalWeight;

    private Long daysToExpire;

    private String status;

}
