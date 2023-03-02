package io.babydevelopers.babybot.application.spring.discord.repository

interface StudyRepository {
    fun save(studyMemberEntity: StudyMemberEntity): Long
    fun findAll(): List<StudyMemberEntity>
    fun deleteByChannelName(channelName: String)
    fun findAllByChannelName(channelName: String): List<StudyMemberEntity>
}