package io.babydevelopers.babybot.domain

interface StudyMemberRepository {
    fun save(studyMember: StudyMember): Long
    fun findAll(): List<StudyMember>
    fun deleteByChannelName(channelName: String)
    fun findAllByChannelName(channelName: String): List<StudyMember>
}
