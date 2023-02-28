package io.babydevelopers.babybot.application.spring.discord.listener

import io.babydevelopers.babybot.application.spring.discord.service.ManualForumListener
import io.babydevelopers.babybot.domain.SlashCommand
import io.babydevelopers.babybot.domain.SlashCommand.*
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Controller

private fun SlashCommandInteractionEvent.sendMessage(message: String) = reply(message).queue()
private val SlashCommandInteractionEvent._member: Member
    get() = member ?: error("멤버가 존재하지 않습니다.")
private val SlashCommandInteractionEvent.hasAdminRole: Boolean
    get() = _member.hasPermission(net.dv8tion.jda.api.Permission.ADMINISTRATOR)

@Controller
class CommandStudyController(
    private val manualForumListener: ManualForumListener,
) : ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val hasAdminRole = event.hasAdminRole

        if (!hasAdminRole) {
            event.sendMessage("권한이 없습니다.")
            return
        }

        when (SlashCommand.from(event.name)) {
            ADMISSION -> createChannel(event)

            DELETE -> {
                manualForumListener.onChannelDelete(event)
                event.sendMessage("${event.channel.name} 보이스 채널이 삭제되었습니다.")
            }

            ENTER -> enterChannel(event)
        }
    }

    private fun enterChannel(event: SlashCommandInteractionEvent) {
        try {
            manualForumListener.onChannelEnter(event)
            event.sendMessage("${event._member.effectiveName}님이 ${event.channel.name} 스터디에 참여하였습니다.")
        } catch (e: IllegalArgumentException) {
            event.sendMessage(e.message.toString())
        }
    }

    private fun createChannel(event: SlashCommandInteractionEvent) {
        try {
            manualForumListener.onChannelCreate(event)
            event.sendMessage("보이스 채널을 생성하였습니다.")
        } catch (e: IllegalArgumentException) {
            event.sendMessage(e.message.toString())
        }
    }
}
