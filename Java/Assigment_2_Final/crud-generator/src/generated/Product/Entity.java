@Entity
@Table(name = "products")
public class Product {
    @Id
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(nullable = false)
    private BigDecimal price;
    
    @Column(nullable = false)
    private String category;
    
    @Column(nullable = false)
    private Integer stockQuantity;

    // Getters and Setters
}