package io.babydevelopers.babybot.application.spring.discord

import io.babydevelopers.babybot.application.spring.chatgpt.ChatGptClient
import io.babydevelopers.babybot.application.spring.chatgpt.model.ChatGptRequest
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Controller

@Controller
class ChatGPTController(
    private val chatGptClient: ChatGptClient,
) : ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        val author: User = event.author
        val message: Message = event.message

        if (author.isBot) {
            return
        }

        if (message.mentions.members.contains(event.guild.selfMember)) {
            val req = ChatGptRequest(prompt = message.contentRaw)

            val message = runBlocking { chatGptClient.sendMessage(req).block() }
            event.channel.sendMessage(message.text).queue()
        }
    }
}
