package io.babydevelopers.babybot.application.spring.discord

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller


@Controller
class ChatGPTController(
    @Value("\${chat-gpt.token}") private val gptToken: String,
) : ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        val author: User = event.author
        val message: Message = event.message

        if (author.isBot) {
            return
        }

        if (message.mentions.members.contains(event.guild.selfMember)) {
            //TODO: 이곳에 Chat-GPT 코드를 구현합니다.

            event.channel.sendMessage("안녕하세요.").queue()
        }
    }
}
