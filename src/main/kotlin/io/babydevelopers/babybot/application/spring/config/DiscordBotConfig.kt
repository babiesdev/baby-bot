package io.babydevelopers.babybot.application.spring.config

import io.babydevelopers.babybot.application.spring.discord.ChatGPTController
import io.babydevelopers.babybot.application.spring.discord.controller.SlashCommand
import io.babydevelopers.babybot.application.spring.discord.controller.SlashEnum.ADMISSION
import io.babydevelopers.babybot.application.spring.discord.controller.SlashEnum.DELETE
import io.babydevelopers.babybot.application.spring.discord.controller.SlashEnum.ENTER
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity.playing
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DiscordBotConfig(
    private val slashCommand: SlashCommand,
) {
    @Bean
    fun bot(
        @Value("\${discord.token}") token: String,
        @Value("\${discord.playing-message}") message: String,
        chatGPTController: ChatGPTController,
    ): JDA = JDABuilder.createLight(token)
        .setActivity(playing(message))
        .setAutoReconnect(true)
        .addEventListeners(chatGPTController)
        .addEventListeners(slashCommand)
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
