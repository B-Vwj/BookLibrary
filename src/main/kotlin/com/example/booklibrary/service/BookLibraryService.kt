package com.example.booklibrary.service

import com.example.booklibrary.controller.data.Book
import com.example.booklibrary.persistence.DataStore
import com.example.booklibrary.persistence.data.BookEntry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.reflect.full.memberProperties

@Service
class BookLibraryService(@Autowired val bookRepository: DataStore) {
    fun add(book: Book): UUID {
        require(book.bookId == null) { ErrorMessage.INVALID_ID }
        // TODO: Semantic check goes here

        val bookEntry = book.asBookEntry(bookId = book.generateUUID())
        bookRepository.insert(bookEntry)
        return bookEntry.bookId
    }

    fun getById(id: UUID): Book {
        val bookEntry = bookRepository.findByBookId(id).orElseThrow {
            throw NoSuchElementException(ErrorMessage.NOT_FOUND)
        }
        return bookEntry.asBook()
    }

    fun getAll(): RetrievalResponse<Book> =
        bookRepository.findAll()
            .let { bookEntry ->
                RetrievalResponse(
                    bookEntry.map { it.asBook() },
                    bookEntry.size
                )
            }

    fun update(book: Book, id: UUID) {
        // TODO: Semantic check goes here
        require(book.bookId == id || book.bookId == null) { ErrorMessage.NON_MATCHING_ID }
        val dbBook = getById(id)
        val newBook = dbBook.updateProperties(book)
        bookRepository.save(newBook.asBookEntry())
    }

    fun delete(id: UUID) {
        if (bookRepository.findByBookId(id).isEmpty) {
            throw NoSuchElementException(ErrorMessage.NOT_FOUND)
        }
        bookRepository.deleteById(id)
    }


    companion object {
        /**
         * Convert [Book] object to [BookEntry] object
         * @param bookId  Null UUID of Book
         * @return [BookEntry]  BookEntry object with [Book] property values mapped over
         */
        internal fun Book.asBookEntry(
            bookId: UUID = requireNotNull(this.bookId) { ErrorMessage.NULL_ID }
        ): BookEntry = BookEntry(
            bookId,
            title,
            author,
            isbn,
            bookEdition,
            publishDate,
            numberOfPages
        )

        /**
         * Convert [BookEntry] object to [Book] object
         * @return [Book]  Book object with [BookEntry] property values mapped over
         */
        internal fun BookEntry.asBook(): Book = Book(
            bookId,
            title,
            author,
            isbn,
            bookEdition,
            publishDate,
            numberOfPages
        )

        internal fun Book.updateProperties(newBook: Book): Book = with(::Book) {
            val propsByName = Book::class.memberProperties.associateBy { it.name }
            callBy(parameters.associateWith { parameter ->
                when (parameter.name) {
                    Book::bookId.name -> newBook.bookId
                    Book::title.name -> newBook.title
                    Book::author.name -> newBook.author
                    Book::isbn.name -> newBook.isbn
                    Book::bookEdition.name -> newBook.bookEdition
                    Book::publishDate.name -> newBook.publishDate
                    Book::numberOfPages.name -> newBook.numberOfPages
                    else -> propsByName[parameter.name]?.get(this@updateProperties)
                }
            })
        }

        /**
         * Generate new [UUID] for the [Book] object
         */
        internal fun Book.generateUUID(): UUID = UUID.nameUUIDFromBytes("$title".toByteArray())

//        private val BOOK_RECORD: Book = Book(
//            id = "id",
//            bookId = UUID.nameUUIDFromBytes("Mimir".toByteArray()),
//            title = "The Wise One",
//            author = "Mimir",
//            isbn = "1234567890",
//            bookEdition = "first",
//            publishDate = "01-01-2000",
//            numberOfPages = 100,
//            status = ReadingStatus.COMPLETED
//        )
    }
}

data class RetrievalResponse<T : Any>(
    val entries: List<T>,
    val numberOfEntries: Int
)

// TODO: Move to ServiceValidator file
object ErrorMessage {
    const val NON_MATCHING_ID: String = "Item id must match provided 'id' or be null."
    const val NOT_FOUND: String = "No item was found with provided id."
    const val NULL_ID: String = "'id' cannot be null."
    const val INVALID_ID: String = "'id' cannot be set on add. Did you mean to update?"
}