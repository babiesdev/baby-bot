package io.babydevelopers.babybot.domain

import java.time.LocalDate

data class GithubRepository(
    val name: String,
    val url: String,
    val commits: List<Commit>,
) {
    val recentCommit
        get() = commits.maxByOrNull(Commit::createdAt)
            ?: throw IllegalStateException("커밋이 없습니다.")

    fun hasCommitFromPreviousDay() =
        commits.any { commit ->
            commit.isEqualDate(LocalDate.now().minusDays(1))
        }
}
