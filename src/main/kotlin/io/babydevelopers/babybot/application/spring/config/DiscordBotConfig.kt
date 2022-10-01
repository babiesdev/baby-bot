package io.babydevelopers.babybot.application.spring.config

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity.playing
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DiscordBotConfig {
    @Bean
    fun bot(
        @Value("\${discord.token}") token: String,
        @Value("\${discord.playing-message}") message: String
    ): JDA = JDABuilder.createLight(token)
        .setActivity(playing(message))
        .build()
}
