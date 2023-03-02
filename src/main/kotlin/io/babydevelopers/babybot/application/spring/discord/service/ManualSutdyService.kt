package io.babydevelopers.babybot.application.spring.discord.service

import io.babydevelopers.babybot.application.spring.discord.model.DiscordServer
import io.babydevelopers.babybot.application.spring.discord.repository.StudyMemberEntity
import io.babydevelopers.babybot.application.spring.discord.repository.StudyRepository
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.channel.Channel
import net.dv8tion.jda.api.entities.channel.ChannelType.GUILD_PUBLIC_THREAD
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

private val SlashCommandInteractionEvent._member: Member
    get() = member ?: error("멤버가 존재하지 않습니다.")

@Service
class ManualSutdyService(
    @Value(value = "\${discord.category-id}") private val studyCategoryId: Long,
    private val studyRepository: StudyRepository,
) {
    fun onChannelCreate(event: SlashCommandInteractionEvent) {
        require(isForum(event.channel)) { error("게시판을 통해서 승인을 해야합니다.") }

        DiscordServer(guild(event)).createRoleAndChannel(
            channelName(event.channel.name),
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

    fun onChannelEnterRequest(event: SlashCommandInteractionEvent) = event._member
        .run { studyRepository.save(StudyMemberEntity(idLong, effectiveName, channelName(event.channel.name))) }

    fun onChannelEnterApproval(event: SlashCommandInteractionEvent) {
        val channelName = channelName(event.channel.name)

        DiscordServer(guild(event)).apply {
            studyRepository.findAllByChannelName(channelName)
                .forEach { getMemberById(event, it.id, this) }
        }

        studyRepository.deleteByChannelName(channelName)
    }

    private fun getMemberById(event: SlashCommandInteractionEvent, memberId: Long, discordServer: DiscordServer) = Thread {
        Thread.sleep(1)
        guild(event)
            .loadMembers().get()
            .let { members ->
                members.firstOrNull { it.idLong == memberId }
            }
            .let {
                val member = it ?: error("해당 멤버가 존재하지 않습니다.")
                discordServer.addRoleToMember(member, discordServer.getRole(channelName(event.channel.name)))
            }
    }.start()

    private fun isForum(channel: Channel) = channel.type == GUILD_PUBLIC_THREAD

    private fun guild(event: SlashCommandInteractionEvent) = event.guild
        ?: error("서버가 존재하지 않습니다.")

    private fun channelName(channelName: String) = "[스터디] $channelName"
}
