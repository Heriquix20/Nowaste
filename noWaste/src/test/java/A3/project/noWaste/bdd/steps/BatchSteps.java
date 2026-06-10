package A3.project.noWaste.bdd.steps;

import A3.project.noWaste.bdd.support.BDDContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class BatchSteps {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private BDDContext context;

    @Quando("ele cadastra um lote com quantidade {int} e validade em {int} dias")
    public void eleCadastraUmLote(Integer quantidade, Integer dias) throws Exception {
        Map<String, Object> body = Map.of(
                "quantity", quantidade,
                "expirationDate", LocalDate.now().plusDays(dias).toString()
        );

        MvcResult result = mockMvc.perform(
                post("/inventories/" + context.getInventory().getId()
                        + "/products/" + context.getProduct().getId() + "/batches")
                        .header("Authorization", "Bearer " + context.getToken())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
        ).andReturn();

        salvarResposta(result);
    }

    @Quando("ele consulta os lotes do produto")
    public void eleConsultaOsLotesDoProduto() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/inventories/" + context.getInventory().getId()
                        + "/products/" + context.getProduct().getId() + "/batches")
                        .header("Authorization", "Bearer " + context.getToken())
        ).andReturn();

        salvarResposta(result);
    }

    @Entao("a resposta do lote deve ser {int}")
    public void aRespostaDoLoteDeveSer(Integer status) {
        assertEquals(status.intValue(), context.getResponseStatus());
    }

    @Entao("o lote retornado deve ter código {string}")
    public void oLoteRetornadoDeveTerCodigo(String codigo) {
        assertEquals(codigo, context.getResponseBody().get("code").asText());
    }

    @Entao("o primeiro lote retornado deve ter status {string}")
    public void oPrimeiroLoteRetornadoDeveTerStatus(String status) {
        assertEquals(status, context.getResponseBody().get(0).get("status").asText());
    }

    private void salvarResposta(MvcResult result) throws Exception {
        context.setResponseStatus(result.getResponse().getStatus());

        String json = result.getResponse().getContentAsString();
        JsonNode body = json == null || json.isBlank()
                ? objectMapper.readTree("{}")
                : objectMapper.readTree(json);

        context.setResponseBody(body);
    }
}