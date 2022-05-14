package com.example.booklibrary.persistence

import com.example.booklibrary.persistence.data.BookEntry
import org.springframework.data.mongodb.core.MongoAction
import org.springframework.data.mongodb.repository.DeleteQuery
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface DataStore : MongoRepository<BookEntry, String> {
    @Query("{'bookId':?0}")
    fun findByBookId(bookId: UUID): Optional<BookEntry>

    @DeleteQuery("{'bookId':?0}")
    fun deleteById(bookId: UUID)
}