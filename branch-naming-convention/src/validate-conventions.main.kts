@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("com.google.code.gson:gson:2.10")

@file:Import("EventFileUtil.kt")

import kotlin.system.exitProcess

val eventFilePath = args[0]
val commitMsgPattern = args[1]
val branchNamePattern = args[2]
val branchCompatibility = args[3]
val validateTicketsInCommit = args[4]
val ticketFromCommitMessagePattern = args[5]

println("Hey Its up and running")

val eventFileUtil = EventFileUtil(eventFilePath)
val githubEvent = eventFileUtil.getGithubEvent()
println(Gson().toJson(githubEvent))

exitProcess(0)