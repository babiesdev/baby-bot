package io.babydevelopers.babybot.application.spring.discord.listener

import io.babydevelopers.babybot.application.spring.chatgpt.ChatGptClient
import io.babydevelopers.babybot.application.spring.chatgpt.model.ChatGptRequest
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Controller

@Controller
class MentionChatGPTController(
    private val chatGptClient: ChatGptClient,
) : ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        val (message, author) = event.run { message to author }

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
