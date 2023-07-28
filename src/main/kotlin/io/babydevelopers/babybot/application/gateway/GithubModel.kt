package io.babydevelopers.babybot.application.gateway

import java.time.LocalDateTime

data class GithubUserData(
    val data: Data?
)

data class User(
    val repositories: Repositories?
)

data class Data(
    val user: User?
)

data class Repositories(
    val edges: List<Edges>?
)

data class Edges(
    val node: Node?
)

data class Node(
    val name: String?,
    val url: String?,
    val defaultBranchRef: DefaultBranchRef?
)

data class DefaultBranchRef(
    val target: Target?
)

data class Target(
    val history: History?
)

data class History(
    val edges: List<HistoryEdges>?
)

data class HistoryEdges(
    val node: HistoryNode?
)

class HistoryNode {
    val message: String? = null
    val committedDate: LocalDateTime? = null
}
