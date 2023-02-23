package io.babydevelopers.babybot.domain

enum class SlashCommand(val command: String) {
    ADMISSION("승인"),
    DELETE("삭제"),
    ENTER("참여");

    companion object {
        infix fun from(command: String): SlashCommand =
            SlashCommand.values().firstOrNull { it.command == command } ?: error("존재하지 않는 명령어입니다.")
    }
}
