package io.babydevelopers.babybot.application.spring.discord.listener

import io.babydevelopers.babybot.application.spring.discord.model.DiscordServer
import net.dv8tion.jda.api.entities.channel.Channel
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ManualForumController(
    @Value(value = "\${discord.category-id}") private val studyCategoryId: Long,
) {
    fun onChannelCreate(event: SlashCommandInteractionEvent) {
        require(isForum(event.channel)) { error("게시판을 통해서 승인을 해야합니다.") }

        DiscordServer(guild(event)).createRoleAndChannel(
            channelName(event.channel.name)
        )(category(event))
    }

    fun onChannelDelete(event: SlashCommandInteractionEvent) {
        val discordServer = DiscordServer(guild(event))
        val channelName = channelName(event.channel.name)

        discordServer.apply {
            deleteAllRoles(channelName)
            deleteChannel(channelName, category(event))
        }
    }

    private fun category(event: SlashCommandInteractionEvent) =
        event.jda.getCategoryById(studyCategoryId)
            ?: error("해당 카테고리가 존재하지 않습니다.")

    fun onChannelEnter(event: SlashCommandInteractionEvent) {
        val discordServer = DiscordServer(guild(event))
        val member = event.member
            ?: error("멤버가 존재하지 않습니다.")

        runCatching {
            val role = discordServer.getRole(channelName(event.channel.name))
            discordServer.addRoleToMember(member, role)
        }.onFailure { throw IllegalArgumentException("이미 참여했습니다.") }
    }

    private fun isForum(channel: Channel) = channel.type == ChannelType.GUILD_PUBLIC_THREAD

    private fun guild(event: SlashCommandInteractionEvent) = event.guild
        ?: error("서버가 존재하지 않습니다.")

    private fun channelName(channelName: String) = "[스터디] $channelName"
}
