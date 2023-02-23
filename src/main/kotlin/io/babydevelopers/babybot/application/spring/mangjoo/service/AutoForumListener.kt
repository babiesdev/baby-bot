package io.babydevelopers.babybot.application.spring.mangjoo.service

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.channel.Channel
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateArchivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.internal.entities.channel.concrete.ThreadChannelImpl

class AutoForumListener(
    private val studyForumId: Long,
    private val studyCategoryId: Long
) : ListenerAdapter() {
    override fun onChannelDelete(event: ChannelDeleteEvent) {
        if (event.channel.asThreadChannel().parentChannel.asForumChannel().idLong == studyForumId) {
            deleteChannel(event.jda, event.channel)
        }
    }

    override fun onChannelUpdateArchived(event: ChannelUpdateArchivedEvent) {
        if (isForum(event) && isArchaive(event)) {
            deleteChannel(event.jda, event.channel)
        }
        if (isForum(event) && !isArchaive(event)) {
            val categoryById = event.jda.getCategoryById(studyCategoryId)
            categoryById!!.createVoiceChannel("[스터디] " + event.channel.name).queue()
        }
    }

    private fun isArchaive(event: ChannelUpdateArchivedEvent) =
        (event.channel as ThreadChannelImpl).isArchived


    private fun isForum(event: ChannelUpdateArchivedEvent) =
        event.channel.asThreadChannel().parentChannel.asForumChannel().idLong == studyForumId

    private fun deleteChannel(event: JDA, channel: Channel) {
        event.getCategoryById(studyCategoryId)!!
            .voiceChannels.stream()
            .filter { voiceChannel: VoiceChannel -> voiceChannel.name == "[스터디] " + channel.name }
            .forEach { it: VoiceChannel -> it.delete().queue() }
    }
}