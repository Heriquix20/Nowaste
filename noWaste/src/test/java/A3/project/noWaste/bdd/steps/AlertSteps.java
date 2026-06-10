package A3.project.noWaste.bdd.steps;

import A3.project.noWaste.bdd.support.BDDContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class AlertSteps {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private BDDContext context;

    @Quando("ele consulta os alertas de lotes vencidos")
    public void eleConsultaOsAlertasDeLotesVencidos() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/alerts/expired")
                        .header("Authorization", "Bearer " + context.getToken())
        ).andReturn();

        salvarResposta(result);
    }

    @Quando("ele consulta os alertas de lotes do mês atual")
    public void eleConsultaOsAlertasDeLotesDoMesAtual() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/alerts/month")
                        .header("Authorization", "Bearer " + context.getToken())
        ).andReturn();

        salvarResposta(result);
    }

    @Entao("a resposta de alertas deve ser {int}")
    public void aRespostaDeAlertasDeveSer(Integer status) {
        assertEquals(status.intValue(), context.getResponseStatus());
    }

    @Entao("a lista de alertas vencidos deve conter pelo menos {int} item")
    public void aListaDeAlertasVencidosDeveConterPeloMenosItem(Integer quantidade) {
        assertTrue(context.getResponseBody().isArray());
        assertTrue(context.getResponseBody().size() >= quantidade);
    }

    @Entao("a lista de alertas do mês atual deve conter pelo menos {int} item")
    public void aListaDeAlertasDoMesAtualDeveConterPeloMenosItem(Integer quantidade) {
        assertTrue(context.getResponseBody().isArray());
        assertTrue(context.getResponseBody().size() >= quantidade);
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