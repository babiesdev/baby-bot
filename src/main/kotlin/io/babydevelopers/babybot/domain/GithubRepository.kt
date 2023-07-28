package io.babydevelopers.babybot.domain

data class GithubRepository(
    val name: String,
    val url: String,
    val commits: List<Commit>,
)
