package io.babydevelopers.babybot.application.spring.mangjoo.service

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.channel.Channel
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel

class ManualForumListener(
    private val studyForumId: Long,
    private val studyCategoryId: Long
) {
    fun onChannelCreate(jda: JDA, channel: Channel) {
        if (!isForum(channel)) {
            throw IllegalArgumentException("Channel is not forum")
        }
        jda.getCategoryById(studyCategoryId)!!.createVoiceChannel("[스터디] " + channel.name).queue()
    }

    fun onChannelDelete(jda: JDA, channel: Channel) {
        deleteChannel(jda, channel)
    }


    private fun isForum(channel: Channel) =
        channel.type == ChannelType.FORUM

    private fun deleteChannel(jda: JDA, channel: Channel) {
        jda.getCategoryById(studyCategoryId)!!
            .voiceChannels.stream()
            .filter { voiceChannel: VoiceChannel -> voiceChannel.name == "[스터디] " + channel.name }
            .forEach { it: VoiceChannel -> it.delete().queue() }
    }
}