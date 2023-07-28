package io.babydevelopers.babybot.application.spring.github.gateway

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class GithubGateway(
    @Value("\${github.token}") val token: String,
    val githubClient: GithubClient
) {
    fun get(username: String): GithubUser =
        githubClient.get(token, mapOf("query" to query.format(username)))
}

val query = """
query {
    user(login: "%s") {
        repositories(first: 10, orderBy: {field: UPDATED_AT, direction: DESC}) {
            edges {
                node {
                    name
                    url
                    defaultBranchRef {
                        target {
                            ... on Commit {
                                history(first: 10) {
                                    edges {
                                        node {
                                            message
                                            committedDate
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}""".trimIndent()
