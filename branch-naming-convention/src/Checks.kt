fun isFeatureBranch(featureBranchPattern: String, currentBranch: String): Boolean {
    return if (featureBranchPattern.isEmpty()) {
        println("Skipping feature branch name validity check....")
        false
    } else {
        println("Checking for feature branch name: $currentBranch for validity pattern: $featureBranchPattern")
        val regex = featureBranchPattern.toRegex()
        val matches = regex.matches(currentBranch)
        println("current branch is a feature branch: $matches")
        matches
    }
}

fun isNonFeatureCurrentBranchNameValid(nonFeatureBranchPattern: String, currentBranch: String): Boolean {
    return if (nonFeatureBranchPattern.isEmpty()) {
        println("non feature branch pattern can't be null or empty....")
        false
    } else {
        println("Checking for non feature branch name: $currentBranch for validity pattern: $nonFeatureBranchPattern")
        val regex = nonFeatureBranchPattern.toRegex()
        val matches = regex.matches(currentBranch)
        println("current branch is a valid non feature branch: $matches")
        matches
    }
}


fun isCommitMessageValid(commitMsgPattern: String, commitMsg: String?): Boolean {
    return if (commitMsgPattern.isEmpty() || commitMsg.isNullOrEmpty()) {
        println("commit msg OR its pattern can't be null or empty....")
        false
    } else {
        println("verifying commit msg: $commitMsg for pattern: $commitMsgPattern")
        val regex = commitMsgPattern.toRegex()
        val matches = regex.matches(commitMsg)
        println("Commit msg: $commitMsg against verification pattern: $commitMsgPattern resulted: $matches")
        matches
    }
}


fun checkForTicketFromCommitMessageAndBranchPattern(
    commitMessage: String?,
    branchName: String,
    ticketNumberFromCommitMessagePattern: String,
    ticketNumberFromBranchNamePattern: String
): Boolean {
    println("commitMessage: $commitMessage , ticketNumberPatternFromCommit: $ticketNumberFromCommitMessagePattern, ticketNumberFromBranch: $ticketNumberFromBranchNamePattern")


    val ticketNoFromCommitPattern = ticketNumberFromCommitMessagePattern.toRegex()
    val ticketNoFromBranchPattern = ticketNumberFromBranchNamePattern.toRegex()

    val ticketNumberFromCommitMessage = ticketNoFromCommitPattern.find(commitMessage)?.value
    val ticketNumberFromBranchName = ticketNoFromBranchPattern.find(branchName)?.value

    return if (ticketNumberFromCommitMessage != null
        && ticketNumberFromBranchName != null
        && ticketNumberFromCommitMessage == ticketNumberFromBranchName
    ) {
        println("ticket number found in both commit message and branch name and both are same")
        true
    } else {
        println("ticket number from commit message and branch name are not alike")
        if(ticketNumberFromCommitMessage.isNullOrEmpty()) {
            println("ticket number form commit message couldn't be found as per regex: $ticketNumberFromCommitMessagePattern")
        }
        if(ticketNumberFromBranchName.isNullOrEmpty()) {
            println("ticket number form branch name couldn't be found as per regex: $ticketNumberFromBranchNamePattern")
        }
        false
    }
}


/**
 * check validity of commit message
 */
fun checkForCommitMessageValidity(
    commitMessage: List<String>,
    commitMessagePattern: String
): Int {
    val matches = if (commitMessagePattern == "nan") {
        println("Skipping commit message validity....")
        true
    } else {
        println("Checking commit message: $commitMessage for validity pattern: $commitMessagePattern")
        val regex = commitMessagePattern.toRegex()
        var matches = true
        commitMessage.forEach {
            if (!regex.matches(it)) {
                matches = false
                println("Commit Message validity Failed for message: $it")
            }
        }
        println("commit message validity is successful: $matches")
        matches
    }
    return if (matches) 0 else 1
}


fun checkForBranchNameValidity(branchName: String, branchNamePattern: String): Int {
    val matches = if (branchNamePattern == "nan") {
        println("Skipping branch name validity check....")
        true
    } else {
        println("Checking branch name: $branchName for validity pattern: $branchNamePattern")
        val regex = branchNamePattern.toRegex()
        val matches = regex.matches(branchName)
        println("branch name validity is successful: $matches")
        matches
    }
    return if (matches) 0 else 1
}


//fun checkForTicketFromCommitMessagePattern(
//    commitMessages: List<String>,
//    ticketNumberFromCommitMessagePattern: String
//): Int {
//    println("commitMessage: ${commitMessages.joinToString { "$it, " }} , count: ${commitMessages.count()}")
//
//    val ticketsList = ArrayList<String>()
//    val regex = ticketNumberFromCommitMessagePattern.toRegex()
//    commitMessages.forEach {
//        val matchResult = regex.find(it)
//        if (matchResult != null) {
//            ticketsList.add(matchResult.value)
//        }
//    }
//
//    return if (ticketsList.distinct().count() == ticketsList.size && ticketsList.distinct()
//            .count() == commitMessages.size
//    ) {
//        println("Success! Ticket numbers are unique in commit messages")
//        0
//    } else {
//        println("Error! Tickets numbers are not unique in commit messages")
//        1
//    }
//}

fun checkForBranchesCompatibility(): Int {

    return 0
}