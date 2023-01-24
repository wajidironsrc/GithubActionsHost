
data class GithubUser(val login: String)
data class GithubRepoOwner(val login: String)
data class GithubRepository(val name: String, val owner: GithubRepoOwner)
data class GithubPullRequestHead(val sha: String)
data class GithubPullRequest(val number: Int, val user: GithubUser, val head: GithubPullRequestHead)
data class GithubEvent(val pull_request: GithubPullRequest, val repository: GithubRepository)
data class GithubChangedFile(val filename: String, val status: String, val patch: String)