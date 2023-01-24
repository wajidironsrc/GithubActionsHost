@file:DependsOn("com.google.code.gson:gson:2.10")
@file:Import("EventFileUtil.kt")

import kotlin.system.exitProcess

val eventFilePath = args[0] //event file path
val commitMsgPattern = args[1] //Regex Pattern for commit msg verification
val branchNamePattern = args[2] //Branch Name convention
val branchCompatibility = args[3] //Branch Name Compatibility
val validateTicketsInCommit = args[4] //if true, commit msg against ticketFromCommitMessagePattern will be checked.
val ticketFromCommitMessagePattern = args[5] //Regex, to verify commit messages with.


val eventFileUtil = EventFileUtil(eventFilePath)
val githubEvent = eventFileUtil.getGithubEvent()
println(Gson().toJson(githubEvent))

exitProcess(1)