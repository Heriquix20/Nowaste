package A3.project.noWaste.bdd.steps;

import A3.project.noWaste.config.TokenConfig;
import A3.project.noWaste.domain.Batch;
import A3.project.noWaste.domain.Inventory;
import A3.project.noWaste.domain.Product;
import A3.project.noWaste.domain.User;
import A3.project.noWaste.bdd.support.BDDContext;
import A3.project.noWaste.infra.BatchRepository;
import A3.project.noWaste.infra.InventoryRepository;
import A3.project.noWaste.infra.ProductRepository;
import A3.project.noWaste.infra.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import io.cucumber.java.pt.Dado;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class CommonSteps {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private TokenConfig tokenConfig;

    @Autowired
    private BDDContext context;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Dado("que existe um usuário autenticado")
    public void queExisteUmUsuarioAutenticado() {
        User user = new User();
        user.setName("Usuario BDD");
        user.setEmail("bdd@nowaste.com");
        user.setPassword("123456");
        user = userRepository.save(user);

        context.setUser(user);
        context.setToken(tokenConfig.generateToken(user));
    }

    @Dado("que o usuário possui um inventário cadastrado")
    public void queOUsuarioPossuiUmInventarioCadastrado() {
        Inventory inventory = new Inventory();
        inventory.setName("Despensa");
        inventory.setDescription("Inventario de teste");
        inventory.setLocation("Cozinha");
        inventory.setUser(context.getUser());
        inventory = inventoryRepository.save(inventory);

        context.setInventory(inventory);
    }

    @Dado("que o usuário possui um produto {string} com peso {double} gramas")
    public void queOUsuarioPossuiUmProdutoComPesoEmGramas(String nome, Double peso) {
        Product product = new Product();
        product.setName(nome);
        product.setCategory("Graos");
        product.setBrand("Marca Teste");
        product.setWeightInGrams(peso);
        product.setInventory(context.getInventory());
        product = productRepository.save(product);

        context.setProduct(product);
    }

    @Dado("que existe um lote desse produto com quantidade {int} e validade em {int} dias")
    public void queExisteUmLoteDesseProdutoComQuantidadeEValidadeEmDias(Integer quantidade, Integer dias) {
        Batch batch = new Batch();
        batch.setCode("MANUAL-" + System.nanoTime());
        batch.setQuantity(quantidade);
        batch.setExpirationDate(LocalDate.now().plusDays(dias));
        batch.setProduct(context.getProduct());
        batchRepository.save(batch);
    }

    @Dado("que existe um lote vencido desse produto com quantidade {int}")
    public void queExisteUmLoteVencidoDesseProdutoComQuantidade(Integer quantidade) {
        jdbcTemplate.update(
                "insert into batches (code, quantity, expiration_date, product_id) values (?, ?, ?, ?)",
                "EXP-" + System.nanoTime(),
                quantidade,
                LocalDate.now().minusDays(2),
                context.getProduct().getId()
        );
    }

    @Dado("que existe um lote desse produto com quantidade {int} e validade ainda neste mês")
    public void queExisteUmLoteDesseProdutoComQuantidadeEValidadeAindaNesteMes(Integer quantidade) {
        LocalDate hoje = LocalDate.now();
        LocalDate dataNoMesAtual = hoje.withDayOfMonth(hoje.lengthOfMonth());

        Batch batch = new Batch();
        batch.setCode("MES-" + System.nanoTime());
        batch.setQuantity(quantidade);
        batch.setExpirationDate(dataNoMesAtual);
        batch.setProduct(context.getProduct());
        batchRepository.save(batch);
    }
}