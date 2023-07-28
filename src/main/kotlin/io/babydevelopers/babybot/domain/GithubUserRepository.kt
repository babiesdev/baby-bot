package io.babydevelopers.babybot.domain

interface GithubUserRepository {
    fun findByUsername(username: String): GithubUser
}
