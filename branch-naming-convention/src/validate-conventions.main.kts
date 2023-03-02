@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("com.google.code.gson:gson:2.10")
@file:Import("EventFileUtil.kt")
@file:Import("DataModels.kt")
@file:Import("Checks.kt")

import java.lang.System
import kotlin.system.exitProcess
import kotlin.collections.*

//inputs
val eventFilePath = args[0]
val regularBranchPattern = args[1]
val allowedCommitCountStr = args[2]
val commitMsgPattern = args[3]
val shouldComparetiketsInBranchNameToCommitMsgStr = args[4]
val ticketNumberFromBranchPattern = args[5]
val ticketNumberInCommitMsgPattern = args[6]
val commitMessage: String = args[7]
val failOnErrorStr = args[8]

val failOnError = (failOnErrorStr == "true")
val allowedCommitCount = if(allowedCommitCountStr == "NA") {
    //incase if count is not specified, then max amount is allowed.
    Int.MAX_VALUE
} else {
    allowedCommitCountStr.toInt()
}
val shouldComparetiketsInBranchNameToCommitMsg = shouldComparetiketsInBranchNameToCommitMsgStr.toBoolean()

println("starting PR validation checks....")
println("Commit Messages: $commitMessage")

val eventFileUtil = EventFileUtil(eventFilePath)
val githubEvent = eventFileUtil.getGithubEvent()
val currentBranchName = githubEvent.pull_request.head.ref
val destinationBranchName = githubEvent.pull_request.base.ref
val numberOfCommits = githubEvent.pull_request.commits
val commitMessages: List<String> = if(githubEvent.pull_request.commits > 1) {
    commitMessage.split("\n").map {
        it.trim()
    }
} else {
    val msg: String = commitMessage.trim()
    listOf<String>(msg)
}
println("commit messages:")
commitMessages.forEach {
    println(it)
}

println("Ref: Current/Head Branch: $currentBranchName")
println("Ref: Destination/Base BranchName: $destinationBranchName")

//regular branch checks
if(allowedCommitCount == 1) {
    //current branch is regular branch verify its pattern
    val isBranchNameValid = isRegularCurrentBranchNameValid(
        regularBranchPattern = regularBranchPattern,
        currentBranchName
    )
    if(isBranchNameValid) {
        println("Current Branch name is valid as a regular branch, branchName: $currentBranchName")
    } else {
        failRun("Current branch name is not valid as a regular branch, branchName: $currentBranchName")
    }

    //its a regular branch, so checking for commit count
    if(numberOfCommits > allowedCommitCount) {
        failRun("number of commits in current branch are: $numberOfCommits which more than allowed commits: $allowedCommitCount")
    } else {
        println("commit count check passed....")
        println("commit count is as per policy specified.")
    }

    //check for commit message as per pattern
    val commitMessage = commitMessages.firstOrNull()
    val isCommitMessageValid = isCommitMessageValid(
        commitMsgPattern = commitMsgPattern,
        commitMsg = commitMessage
    )
    if(isCommitMessageValid)
        println("commit message is valid as per pattern, commitMessage: $commitMessage, commitMsgPattern: $commitMsgPattern\"")
    else {
        failRun("commit message verification failed, commitMessage: $commitMessage, commitMsgPattern: $commitMsgPattern")
    }

    //check if ticket number from commit and branch name is same
    if(shouldComparetiketsInBranchNameToCommitMsg) {
        val areTicketNumberAlikeInCommitAndBranchName = checkForTicketFromCommitMessageAndBranchPattern(
            commitMessages.firstOrNull(),
            currentBranchName,
            ticketNumberInCommitMsgPattern,
            ticketNumberFromBranchPattern
        )
        if(areTicketNumberAlikeInCommitAndBranchName) {
            println("ticket number from branch and commit message are alike")
        } else {
            failRun("ticket number from branch and commit message are not alike")
        }
    }
} else {
    //Non Regular branch checks
    //check if all commits included in the non regular branch are unique from their ticket numbers
    val areAllCommitsUnique = checkForTicketFromCommitMessagePattern(
        commitMessages,
        ticketNumberInCommitMsgPattern
    )
    if(areAllCommitsUnique) {
        println("All commits messages in the non regular branch included are unique.")
    } else {
        failRun("Not all commits messages in the non regular branch included are unique.")
    }
}

//reached the end without any errors,
successfulRun()

fun successfulRun() {
    exitProcess(0)
}

fun failRun(errorMsg: String) {
    System.err.println(errorMsg)
    if(failOnError)
        exitProcess(1)
}