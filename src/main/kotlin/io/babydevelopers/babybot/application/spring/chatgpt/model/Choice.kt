package io.babydevelopers.babybot.application.spring.chatgpt.model

data class Choice(
    val text: String,
    val index: Int,
    val logprobs: String?,
)
