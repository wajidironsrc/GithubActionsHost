/**
 * check validity of commit message
 */
fun checkForCommitMessageValidity(
    commitMessage: String,
    commitMessagePattern: String
): Int {
    val matches = if(commitMessagePattern == "nan") {
        println("Skipping commit message validity....")
        true
    } else {
        println("Checking commit message: $commitMessage for validity pattern: $commitMessagePattern")
        val regex = commitMessagePattern.toRegex()
        val matches = regex.matches(commitMessage)
        println("commit message validity is successful: $matches")
        matches
    }
    return if (matches) 0 else 1
}


fun checkForBranchNameValidity(branchName: String, branchNamePattern: String) : Int {
    val matches = if(branchNamePattern == "nan") {
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


fun checkForTicketFromCommitMessagePattern(commitMessage: String, ticketNumberFromCommitMessagePattern: String) : Int {
//    val matches = if(ticketNumberFromCommitMessagePattern == "nan") {
//        println("Skipping ticket number from commit message validity check....")
//        true
//    } else {
//        println("Checking ticket number from commit message: $commitMessage for validity pattern: $ticketNumberFromCommitMessagePattern")
//        val regex = ticketNumberFromCommitMessagePattern.toRegex()
//        val matches = regex.matches(commitMessage)
//        println("ticket number from commit message is: $matches")
//        matches
//    }
//    return if (matches) 0 else 1
    return 0
}