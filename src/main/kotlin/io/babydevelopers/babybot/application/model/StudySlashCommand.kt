package io.babydevelopers.babybot.application.model

import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import net.dv8tion.jda.internal.interactions.CommandDataImpl

enum class StudySlashCommand(val data: SlashCommandData) {
    ADMISSION(CommandDataImpl("승인", "스터디 승인")),
    DELETE(CommandDataImpl("삭제", "보이스 채널 삭제")),
    ENTER(CommandDataImpl("참여", "스터디 참여 요청")),
    APPROVAL(CommandDataImpl("참여승인", "스터디 참여자 승인")),
    ;

    val command: String
        get() = data.name

    companion object {
        infix fun from(command: String): StudySlashCommand =
            StudySlashCommand.values().firstOrNull { it.command == command }
                ?: error("존재하지 않는 명령어입니다.")
    }
}
