import java.lang.System
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.HttpUrl
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.io.File


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
        owner = event.pull_request.user.login,
        repo = event.repository.name,
        pullNumber = event.pull_request.number,
    )
    val response = requestCall.execute()
    val listOfComments = response.body() ?: emptyList()
    val moshiAdapter = createMoshi().adapter(GithubReviewCommentsListResponseItem::class.java)
    listOfComments.forEach {
        println("${moshiAdapter.toJson(it)}")
    }

    return listOfComments
}