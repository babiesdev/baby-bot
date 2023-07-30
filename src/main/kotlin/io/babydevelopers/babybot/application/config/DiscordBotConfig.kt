package io.babydevelopers.babybot.application.config

import io.babydevelopers.babybot.application.model.StudySlashCommand
import io.babydevelopers.babybot.endpoint.discord.CommandStudyController
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity.playing
import net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MEMBERS
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
    ): JDA = JDABuilder.createLight(token)
        .setActivity(playing(message))
        .setAutoReconnect(true)
        .enableIntents(GUILD_MEMBERS)
        .addEventListeners(commandStudyController)
        .build()
        .also { jda ->
            StudySlashCommand.values()
                .forEach { jda.upsertCommand(it.data).queue() }
        }
}
