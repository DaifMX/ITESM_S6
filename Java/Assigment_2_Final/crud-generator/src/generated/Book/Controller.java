@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book createdBook = bookService.createBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findById(@PathVariable Long id) throws EntityNotFoundException {
        Book foundBook = bookService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + id));
        return ResponseEntity.ok(foundBook);
    }

    @GetMapping
    public ResponseEntity<List<Book>> findAll() {
        List<Book> books = bookService.findAll();
        return ResponseEntity.ok(books);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) throws EntityNotFoundException {
        Book updatedBook = bookService.updateBook(bookDetails, id);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) throws EntityNotFoundException {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}