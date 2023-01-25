
data class GithubUser(val login: String)
data class GithubRepoOwner(val login: String)
data class GithubRepository(val name: String, val owner: GithubRepoOwner)
data class GithubPullRequestHead(
    val sha: String,
    val ref: String, //origin branch of the PR
)

data class GithubPullRequestBase(
    val ref: String, //destination branch

)
data class GithubPullRequest(
    val number: Int,
    val user: GithubUser,
    val head: GithubPullRequestHead,
    val base: GithubPullRequestBase
)
data class GithubEvent(
    val pull_request: GithubPullRequest,
    val repository: GithubRepository
)