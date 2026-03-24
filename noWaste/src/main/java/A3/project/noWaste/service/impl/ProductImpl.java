package A3.project.noWaste.service.impl;

import A3.project.noWaste.domain.Product;
import A3.project.noWaste.dto.ProductDTO;
import A3.project.noWaste.infra.ProductRepository;
import A3.project.noWaste.service.ProductService;
import A3.project.noWaste.exceptions.DataIntegratyViolationException;
import A3.project.noWaste.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ModelMapper mapper;


    // Buscar produto por ID
    @Override
    public Product findById(Integer id) {
        Optional<Product> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Produto não encontrado"));
    }

    // Listar todos os produtos
    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }

    // Criar produto
    @Override
    public Product create(ProductDTO obj) {
        checkByName(obj);
        Product product = mapper.map(obj, Product.class);
        return repository.save(product);
    }

    // Atualizar produto
    @Override
    public Product update(ProductDTO obj) {
        checkByName(obj);
        Product product = mapper.map(obj, Product.class);
        return repository.save(product);
    }

    // Deletar produto
    @Override
    public void delete(Integer id) {
        Product product = findById(id);
        repository.delete(product);
    }

    // Verificação de duplicidade pelo nome
    private void checkByName(ProductDTO obj) {
        Optional<Product> product = repository.findByName(obj.getName());
        if (product.isPresent() && !product.get().getId().equals(obj.getId())) {
            throw new DataIntegratyViolationException("Produto já cadastrado no sistema");
        }
    }
}
