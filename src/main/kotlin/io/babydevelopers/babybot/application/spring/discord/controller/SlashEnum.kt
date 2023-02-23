package io.babydevelopers.babybot.application.spring.discord.controller

enum class SlashEnum(val command: String) {
    ADMISSION("승인"), DELETE("삭제"), ENTER("참여");

    companion object {
        infix fun from(command: String): SlashEnum = SlashEnum.values().firstOrNull { it.command == command } ?: error("존재하지 않는 명령어입니다.")
    }
}
