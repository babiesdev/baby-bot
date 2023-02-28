package io.babydevelopers.babybot.application.spring.discord.model

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.channel.concrete.Category
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel
import java.util.*

private fun String.isSameName(role: Role) = this == role.name
private fun String.isSameName(role: VoiceChannel) = this == role.name
private fun Role.deleteAt(): Unit = delete().queue()
private fun VoiceChannel.deleteAt(): Unit = delete().queue()

data class DiscordServer(private val guild: Guild) {

    fun createRoleAndChannel(name: String, categoryById: Category) {
        guild.createRole()
            .setName(name)
            .queue { role -> channelAction(name, categoryById, role).queue() }
    }

    fun deleteAllRoles(channelName: String) =
        guild.getRolesByName(channelName, true)
            .filter(channelName::isSameName)
            .forEach(Role::deleteAt)

    fun getRole(channelName: String): Role {
        val roles = guild.getRolesByName(channelName, true)

        require(roles.isNotEmpty()) { "'$channelName' 이름의 채널이 존재하지 않습니다." }

        return roles.first()
    }

    fun addRoleToMember(memberId: Member, role: Role) {
        guild.addRoleToMember(memberId, role).queue()
    }

    fun deleteChannel(name: String, categoryById: Category) =
        categoryById.voiceChannels
            .filter(name::isSameName)
            .forEach(VoiceChannel::deleteAt)

    private fun channelAction(
        name: String,
        categoryById: Category,
        role: Role,
    ) = guild.createVoiceChannel(name)
        .setParent(categoryById)
        .addPermissionOverride(role, EnumSet.of(Permission.VIEW_CHANNEL), null)
        .addPermissionOverride(guild.publicRole, null, EnumSet.of(Permission.VIEW_CHANNEL))
}
