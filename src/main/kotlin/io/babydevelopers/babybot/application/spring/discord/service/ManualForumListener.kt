package io.babydevelopers.babybot.application.spring.discord.service

import io.babydevelopers.babybot.application.spring.discord.domain.DiscordServer
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.channel.Channel
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ManualForumListener(
    @Value(value = "\${discord.category-id}") private val studyCategoryId: Long,
) {
    fun onChannelCreate(event: SlashCommandInteractionEvent) {
        require(isForum(event.channel)) { error("게시판을 통해서 승인을 해야합니다.") }

        DiscordServer(guild(event)).createRoleAndChannel("[스터디] ${event.channel.name}", category(event))
    }

    fun onChannelDelete(event: SlashCommandInteractionEvent) {
        val discordServer = DiscordServer(guild(event))
        val channelName = "[스터디] ${event.channel.name}"

        discordServer.apply {
            deleteAllRoles(channelName)
            deleteChannel(channelName, category(event))
        }
    }

    private fun category(event: SlashCommandInteractionEvent) = event.jda.getCategoryById(studyCategoryId) ?: error("해당 카테고리가 존재하지 않습니다.")

    fun onChannelEnter(event: SlashCommandInteractionEvent) {
        val discordServer = DiscordServer(guild(event))
        val member = event.member ?: error("멤버가 존재하지 않습니다.")

        runCatching {
            discordServer.getRole("[스터디] ${event.channel.name}")
                .let { role -> discordServer.addRoleToMember(member, role) }
        }.onFailure { throw IllegalArgumentException("이미 참여했습니다.") }
    }

    private fun isForum(channel: Channel) = channel.type == ChannelType.GUILD_PUBLIC_THREAD

    private fun guild(event: SlashCommandInteractionEvent): Guild {
        return event.guild ?: error("서버가 존재하지 않습니다.")
    }
}
