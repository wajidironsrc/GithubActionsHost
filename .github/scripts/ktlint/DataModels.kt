data class GithubReviewCommentsListResponseItem(
    val side: String? = null,
    val line: Int? = null,
    val diffHunk: String? = null,
    val startLine: Int? = null,
    val body: String? = null,
    val startSide: String? = null,
    val originalLine: Int? = null,
    val path: String? = null,
    val originalPosition: Int? = null,
    val originalStartLine: Int? = null,
    val pullRequestReviewId: Int? = null,
    val id: Int? = null,
    val position: Int? = null
)

data class GithubUser(val login: String)
data class GithubRepoOwner(val login: String)
data class GithubRepository(val name: String, val owner: GithubRepoOwner)
data class GithubPullRequestHead(val sha: String)
data class GithubPullRequest(val number: Int, val user: GithubUser, val head: GithubPullRequestHead)
data class GithubEvent(val pull_request: GithubPullRequest, val repository: GithubRepository)
data class GithubChangedFile(val filename: String, val status: String, val patch: String)

data class GithubPrComment(
    val body: String,
    val commit_id: String,
    val path: String,
    val line: Int,
    val side: String = "RIGHT"
)

class KtlintError(val line: Int, val message: String)
class KtlintFileErrors(val file: String, val errors: List<KtlintError>)
class KtlintReport(val errors: List<KtlintFileErrors>)