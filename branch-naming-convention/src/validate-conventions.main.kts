@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("com.google.code.gson:gson:2.10")
@file:Import("EventFileUtil.kt")
@file:Import("DataModels.kt")
@file:Import("Checks.kt")

import kotlin.system.exitProcess
import kotlin.collections.*

//inputs
val eventFilePath = args[0]
val featureBranchPattern = args[1]
val nonFeaturebranchNamePattern = args[2]
val allowedCommitCountOnNonFeatureBranchStr = args[3]
val commitMsgPattern = args[4]
val shouldComparetiketsInBranchNameToCommitMsgStr = args[5]
val ticketNumberFromBranchPattern = args[6]
val ticketNumberInCommitMsgPattern = args[7]
val commitMessage: String = args[8]
val failOnErrorStr = args[9]

val failOnError = (failOnErrorStr == "true")
val allowedCommitCountOnNonFeatureBranch = allowedCommitCountOnNonFeatureBranchStr.toInt()
val shouldComparetiketsInBranchNameToCommitMsg = shouldComparetiketsInBranchNameToCommitMsgStr.toBoolean()

println("starting PR validation checks....")
println("Commit Messages: $commitMessage")

val eventFileUtil = EventFileUtil(eventFilePath)
val githubEvent = eventFileUtil.getGithubEvent()
val currentBranchName = githubEvent.pull_request.head.ref
val numberOfCommits = githubEvent.pull_request.commits
val commitMessages: List<String> = if(githubEvent.pull_request.commits > 1) {
    commitMessage.split("\n").map {
        it.trim()
    }
} else {
    val msg: String = commitMessage.replace(oldValue = githubEvent.after, newValue = "", ignoreCase = true)
    val afterTrim = msg.trim()
    listOf<String>(afterTrim)
}

//STEP - 1
//verify if current branch is a feature branch:
val isFeatureBranch = isFeatureBranch(
    featureBranchPattern = featureBranchPattern,
    currentBranchName
)
if(isFeatureBranch)
    println("Curren branch is a feature branch...")
else
    println("Current branch is not a feature branch...")



var isBranchNameValid = isFeatureBranch
if(!isFeatureBranch) {
    //STEP - 2
    //if current branch is not a feature branch verify its pattern
    isBranchNameValid = isNonFeatureCurrentBranchNameValid(
        nonFeatureBranchPattern = nonFeaturebranchNamePattern,
        currentBranchName
    )
    if(isBranchNameValid) {
        println("Current Branch name is valid as a non feature branch")
    } else {
        println("Current branch name is not valid as a non feature branch.")
        if(failOnError) {
            println("Branch name is not valid")
            System.exit(1)
        }
    }


    //STEP - 3
    //if non feature branch, then check for commit count
    if(numberOfCommits > allowedCommitCountOnNonFeatureBranch) {
        println("number of commits in current branch are: $numberOfCommits which more than allowed commits: $allowedCommitCountOnNonFeatureBranch")
        if(failOnError) {
            System.exit(1)
        }
    } else {
        println("commit count check passed....")
        println("commit count is as per policy specified.")
    }


    //STEP - 4
    //check for commit message as per pattern
    val isCommitMessageValid = isCommitMessageValid(
        commitMsgPattern = commitMsgPattern,
        commitMsg = commitMessages.firstOrNull()
    )
    if(isCommitMessageValid)
        println("commit message is valid as per pattern")
    else {
        println("commit message verification failed")
        if(failOnError)
            System.exit(1)
    }

    //STEP - 5
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
            println("ticket number from branch and commit message are not alike")
            if(failOnError)
                System.exit(1)
        }
    }
}

System.exit(0) //0 means successful workflow run


//
////STEP - 1
//val commitMsgValidityResult = checkForCommitMessageValidity(
//    commitMessages,
//    commitMsgPattern
//)
//if (commitMsgValidityResult > 0 && failOnError)
//    exitProcess(commitMsgValidityResult)
//
////STEP - 2
//val branchNameValidityResult = checkForBranchNameValidity(
//    branchName = githubEvent.pull_request.head.ref,
//    branchNamePattern = branchNamePattern
//)
//if (branchNameValidityResult > 0 && failOnError)
//    exitProcess(branchNameValidityResult)
//
//
////STEP - 3
//// branch compatibility checks
//
//
////STEP - 4
//val ticketFromCommitMessageValidityResult = checkForTicketFromCommitMessagePattern(
//    commitMessages = commitMessages,
//    ticketNumberFromCommitMessagePattern = ticketFromCommitMessagePattern
//)
//if (ticketFromCommitMessageValidityResult > 0 && failOnError)
//    exitProcess(ticketFromCommitMessageValidityResult)
//
//
////STEP - 5
//println("branch compat check")
//println("data recieved: $branchCompatibility")
//println(branchCompatibility)
////val branchCompatibilityValidationResult = checkForBranchesCompatibility(
////    branchCompatibility
////)
//
//
////normal ending of the flow
//exitProcess(0)