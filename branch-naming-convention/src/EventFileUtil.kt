import java.io.File
import com.google.gson.Gson



class EventFileUtil(filePath: String) {

    private lateinit var githubEvent: GithubEvent


    init {
        val json = File(filePath).readText()
        githubEvent = Gson().fromJson(json, GithubEvent::class.java)
        println("Event File Path Data:")
        println(json)
    }


    fun getGithubEvent() : GithubEvent {
        return githubEvent
    }

}