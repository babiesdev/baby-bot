package io.babydevelopers.babybot.application.gateway

import io.babydevelopers.babybot.domain.Commit
import io.babydevelopers.babybot.domain.GithubRepository
import io.babydevelopers.babybot.domain.GithubUser
import io.babydevelopers.babybot.domain.GithubUserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class GithubGateway(
    @Value("\${github.token}") val token: String,
    val githubClient: GithubClient,
) : GithubUserRepository {
    override fun findByUsername(username: String): GithubUser =
        GithubUser(
            username,
            githubClient.get(token, mapOf("query" to query.format(username))).data!!.user!!.repositories!!.edges!!.map {
                GithubRepository(
                    it.node!!.name!!,
                    it.node!!.url!!,
                    it.node!!.defaultBranchRef!!.target!!.history!!.edges!!.map {
                        Commit(it.node!!.message!!, it.node!!.committedDate!!)
                    },
                )
            },
        )
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
}
""".trimIndent()
