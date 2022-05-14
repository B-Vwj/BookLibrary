package com.example.booklibrary

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EntityScan
@EnableMongoRepositories("com.example.booklibrary.persistence")
@ComponentScan(
    "com.example.booklibrary.controller",
    "com.example.booklibrary.controller.data",
    "com.example.booklibrary.service",
    "com.example.booklibrary.persistence"
)
class BookLibraryApplication

fun main(args: Array<String>) {
    runApplication<BookLibraryApplication>(*args)
}
