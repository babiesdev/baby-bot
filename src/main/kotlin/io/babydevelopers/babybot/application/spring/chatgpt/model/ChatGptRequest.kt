package io.babydevelopers.babybot.application.spring.chatgpt.model

data class ChatGptRequest(
    val model: String = "text-davinci-003",
    val prompt: String,
    val maxTokens: Int = 1000,
    val temperature: Double = 0.0,
    val topP: Double = 1.0,
) {
    init {
        require(maxTokens <= 2048) { "Chat-GPT 규격 상 2048을 초과할 수 없습니다." }
    }
}
