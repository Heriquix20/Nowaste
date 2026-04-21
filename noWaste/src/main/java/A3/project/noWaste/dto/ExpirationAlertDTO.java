package A3.project.noWaste.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpirationAlertDTO {

    // inventario
    private Integer inventoryId;
    private String inventoryName;

    // produto
    private Integer productId;
    private String productName;

    // lote
    private Integer batchId;
    private String batchCode;

    // atributos importantes do lote
    private Integer quantity;
    private Double totalWeight;

    // alerta
    private LocalDate expirationDate;
    private Long daysToExpire;
    private String status;
}
