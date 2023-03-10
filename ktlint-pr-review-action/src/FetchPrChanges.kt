import java.lang.System
import okhttp3.HttpUrl
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*
import java.io.File

fun fetchPrChanges(
    eventFilePath: String,
    token: String,
    collectionReportFilePath: String
): Boolean {
    return try {
        val event = createGithubEventRequestModel(eventFilePath)
        val changes = collectChangesViaApi(token, createRetrofit(), event)
            .filterNot { file -> file.status == "removed" }
            .filter { file -> file.filename.endsWith(".kt") }
        saveChangesToFile(collectionReportFilePath, changes)

        true
    } catch (ex: Throwable) {
        val errorMessage = if (ex.message.isNullOrBlank())
            "Unknown error: ${ex.javaClass.name}" else
            ex.message
        println("while collecting PR changes: $errorMessage")
        false
    }
}

fun saveChangesToFile(
    collectionReportFilePath: String,
    changes: List<GithubChangedFile>
) {

    println("fun saveChanges: changes=$changes")

    val changesConcatenated = changes.joinToString("\n") { file ->
        val patches = file.patch
            .split("@@")
            .filter { s -> s.trim().startsWith("-") }
            .flatMap { s -> s.trim().split(" ") }
            .filter { s -> s.startsWith("+") }
            .joinToString(" ") { s -> s.removePrefix("+") }
        "${file.filename} $patches"
    }
    File(collectionReportFilePath).writeText(changesConcatenated)
}

fun collectChangesViaApi(
    token: String,
    retrofit: Retrofit,
    event: GithubEvent
): List<GithubChangedFile> {

    println("fun collectChanges: event=$event")

    val startingPage = 1
    return retrofit
        .create(GithubService::class.java)
        .collectAllPrChanges(token, event, startingPage)
}

fun GithubService.collectAllPrChanges(
    token: String,
    event: GithubEvent,
    startingPage: Int
): List<GithubChangedFile> {

    println("fun GithubService.collectAllPrChanges: event=$event|startingPage=$startingPage")

    val changesFromCurrentPage = executeGetPullRequestFiles(token, event, startingPage)
    val changesFromNextPage = if (changesFromCurrentPage.isEmpty())
        emptyList() else
        collectAllPrChanges(token, event, startingPage + 1)
    return changesFromCurrentPage + changesFromNextPage
}

fun GithubService.executeGetPullRequestFiles(
    token: String,
    event: GithubEvent,
    startingPage: Int
): List<GithubChangedFile> {

    println("fun GithubService.executeGetPullRequestFiles: event=$event|startingPage=$startingPage")

    val requestFiles = getPullRequestFiles(
        "token $token",
        event.repository.owner.login,
        event.repository.name,
        event.pull_request.number,
        startingPage
    )
    println("********************************Fetch PR changes API****************************************")
    val result = requestFiles.execute()
    println("result of http while fetching PR changes: $result")
    println("raw response: ${result.raw()}")
    println("response message: ${result.message()}")
    println("response code: ${result.code()}, isSuccessful: ${result.isSuccessful}")
    if(result.errorBody() != null) {
        println("error occurred: ${result.errorBody()?.string()}")
    }
    val responseItems = result.body() ?: emptyList()
    responseItems.forEach { changedFile ->
        println("Changed File: fileName: ${changedFile.filename}, status: ${changedFile.status}, patch: ${changedFile.patch}")
    }
    return responseItems
}

interface GithubService {
    @GET("/repos/{owner}/{repo}/pulls/{pull_number}/files")
    fun getPullRequestFiles(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("pull_number") number: Int,
        @Query("page") page: Int
    ): Call<List<GithubChangedFile>>
}