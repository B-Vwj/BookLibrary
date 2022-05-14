package com.example.booklibrary.controller.data

import java.util.UUID

data class Book(
    val bookId: UUID? = null,
    val title: String = "",
    val author: String = "",
    val isbn: String = "",
    val bookEdition: String = "",
    val publishDate: String = "",
    val numberOfPages: Int = 0,
//    val status: ReadingStatus = ReadingStatus.PLANNING_TO_READ
)
