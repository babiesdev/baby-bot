package io.babydevelopers.babybot.application.spring.chatgpt.model

import java.time.LocalDate

data class ChatGptResponse(
    val id: String,
    val `object`: String,
    val created: LocalDate,
    val model: String,
    val choices: List<Choice>,
    val usage: Usage,
) {
    val text: String
        get() = choices.first().text
}
