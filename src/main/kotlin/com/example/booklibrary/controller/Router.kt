package com.example.booklibrary.controller

import com.example.booklibrary.controller.data.Book
import com.example.booklibrary.service.BookLibraryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/library/")
class Router(@Autowired val bookLibraryService: BookLibraryService) {
    @PostMapping(Routes.POST_QUESTION)
    fun addBook(@RequestBody book: Book): ResponseEntity<UUID> {
        return bookLibraryService.add(book)
            .let { id -> ItemId(id) }
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it.id) }
    }

    @GetMapping(Routes.GET_QUESTION_BY_ID)
    fun getBookById(@PathVariable id: UUID): ResponseEntity<Book> =
        ResponseEntity.ok(bookLibraryService.getById(id))

    @GetMapping(Routes.GET_QUESTIONS)
    fun getBooks(): ResponseEntity<List<Book>> =
        ResponseEntity.ok(bookLibraryService.getAll().entries)

    @PutMapping(Routes.PUT_QUESTION)
    fun updateBook(@RequestBody book: Book, @PathVariable id: UUID): ResponseEntity<String> {
        return bookLibraryService.update(book, id).let {
            ResponseEntity.status(HttpStatus.NO_CONTENT).body("")
        }
    }

    @DeleteMapping(Routes.DELETE_QUESTION)
    fun deleteBook(@PathVariable id: UUID): ResponseEntity<String> {
        return bookLibraryService.delete(id)
            .let { ResponseEntity.status(HttpStatus.NO_CONTENT).body("") }
    }
}

data class ItemId(
    val id: UUID
)

object Routes {
    const val ROUTE_PREFIX: String = "/books"
    const val POST_QUESTION: String = ROUTE_PREFIX
    const val GET_QUESTIONS: String = ROUTE_PREFIX
    const val GET_QUESTION_BY_ID: String = "$ROUTE_PREFIX/{id}"
    const val PUT_QUESTION: String = "$ROUTE_PREFIX/{id}"
    const val DELETE_QUESTION: String = "$ROUTE_PREFIX/{id}"
}