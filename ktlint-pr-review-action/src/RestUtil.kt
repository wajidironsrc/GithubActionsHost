import java.lang.System
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.HttpUrl
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.io.File
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory



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
//        .addConverterFactory(MoshiConverterFactory.create(createMoshi()))
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}

fun createMoshi(): Moshi {
    return Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
}

fun createGithubEventRequestModel(
    eventFilePath: String
): GithubEvent {

    println("fun createGithubEvent: $eventFilePath")

    val json = File(eventFilePath).readText()
    return createMoshi()
        .adapter(GithubEvent::class.java)
        .fromJson(json)
        ?: throw Exception("Could not create json from file: $eventFilePath")
}