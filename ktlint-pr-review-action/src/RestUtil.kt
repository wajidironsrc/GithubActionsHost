import java.lang.System
import okhttp3.HttpUrl
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*
import java.io.File
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson



fun createRetrofit(): Retrofit {

    val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            println("http: $message")
        }
    })

    val client = OkHttpClient()
        .newBuilder()
        .addInterceptor(loggingInterceptor)
        .build()

    return Retrofit.Builder()
        .baseUrl(HttpUrl.get("https://api.github.com"))
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}

fun createGithubEventRequestModel(
    eventFilePath: String
): GithubEvent {

    println("fun createGithubEvent: $eventFilePath")

    val json = File(eventFilePath).readText()
    return Gson().fromJson(json, GithubEvent::class.java)
}