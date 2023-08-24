package io.babydevelopers.babybot.domain

data class GithubUser(
    val name: String,
    val repositories: List<GithubRepository>,
) {
    val recentRepository: GithubRepository
        get() = repositories.maxByOrNull {
            it.commits.maxByOrNull(Commit::createdAt)
                ?.createdAt
                ?: throw IllegalStateException("커밋이 없습니다.")
        }
            ?: throw IllegalStateException("저장소가 없습니다.")
}
