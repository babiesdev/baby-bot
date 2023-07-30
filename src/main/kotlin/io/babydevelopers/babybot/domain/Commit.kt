package io.babydevelopers.babybot.domain

import java.time.LocalDateTime

data class Commit(
    val message: String,
    val createdAt: LocalDateTime,
)
