import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

fun runKtlint(
    logLevel:Any,
    experimental:Any,
    outputFileName:String

) {
    val commandOutput = exec("ktlint --log-level=$logLevel --reporter=json,output=$outputFileName")
    println("command OutPut: $commandOutput")
}

fun exec(cmd: String, stdIn: String = "", captureOutput:Boolean = true, workingDir: File = File(".")): String? {
    try {
        val process = ProcessBuilder(*cmd.split("\\s".toRegex()).toTypedArray())
            .directory(workingDir)
            .redirectOutput(if (captureOutput) ProcessBuilder.Redirect.PIPE else ProcessBuilder.Redirect.INHERIT)
            .redirectError(if (captureOutput) ProcessBuilder.Redirect.PIPE else ProcessBuilder.Redirect.INHERIT)
            .start().apply {
                if (stdIn != "") {
                    outputStream.bufferedWriter().apply {
                        write(stdIn)
                        flush()
                        close()
                    }
                }
                waitFor(60, TimeUnit.SECONDS)
            }
        if (captureOutput) {
            return process.inputStream.bufferedReader().readText()
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}