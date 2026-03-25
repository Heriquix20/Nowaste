package A3.project.noWaste.domain;

import jakarta.persistence.*;
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
@Entity
@Table(name = "batches")
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String code;

    @NotNull(message = "A quantidade e obrigatoria")
    @Min(value = 1, message = "A quantidade deve ser maior que zero")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "A data de validade e obrigatoria")
    @FutureOrPresent(message = "A data de validade nao pode ser no passado")
    @Column(nullable = false)
    private LocalDate expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Transient
    private Double totalWeight;

    public Double getTotalWeight() {
        if (product == null || product.getWeight() == null || quantity == null) {
            return 0.0;
        }
        return product.getWeight() * quantity;
    }
}
