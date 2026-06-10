package A3.project.noWaste.bdd.support;

import A3.project.noWaste.domain.Inventory;
import A3.project.noWaste.domain.Product;
import A3.project.noWaste.domain.User;
import com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.spring.ScenarioScope;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
@Getter
@Setter
public class BDDContext {

    private User user;
    private String token;
    private Inventory inventory;
    private Product product;

    private int responseStatus;
    private JsonNode responseBody;
}