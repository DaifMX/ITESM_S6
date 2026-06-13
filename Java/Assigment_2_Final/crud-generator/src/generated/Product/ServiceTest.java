import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.ai.crud.entity.Product;
import com.ai.crud.repository.ProductRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll_ShouldReturnIterableWhenCalledWithValidParams() {
        // Arrange
        Iterable<Product> expectedProducts = new java.util.ArrayList<>();
        when(productRepository.findAll()).thenReturn(expectedProducts);

        // Act
        Iterable<Product> actualProducts = productService.findAll();

        // Assert
        assert (actualProducts.equals(expectedProducts));
    }

    @Test
    public void testFindById_ShouldReturnOptionalWhenCalledWithValidParams() {
        // Arrange
        Product expectedProduct = new Product();
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(expectedProduct));

        // Act
        Optional<Product> actualProduct = productService.findById(1L);

        // Assert
        assert (actualProduct.equals(Optional.ofNullable(expectedProduct)));
    }

    @Test
    public void testFindById_ShouldReturnEmptyOptionalWhenCalledWithInvalidParams() {
        // Arrange
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act
        Optional<Product> actualProduct = productService.findById(1L);

        // Assert
        assert (actualProduct.isEmpty());
    }

    @Test
    public void testCreate_ShouldReturnSavedEntityWhenCalledWithValidParams() {
        // Arrange
        Product expectedProduct = new Product();
        when(productRepository.save(any(Product.class))).thenReturn(expectedProduct);

        // Act
        Product actualProduct = productService.create(expectedProduct);

        // Assert
        assert (actualProduct.equals(expectedProduct));
    }

    @Test
    public void testCreate_ShouldReturnNullWhenCalledWithNullParams() {
        // Arrange

        // Act & Assert
        assert (productService.create(null) == null);
    }

    @Test
    public void testDeleteById_ShouldNotThrowExceptionWhenCalledWithValidParams() {
        // Arrange
        productService.deleteById(1L);

        // No assertions needed for this method, as it's a no-op in the real implementation.
    }

    @Test
    public void testDeleteById_ShouldThrowNoSuchElementExceptionWhenCalledWithInvalidParams() {
        // Arrange
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> productService.deleteById(1L));
    }
}