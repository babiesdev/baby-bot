package io.babydevelopers.babybot.application.spring.chatgpt.model

data class Usage(
    private val promptTokens: Int,
    private val completionTokens: Int,
    private val totalTokens: Int,
)
