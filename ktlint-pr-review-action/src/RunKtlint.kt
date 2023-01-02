import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

fun runKtlint(
    logLevel: Any,
    experimental: String,
    outputFileName: String,
    configFilePath: String
) {
    var command = "ktlint --log-level=error --reporter=json,output=$outputFileName"
    //setting editorConfig file path
    if (configFilePath != "/")
        command = "$command --editorconfig=$configFilePath"

    //setting experimental rules
    if (experimental.toBoolean())
        command = "$command --experimental"

    println("Running command for KtLint Scan: $command")

    exec(command)
}

fun exec(cmd: String, workingDir: File = File(".")): String? {
    try {
        val process = ProcessBuilder(*cmd.split("\\s".toRegex()).toTypedArray())
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        process.inputStream.transferTo(System.out)
        process.errorStream.transferTo(System.err)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}