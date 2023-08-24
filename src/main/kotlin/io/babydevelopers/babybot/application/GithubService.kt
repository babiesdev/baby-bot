package io.babydevelopers.babybot.application

import io.babydevelopers.babybot.domain.GithubUser
import io.babydevelopers.babybot.domain.GithubUserRepository
import net.dv8tion.jda.api.JDA
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class GithubService(
    private val githubUserRepository: GithubUserRepository,
    private val jda: JDA
) {
    @Scheduled(cron = "0 0 1 * * *")
    fun notify() {
        sendMessage(TARGET_USER_NAMES.map {
            githubUserRepository.findByUsername(it)
        })
    }

    private fun sendMessage(githubUsers: List<GithubUser>) {
        val category = jda.getTextChannelById("1143966701550063657")
        val message = category?.sendMessage(
            """
            어제의 안일한 인간들을 소개합니다... 🔪🩸
            ${
                githubUsers.map {
                    it.name to it.repositories.any { repository ->
                        repository.commits.any { commit ->
                            commit.createdAt.toLocalDate() == LocalDate.now().minusDays(1)
                        }
                    }
                }.filterNot(Pair<String, Boolean>::second)
                    .joinToString(", ") { "<@${USER_IDS[it.first]}>" }
            }
        """.trimIndent()
        )?.complete()

        val threadChannel = message?.createThreadChannel("마지막 커밋 일자")?.complete()

        val nameRepositoryCommit = githubUsers.map {
            it.name to it.repositories.map { repository ->
                repository to repository.commits.maxByOrNull { commit ->
                    commit.createdAt
                }?.createdAt
            }.maxByOrNull { (_, commit) ->
                commit!!
            }
        }.filterNot { (_, commit) ->
            commit?.second?.toLocalDate() == LocalDate.now().minusDays(1) ||
                    commit?.second?.toLocalDate() == LocalDate.now()
        }.joinToString("\n") { (name, repositoryCommit) ->
            "<@${USER_IDS[name]}> ${repositoryCommit?.first?.name}(<${repositoryCommit?.first?.url}>) : ${
                repositoryCommit?.second?.format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                )
            }"
        }
        threadChannel?.sendMessage(
            """
            ${
                nameRepositoryCommit
            }
        """.trimIndent()
        )?.queue()
    }

    companion object {
        private val TARGET_USER_NAMES = listOf(
            "gunkim",
            "junnyjun",
            "Mang-Joo",
            "YeopDev",
            "JHKoder",
            "devyonghee",
            "wlsdn93"
        )
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
