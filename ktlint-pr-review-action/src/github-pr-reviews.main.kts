@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("com.squareup.retrofit2:retrofit:2.9.0")
@file:DependsOn("com.squareup.okhttp3:logging-interceptor:4.7.2")
@file:DependsOn("com.squareup.retrofit2:converter-gson:2.9.0")
@file:DependsOn("com.google.code.gson:gson:2.10")
@file:Import("DataModels.kt")
@file:Import("FetchPrChanges.kt")
@file:Import("PostComments.kt")
@file:Import("FetchExistingReviewComments.kt")
@file:Import("RestUtil.kt")
@file:Import("RunKtlint.kt")

import kotlin.system.exitProcess

val collectionReport = "collection-report.txt"
val ktLintReport = "ktlint-report.json"
val eventFilePath = args[0]
val token = args[1]
val logLevel = args[2]
val experimental = args[3]
val configFilePath = args[4]

//execute ktlint
runKtlint(
    logLevel,
    experimental,
    ktLintReport,
    configFilePath
)

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