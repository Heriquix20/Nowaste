package A3.project.noWaste.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
public class CucumberSpringConfiguration {
}