@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("com.squareup.moshi:moshi:1.9.3")
@file:DependsOn("com.squareup.moshi:moshi-kotlin:1.9.3")
@file:DependsOn("com.squareup.retrofit2:retrofit:2.9.0")
@file:DependsOn("com.squareup.retrofit2:converter-moshi:2.9.0")
@file:DependsOn("com.squareup.okhttp3:logging-interceptor:4.7.2")
@file:Import("DataModels.kt")
@file:Import("FetchPrChanges.kt")
@file:Import("PostComments.kt")
@file:Import("FetchExistingReviewComments.kt")
@file:Import("RestUtil.kt")

import java.io.File
import kotlin.system.exitProcess

val arg = args[0]
if(arg != null) {
    println(arg)
    exitProcess(1)
}

val collectionReport = "collection-report.txt"
val ktLintReport = "ktlint-report.json"
val eventFilePath = args[0]
val token = args[1]

//fetch PR changes
val isSuccessFetchPrChanges = fetchPrChanges(
    eventFilePath = eventFilePath,
    token = token,
    collectionReport
)
if (!isSuccessFetchPrChanges) {
    exitProcess(-1)
}

//fetch PR comments
val commentList = fetchAlreadyMadeComments(
    eventFilePath = eventFilePath,
    token = token
)


//post new comments if required any
val isSuccessMakePrComments = makePrComments(
    eventFilePath = eventFilePath,
    token = token,
    collectionReportPath = collectionReport,
    ktlintReportPath = ktLintReport,
    listOfAlreadyMadeComments = commentList
)

exitProcess(
    if(isSuccessMakePrComments) 0 else -1
)