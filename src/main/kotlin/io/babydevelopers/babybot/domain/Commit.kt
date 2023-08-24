package io.babydevelopers.babybot.domain

import java.time.LocalDate
import java.time.LocalDateTime

data class Commit(
    val message: String,
    val createdAt: LocalDateTime,
) {
    fun isEqualDate(date: LocalDate) =
        createdAt.toLocalDate() == date
}
