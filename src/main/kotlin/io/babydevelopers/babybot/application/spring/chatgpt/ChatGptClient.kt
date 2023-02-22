package io.babydevelopers.babybot.application.spring.chatgpt

import io.babydevelopers.babybot.application.spring.chatgpt.model.ChatGptRequest
import io.babydevelopers.babybot.application.spring.chatgpt.model.ChatGptResponse
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange
import reactor.core.publisher.Mono

@HttpExchange(
    accept = ["application/json"],
    contentType = "application/json",
)
interface ChatGptClient {
    @PostExchange("/v1/completions")
    fun sendMessage(@RequestBody requestParam: ChatGptRequest): Mono<ChatGptResponse>
}
