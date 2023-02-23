package io.babydevelopers.babybot.application.spring.discord.domain

import java.util.EnumSet
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.channel.concrete.Category

data class DiscordServer(private val guild: Guild) {

    fun createRoleAndChannel(name: String, categoryById: Category) = guild.createRole()
        .setName(name)
        .queue { role ->
            guild.createVoiceChannel(name)
                .setParent(categoryById)
                .addPermissionOverride(role, EnumSet.of(Permission.VIEW_CHANNEL), null)
                .addPermissionOverride(guild.publicRole, null, EnumSet.of(Permission.VIEW_CHANNEL))
                .queue()
        }

    fun deleteAllRoles(channelName: String) =
        guild.getRolesByName(channelName, true)
            .filter { it.name == channelName }
            .forEach { it.delete().queue() }

    fun getRole(channelName: String): Role = guild.getRolesByName(channelName, true)[0]

    fun addRoleToMember(memberId: Member, role: Role) {
        guild.addRoleToMember(memberId, role).queue()
    }

    fun deleteChannel(name: String, categoryById: Category) =
        categoryById.voiceChannels
            .filter { it.name == name }
            .forEach { it.delete().queue() }
}
