package A3.project.noWaste.bdd.steps;

import A3.project.noWaste.bdd.support.BDDContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class ProductSteps {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private BDDContext context;

    @Quando("ele cadastra um produto com nome {string} categoria {string} marca {string} peso {double} unidade {string}")
    public void eleCadastraUmProduto(String nome, String categoria, String marca, Double peso, String unidade) throws Exception {
        Map<String, Object> body = Map.of(
                "name", nome,
                "category", categoria,
                "brand", marca,
                "weight", peso,
                "weightUnit", unidade
        );

        MvcResult result = mockMvc.perform(
                post("/inventories/" + context.getInventory().getId() + "/products")
                        .header("Authorization", "Bearer " + context.getToken())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
        ).andReturn();

        salvarResposta(result);
    }

    @Quando("ele consulta produtos com minWeight {int} e maxWeight {int}")
    public void eleConsultaProdutosComMinWeightEMaxWeight(Integer minWeight, Integer maxWeight) throws Exception {
        MvcResult result = mockMvc.perform(
                get("/inventories/" + context.getInventory().getId() + "/products")
                        .header("Authorization", "Bearer " + context.getToken())
                        .param("minWeight", String.valueOf(minWeight))
                        .param("maxWeight", String.valueOf(maxWeight))
        ).andReturn();

        salvarResposta(result);
    }

    @Entao("a resposta do produto deve ser {int}")
    public void aRespostaDoProdutoDeveSer(Integer status) {
        assertEquals(status.intValue(), context.getResponseStatus());
    }

    @Entao("o produto retornado deve ter peso {int} gramas")
    public void oProdutoRetornadoDeveTerPesoGramas(Integer peso) {
        assertEquals(peso.doubleValue(), context.getResponseBody().get("weight").asDouble());
    }

    @Entao("o produto retornado deve ter unidade {string}")
    public void oProdutoRetornadoDeveTerUnidade(String unidade) {
        assertEquals(unidade, context.getResponseBody().get("weightUnit").asText());
    }

    @Entao("a mensagem de erro deve conter {string}")
    public void aMensagemDeErroDeveConter(String trecho) {
        assertTrue(context.getResponseBody().get("error").asText().toLowerCase().contains(trecho.toLowerCase()));
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