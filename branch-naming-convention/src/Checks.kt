import java.util.regex.Pattern
import kotlin.system.exitProcess

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


fun checkForTicketFromCommitMessagePattern(commitMessages: List<String>, ticketNumberFromCommitMessagePattern: String) : Int {
    println("commitMessage: ${commitMessages.joinToString { "$it, " }} , count: ${commitMessages.count()}" )

    val ticketsList = ArrayList<String>()
    val regex = ticketNumberFromCommitMessagePattern.toRegex()
    commitMessages.forEach {
        val matchResult = regex.find(it)
        if(matchResult != null) {
            ticketsList.add(matchResult.value)
        }
    }

    return if(ticketsList.distinct().count() == ticketsList.size && ticketsList.distinct().count() == commitMessages.size){
        println("Success! Ticket numbers are unique in commit messages")
        0
    } else {
        println("Error! Tickets numbers are not unique in commit messages")
        1
    }
}