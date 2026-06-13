@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Optional<Book> findById(Long id) throws EntityNotFoundException {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + id));
    }

    public List<Book> findAll() {
        return (List<Book>) bookRepository.findAll();
    }

    public Book updateBook(Book book, Long id) throws EntityNotFoundException {
        Optional<Book> optionalBook = findById(id);
        if (!optionalBook.isPresent()) {
            throw new EntityNotFoundException("Book not found with ID: " + id);
        }
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) throws EntityNotFoundException {
        try {
            findById(id);
            bookRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Book not found with ID: " + id);
        }
    }
}