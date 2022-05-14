package com.example.booklibrary.persistence.data

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document("Books")
data class BookEntry(
    @Id
    val bookId: UUID,
    val title: String = "",
    val author: String = "",
    val isbn: String = "",
    val bookEdition: String = "",
    val publishDate: String = "",
    val numberOfPages: Int = 0,
//    val status: ReadingStatus = ReadingStatus.PLANNING_TO_READ
)
