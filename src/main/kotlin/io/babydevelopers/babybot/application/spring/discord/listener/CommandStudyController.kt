package io.babydevelopers.babybot.application.spring.discord.listener

import io.babydevelopers.babybot.application.spring.discord.model.Permission
import io.babydevelopers.babybot.application.spring.discord.service.ManualForumListener
import io.babydevelopers.babybot.domain.SlashCommand.ADMISSION
import io.babydevelopers.babybot.domain.SlashCommand.Companion.from
import io.babydevelopers.babybot.domain.SlashCommand.DELETE
import io.babydevelopers.babybot.domain.SlashCommand.ENTER
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Controller

@Controller
class CommandStudyController(
    private val manualForumListener: ManualForumListener,
) : ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val member = event.member ?: error("멤버가 존재하지 않습니다.")
        val permission = Permission(member)
        when (from(event.name)) {
            ADMISSION -> {
                if (permission.hasPermission) {
                    createChannel(event)
                } else {
                    event.reply("권한이 없습니다.").queue()
                }
            }

            DELETE -> {
                if (permission.hasPermission) {
                    manualForumListener.onChannelDelete(event)
                        .also { event.reply("${event.channel.name}이 삭제되었습니다.").queue() }
                }
            }

            ENTER -> {
                enterChannel(event)
            }
        }
    }

    private fun enterChannel(event: SlashCommandInteractionEvent) {
        val member = event.member ?: error("멤버가 존재하지 않습니다.")
        try {
            manualForumListener.onChannelEnter(event)
            event.reply("${member.effectiveName}님이 ${event.channel.name}에 참여하였습니다.").queue()
        } catch (e: IllegalArgumentException) {
            event.reply(e.message.toString()).queue()
        }
    }

    private fun createChannel(event: SlashCommandInteractionEvent) {
        try {
            manualForumListener.onChannelCreate(event) // 채널 생성
            event.reply("보이스 채널을 생성하였습니다.").queue()
        } catch (e: IllegalArgumentException) {
            event.reply(e.message.toString()).queue()
        }
    }
}
