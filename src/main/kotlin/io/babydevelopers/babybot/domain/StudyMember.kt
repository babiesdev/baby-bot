package io.babydevelopers.babybot.domain

data class StudyMember(
    val id: Long,
    val memberName: String,
    val voiceChannelName: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StudyMember

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
