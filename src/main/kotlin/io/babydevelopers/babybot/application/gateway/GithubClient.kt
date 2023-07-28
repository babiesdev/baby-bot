package io.babydevelopers.babybot.application.gateway

import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange

@HttpExchange(
    accept = ["application/json"],
    contentType = "application/json",
    url = "https://api.github.com",
)
interface GithubClient {
    @PostExchange("/graphql")
    fun get(@RequestHeader("Authorization") token: String, @RequestBody query: Map<String, String>): GithubUserData
}
