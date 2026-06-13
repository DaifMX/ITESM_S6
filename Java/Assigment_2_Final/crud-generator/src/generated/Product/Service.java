import com.ai.crud.entity.Product;
import com.ai.crud.repository.ProductRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public Product create(Product entity) {
        return productRepository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}