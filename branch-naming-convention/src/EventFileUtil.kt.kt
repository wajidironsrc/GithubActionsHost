import java.io.File

class `EventFileUtil.kt`(filePath: String) {

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