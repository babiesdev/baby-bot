package io.babydevelopers.babybot.application.spring.mangjoo.controller

import io.babydevelopers.babybot.application.spring.mangjoo.service.ManualForumListener
import net.dv8tion.jda.api.Permission.ADMINISTRATOR
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class SlashCommand(
    private val manualForumListener: ManualForumListener = ManualForumListener(
        1075960627949993984L,
        877172746117853257L
    )
) : ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        when (event.name) {
            "승인" -> {
                if (event.member?.hasPermission(ADMINISTRATOR)!!) {
                    event.guild?.createRole()?.setName("[스터디] " + event.channel.name)?.queue() // 스터디 채널에 대한 역할 생성
                    createChannel(event)
                    event.reply("보이스 채널을 생성하였습니다.").queue()
                }
            }

            "삭제" -> {
                if (event.member?.hasPermission(ADMINISTRATOR)!!) {
                    manualForumListener.onChannelDelete(event.jda, event.channel)
                    event.guild?.getRolesByName("[스터디] " + event.channel.name, true)?.get(0)?.delete()?.queue()
                    event.reply("${event.channel.name}이 삭제되었습니다.").queue()
                }
            }

            "참여" -> {
                val role = event.guild?.getRolesByName("[스터디] " + event.channel.name, true)?.get(0)
                event.guild?.addRoleToMember(event.member!!, role!!)?.queue()
                event.reply("${event.member?.effectiveName}님이 ${event.channel.name}에 참여하였습니다.").queue()
            }
        }
    }

    private fun createChannel(event: SlashCommandInteractionEvent) {
        try {
            manualForumListener.onChannelCreate(event.jda, event.channel) // 채널 생성
        } catch (e: IllegalArgumentException) {
            event.reply(e.message.toString()).queue()
        }
    }

}