@Service
@Transactional
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @ExtendWith(MockitoExtension.class)
    public static class MockitoExtension {}

    @Test
    void testCreateBookHappyPath() {
        Book book = new Book();
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        Book result = bookService.createBook(book);
        assertEquals(book, result);
    }

    @Test
    void testFindByIdNullInputThrowsException() {
        Long id = null;
        assertThrows(EntityNotFoundException.class, () -> bookService.findById(id));
    }

    @Test
    void testFindAllHappyPath() {
        List<Book> books = new ArrayList<>();
        when(bookRepository.findAll()).thenReturn(books);
        List<Book> result = bookService.findAll();
        assertEquals(books, result);
    }

    @Test
    void testUpdateBookHappyPath() {
        Book updatedBook = new Book();
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.of(new Book()));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);
        Book result = bookService.updateBook(updatedBook, id);
        assertEquals(updatedBook, result);
    }

    @Test
    void testDeleteBookHappyPath() {
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.of(new Book()));
        bookService.deleteBook(id);
        verify(bookRepository).deleteById(id);
    }

    @Test
    void testCreateBookNullInputThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> bookService.createBook(null));
    }

    @Test
    void testFindByIdNotFoundReturnsEntityNotFoundException() throws EntityNotFoundException {
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        Book result = bookService.findById(id);
        assertEquals(result, Optional.of(new Book()));
    }

    @Test
    void testUpdateBookNotFoundThrowsException() throws EntityNotFoundException {
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bookService.updateBook(new Book(), id));
    }

    @Test
    void testDeleteBookNotFoundThrowsException() throws EntityNotFoundException {
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bookService.deleteBook(id));
    }
}