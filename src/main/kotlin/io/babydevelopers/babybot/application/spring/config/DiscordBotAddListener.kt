package io.babydevelopers.babybot.application.spring.config

import io.babydevelopers.babybot.application.spring.mangjoo.controller.SlashCommand
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.context.annotation.Configuration

@Configuration
class DiscordBotAddListener : BeanPostProcessor {
    override fun postProcessAfterInitialization(bean: Any, beanName: String) = bean.also {
        if (it is JDA) {
            it.upsertCommand(admissionCommand).queue()
            it.upsertCommand(deleteCommand).queue()
            it.upsertCommand(enterCommand).queue()
//            it.addEventListener(Authorization())
            it.addEventListener(SlashCommand())
        }
    }

    val admissionCommand = CommandDataImpl("승인", "approval command")
    val deleteCommand = CommandDataImpl("삭제", "보이스 채널 삭제")
    val enterCommand = CommandDataImpl("참여", "스터디 참여")
}