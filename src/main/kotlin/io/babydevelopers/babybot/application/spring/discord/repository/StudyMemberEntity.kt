package io.babydevelopers.babybot.application.spring.discord.repository

data class StudyMemberEntity(
    val id: Long,
    val memberName: String,
    val voiceChannelName: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StudyMemberEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + memberName.hashCode()
        result = 31 * result + voiceChannelName.hashCode()
        return result
    }
}
