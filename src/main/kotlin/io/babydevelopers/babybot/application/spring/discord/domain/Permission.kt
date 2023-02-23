package io.babydevelopers.babybot.application.spring.discord.domain

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member

data class Permission(
    val hasPermission: Boolean,
) {
    constructor(member: Member) : this(
        member.hasPermission(Permission.ADMINISTRATOR),
    )
}
