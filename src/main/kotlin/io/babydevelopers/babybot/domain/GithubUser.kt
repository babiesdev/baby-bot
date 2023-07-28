package io.babydevelopers.babybot.domain

data class GithubUser(
    val name: String,
    val repositories: List<GithubRepository>
)
