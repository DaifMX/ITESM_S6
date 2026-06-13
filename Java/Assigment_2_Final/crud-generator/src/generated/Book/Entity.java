@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String author;
    private String isbn;
    private int publishedYear;
    private BigDecimal price;

    // Getters and Setters
}