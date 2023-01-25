@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("com.google.code.gson:gson:2.10")
@file:Import("EventFileUtil.kt")
@file:Import("DataModels.kt")
@file:Import("Checks.kt")

import kotlin.system.exitProcess

//inputs
val eventFilePath = args[0]
val commitMsgPattern = args[1]
val branchNamePattern = args[2]
val branchCompatibility = args[3]
val validateTicketsInCommit = args[4]
val ticketFromCommitMessagePattern = args[5]
val commitMessage = args[6]
//global vars
val failOnError = false


println("starting PR validation checks....")
println("Commit Messages: $commitMessage")

val eventFileUtil = EventFileUtil(eventFilePath)
val githubEvent = eventFileUtil.getGithubEvent()

//STEP - 1
val commitMsgValidityResult = checkForCommitMessageValidity(
    commitMessage,
    commitMsgPattern
)
if (commitMsgValidityResult > 0 && failOnError)
    exitProcess(commitMsgValidityResult)

//STEP - 2
val branchNameValidityResult = checkForBranchNameValidity(
    branchName = githubEvent.pull_request.head.ref,
    branchNamePattern = branchNamePattern
)
if (branchNameValidityResult > 0 && failOnError)
    exitProcess(branchNameValidityResult)


//STEP - 3
// branch compatibility checks


//STEP - 4
val ticketFromCommitMessageValidityResult = checkForTicketFromCommitMessagePattern(
    commitMessage = commitMessage,
    ticketNumberFromCommitMessagePattern = ticketFromCommitMessagePattern
)
if (ticketFromCommitMessageValidityResult > 0 && failOnError)
    exitProcess(ticketFromCommitMessageValidityResult)


//normal ending of the flow
exitProcess(0)