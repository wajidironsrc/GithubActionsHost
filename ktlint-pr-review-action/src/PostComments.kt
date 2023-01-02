import java.lang.System
import okhttp3.HttpUrl
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*
import java.io.File
import com.google.gson.Gson


fun makePrComments(
    eventFilePath: String,
    token: String,
    collectionReportPath: String,
    ktlintReportPath: String,
    listOfAlreadyMadeComments: List<GithubReviewCommentsListResponseItem>
): Boolean {

    val retrofit = createRetrofit()

    return try {
        val allChanges = loadChanges(collectionReportPath)
        val report = createKtlintReport(ktlintReportPath)
        val event = createGithubEventRequestModel(eventFilePath)
        val comments =
            convertKtlintReportToGithubPrComments(report, event, allChanges.map { it.name }, listOfAlreadyMadeComments)
        println("comments created from ktlint report....")
        val commentsInDiff = comments
            .filter { comment ->
                val fileChanges = allChanges.first { change -> change.name == comment.path }
                comment.line in fileChanges
            }
        println("comments different filtered....")
        println("making comments now....")
        println("total comments to make after filtering are: ${commentsInDiff.size}")
        makeComments(commentsInDiff, token, event, retrofit)

        true
    } catch (ex: Throwable) {
        val errorMessage = if (ex.message.isNullOrBlank())
            "Unknown error: ${ex.javaClass.name}" else
            ex.message
        println("while making PR comments: $errorMessage")
        false
    }
}

fun loadChanges(
    pathToFileWithRelativePaths: String
): List<FileChanges> {

    println("fun loadRelativePathsOfChangedFiles: pathToFileWithRelativePaths=$pathToFileWithRelativePaths")

    return File(pathToFileWithRelativePaths)
        .readText()
        .split("\n")
        .map { line ->
            val lineParts = line.split(" ")
            val fileName = lineParts.first()
            val patchedAreas = lineParts.subList(1, lineParts.size)
                .map { patch ->
                    val patchAsInt = patch.split(",").map { it.toInt() }
                    patchAsInt[0] until patchAsInt[0] + patchAsInt[1]
                }
            Pair(fileName, patchedAreas)
        }
        .map { FileChanges(it.first, it.second) }
}

data class FileChanges(
    val name: String,
    private val patchedAreas: List<IntRange>
) {

    operator fun contains(line: Int): Boolean {
        return patchedAreas.any { area -> line in area }
    }
}

fun createKtlintReport(
    pathToKtlintReport: String
): KtlintReport {

    println("fun createKtlintReport: pathToKtlintReport=$pathToKtlintReport")

    val json = "{\"errors\": ${File(pathToKtlintReport).readText()}}"
    val ktLintReport = Gson().fromJson(json, KtlintReport::class.java)
    println("Number of errors/warnings found by KtLint: ${ktLintReport.errors.size ?: -1}")
    return ktLintReport
}

fun makeComments(
    comments: List<GithubPrComment>,
    token: String,
    event: GithubEvent,
    retrofit: Retrofit
) {

    println("fun makeComments: comments=$comments|event=$event")

    val githubPrCommentsService = retrofit.create(GithubPrCommentsService::class.java)
    comments.forEach { comment ->
        githubPrCommentsService
            .createComment(
                "token $token",
                event.pull_request.user.login,
                event.repository.name,
                event.pull_request.number,
                comment
            )
            .execute()
    }
}

interface GithubPrCommentsService {
    @POST("/repos/{owner}/{repo}/pulls/{pull_number}/comments")
    fun createComment(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("pull_number") number: Int,
        @Body comment: GithubPrComment
    ): Call<GithubPrCommentResponse>
}

class GithubPrCommentResponse(val url: String)

fun convertKtlintReportToGithubPrComments(
    ktlintReport: KtlintReport,
    event: GithubEvent,
    relativePathsOfChangedFiles: List<String>,
    listOfAlreadyMadeComments: List<GithubReviewCommentsListResponseItem>
): List<GithubPrComment> {

    println("fun convertKtlintReportToGithubPrComments: event=$event|relativePathsOfChangedFiles=$relativePathsOfChangedFiles")

    return try {
        val list = arrayListOf<GithubPrComment>()
        val errorsList: List<KtlintFileErrors> = ktlintReport.errors
        errorsList.forEach { it ->
            it.errors.forEach { ktlintError ->
                val fileName =
                    relativePathsOfChangedFiles.firstOrNull { relativePath ->
                        it.file.endsWith(
                            relativePath
                        )
                    }
                if (fileName != null) {
                    val isAlreadyAdded = listOfAlreadyMadeComments.firstOrNull { githubReviewCommentsListResponseItem ->
                        githubReviewCommentsListResponseItem.line == ktlintError.line
                                && githubReviewCommentsListResponseItem.body == ktlintError.message
                                && githubReviewCommentsListResponseItem.path == fileName
                    }
                    if(isAlreadyAdded == null) {
                        println("adding to list: line: ${ktlintError.line}, fileName: ${fileName}, message: ${ktlintError.message}")
                        list.add(
                            GithubPrComment(
                                ktlintError.message,
                                event.pull_request.head.sha,
                                fileName,
                                ktlintError.line
                            )
                        )
                    } else {
                        println("skipping item for already comment is made: line: ${ktlintError.line}, message: ${ktlintError.message}, fileName: $fileName")
                    }
                }
            }
        }
        println("total comments to be made: ${list.size}")
        list
    } catch (ex: java.lang.Exception) {
        val errorMessage = if (ex.message.isNullOrBlank())
            "Unknown error: ${ex.javaClass.name}" else
            ex.message
        println("while making PR comments: $errorMessage")
        listOf()
    }
}