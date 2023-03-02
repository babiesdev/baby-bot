package io.babydevelopers.babybot.application.spring.discord.repository

import org.springframework.stereotype.Repository

@Repository
class StudyMemoryRepository(
    private val studyMemberEntities: MutableSet<StudyMemberEntity> = mutableSetOf(),
) : StudyRepository {
    override fun save(studyMemberEntity: StudyMemberEntity) = studyMemberEntities.add(studyMemberEntity)
        .let { studyMemberEntity.id }

    override fun findAll(): List<StudyMemberEntity> = studyMemberEntities.toList()
    override fun deleteByChannelName(channelName: String) = studyMemberEntities
        .filter { it.voiceChannelName == channelName }
        .forEach { studyMemberEntities.remove(it) }

    override fun findAllByChannelName(channelName: String) = studyMemberEntities.filter { it.voiceChannelName == channelName }
}
