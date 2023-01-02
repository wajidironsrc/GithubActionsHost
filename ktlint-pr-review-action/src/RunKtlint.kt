import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

fun runKtlint(
    logLevel: Any,
    experimental: String,
    outputFileName: String,
    configFilePath: String
) {
    var command = "ktlint --log-level=$logLevel --reporter=json,output=$outputFileName"
    //setting editorConfig file path
    if (configFilePath != "/")
        command = "$command --editorconfig=$configFilePath"

    //setting experimental rules
    if (experimental.toBoolean() == true)
        command = "$command --experimental"

    println("Running command for KtLint Scan: $command")
}

fun exec(cmd: String, workingDir: File = File(".")): String? {
    try {
        val process = ProcessBuilder(*cmd.split("\\s".toRegex()).toTypedArray())
            .directory(workingDir)
//            .redirectOutput(if (captureOutput) ProcessBuilder.Redirect.PIPE else ProcessBuilder.Redirect.INHERIT)
//            .redirectError(if (captureOutput) ProcessBuilder.Redirect.PIPE else ProcessBuilder.Redirect.INHERIT)
            .start()

        process.inputStream.transferTo(System.out)
        process.errorStream.transferTo(System.err)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}