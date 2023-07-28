package io.babydevelopers.babybot.infra

import io.babydevelopers.babybot.domain.StudyMember
import io.babydevelopers.babybot.domain.StudyMemberRepository
import org.springframework.stereotype.Repository

@Repository
class InmemoryStudyMemberRepository(
    private val studyMemberEntities: MutableSet<StudyMember> = mutableSetOf(),
) : StudyMemberRepository {
    override fun save(studyMember: StudyMember) = studyMemberEntities.add(studyMember)
        .let { studyMember.id }

    override fun findAll(): List<StudyMember> = studyMemberEntities.toList()
    override fun deleteByChannelName(channelName: String) = studyMemberEntities
        .filter { it.voiceChannelName == channelName }
        .forEach { studyMemberEntities.remove(it) }

    override fun findAllByChannelName(channelName: String) = studyMemberEntities.filter { it.voiceChannelName == channelName }
}
