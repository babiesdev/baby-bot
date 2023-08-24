package io.babydevelopers.babybot.application

import io.babydevelopers.babybot.domain.GithubRepository
import io.babydevelopers.babybot.domain.GithubUser
import io.babydevelopers.babybot.domain.GithubUserRepository
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class GithubService(
    private val githubUserRepository: GithubUserRepository,
    @Value("\${discord.notify-message}")
    private val notifyMessage: String,
    private val jda: JDA
) {
    @Scheduled(cron = "0 0 0 * * *")
    fun notifyNonCommittedUser() {
        val githubUsers = USER_IDS.keys.map(githubUserRepository::findByUsername)
        val nonCommittedUsers = githubUsers
            .filterNot { it.repositories.any(GithubRepository::hasCommitFromPreviousDay) }

        sendMessage(nonCommittedUsers)
    }

    private fun sendMessage(nonCommittedUsers: List<GithubUser>) {
        val targetChannel: TextChannel = jda.getTextChannelById("1143966701550063657")
            ?: throw IllegalStateException("채널을 찾을 수 없습니다.")

        sendNonCommittedUserMessage(nonCommittedUsers, targetChannel)
    }

    private fun sendNonCommittedUserMessage(
        nonCommittedUsers: List<GithubUser>,
        category: TextChannel
    ) {
        val message = notifyMessage.format(nonCommittedUsers.joinToString(", ") { "<@${USER_IDS[it.name]}>" })
        category.sendMessage(message).queue()
    }

    companion object {
        private val USER_IDS = mapOf(
            "gunkim" to "359331321161711616",
            "junnyjun" to "891658879799283742",
            "Mang-Joo" to "381775947294179331",
            "YeopDev" to "1103325193780334634",
            "JHKoder" to "970661864390410281",
            "devyonghee" to "1022365927607636038",
            "wlsdn93" to "688317747523747852"
        )
    }
}
