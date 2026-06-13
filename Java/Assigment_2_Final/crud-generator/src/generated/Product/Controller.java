import com.ai.crud.entity.Product;
import com.ai.crud.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    @Autowired
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            throw new EntityNotFoundException("Product not found with ID: " + id);
        }
        return ResponseEntity.ok(optionalProduct.get());
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product entity) {
        Product savedEntity = productRepository.save(entity);
        return ResponseEntity.status(201).body(savedEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody Product entityDetails) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            throw new EntityNotFoundException("Product not found with ID: " + id);
        }
        Product existingEntity = optionalProduct.get();
        existingEntity.setName(entityDetails.getName());
        existingEntity.setPrice(entityDetails.getPrice());
        existingEntity.setCategory(entityDetails.getCategory());
        existingEntity.setStockQuantity(entityDetails.getStockQuantity());

        Product updatedEntity = productRepository.save(existingEntity);
        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            throw new EntityNotFoundException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
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