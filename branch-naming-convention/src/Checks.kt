
fun isRegularCurrentBranchNameValid(regularBranchPattern: String, currentBranch: String): Boolean {
    return if (regularBranchPattern.isEmpty()) {
        println("regular branch pattern can't be null or empty....")
        false
    } else {
        println("Checking for regular branch name: $currentBranch for validity pattern: $regularBranchPattern")
        val regex = regularBranchPattern.toRegex()
        val matches = regex.matches(currentBranch)
        println("current branch is a valid regular branch: $matches")
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

    if(commitMessage.isNullOrEmpty()) {
        println("commit message is null or empty, hence invalid")
        return false
    }

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

fun checkForTicketFromCommitMessagePattern(
    commitMessages: List<String>,
    ticketNumberFromCommitMessagePattern: String
): Boolean {
    println("commitMessage: ${commitMessages.joinToString { "$it, " }} , count: ${commitMessages.count()}")

    val ticketsList = ArrayList<String>()
    val regex = ticketNumberFromCommitMessagePattern.toRegex()
    commitMessages.forEach {
        val matchResult = regex.find(it)
        if (matchResult != null) {
            ticketsList.add(matchResult.value)
        }
    }

    return if (ticketsList.distinct().count() == ticketsList.size && ticketsList.distinct()
            .count() == commitMessages.size
    ) {
        println("Success! Ticket numbers are unique in commit messages")
        true
    } else {
        println("Error! Tickets numbers are not unique in commit messages")
        false
    }
}