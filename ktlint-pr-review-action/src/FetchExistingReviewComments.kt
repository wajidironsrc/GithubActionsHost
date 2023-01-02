import java.lang.System
import okhttp3.HttpUrl
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*
import java.io.File
import com.google.gson.Gson


interface GithubFetchPrCommentsService {
    @GET("/repos/{owner}/{repo}/pulls/{pull_number}/comments")
    fun fetchComments(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("pull_number") pullNumber: Int
    ): Call<List<GithubReviewCommentsListResponseItem>>
}

fun fetchAlreadyMadeComments(eventFilePath: String, token: String) : List<GithubReviewCommentsListResponseItem>{

    val event = createGithubEventRequestModel(eventFilePath)

    val fetchCommentsService = createRetrofit().create(GithubFetchPrCommentsService::class.java)
    val requestCall = fetchCommentsService.fetchComments(
        token = "Bearer $token",
        owner = event.repository.owner.login,
        repo = event.repository.name,
        pullNumber = event.pull_request.number,
    )

    println("********************************Fetch PR Comment/Reviews API****************************************")
    val response = requestCall.execute()

    println("result of http while fetching PR Reviews: $response")
    println("raw response: ${response.raw()}")
    println("response message: ${response.message()}")
    println("response code: ${response.code()}, isSuccessful: ${response.isSuccessful}")

    val listOfComments = response.body() ?: emptyList()
    val gson = Gson()
    listOfComments.forEach {
        println("${gson.toJson(it)}")
    }

    return listOfComments
}