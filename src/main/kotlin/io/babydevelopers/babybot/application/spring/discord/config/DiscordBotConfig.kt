package io.babydevelopers.babybot.application.spring.discord.config

import io.babydevelopers.babybot.application.spring.discord.listener.CommandStudyController
import io.babydevelopers.babybot.application.spring.discord.listener.MentionChatGPTController
import io.babydevelopers.babybot.domain.SlashCommand.ADMISSION
import io.babydevelopers.babybot.domain.SlashCommand.DELETE
import io.babydevelopers.babybot.domain.SlashCommand.ENTER
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity.playing
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DiscordBotConfig(
    private val commandStudyController: CommandStudyController,
) {
    @Bean
    fun bot(
        @Value("\${discord.token}") token: String,
        @Value("\${discord.playing-message}") message: String,
        mentionChatGPTController: MentionChatGPTController,
    ): JDA = JDABuilder.createLight(token)
        .setActivity(playing(message))
        .setAutoReconnect(true)
        .addEventListeners(mentionChatGPTController)
        .addEventListeners(commandStudyController)
        .build()
        .also {
            it.upsertCommand(admissionCommand).queue()
            it.upsertCommand(deleteCommand).queue()
            it.upsertCommand(enterCommand).queue()
        }

    val admissionCommand = CommandDataImpl(ADMISSION.command, "approval command")
    val deleteCommand = CommandDataImpl(DELETE.command, "보이스 채널 삭제")
    val enterCommand = CommandDataImpl(ENTER.command, "스터디 참여")
}
