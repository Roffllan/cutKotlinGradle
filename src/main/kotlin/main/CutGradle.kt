package main

import org.kohsuke.args4j.Argument
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option
import java.io.*
import java.lang.Integer.max
import java.lang.Math.min

class CutGradle {
    @Option(name = "-o", usage = "Output file name")
    private lateinit var outputFileName: String

    @Option(name = "-c", usage = "by characters")
    private var isCharBased = false

    @Option(name = "-w", usage = "by words")
    private var isWordBased = false

    @Argument(index = 0, usage = "Input file name")
    private lateinit var inputFileName: String

    @Argument(index = 1, usage = "Range")
    private var range: String? = null

    fun run(args: MutableList<String>) {
        val cmdParser = CmdLineParser(this).runCatching { this.parseArgument(args) }
            .onFailure { System.err }

        try {
            var isDynamicsize = false
            var stringForChange = ""
            val fileWriter = File(outputFileName).bufferedWriter()
            BufferedReader(FileReader(inputFileName)).use { br ->
                br.lines().filter{it.isNotEmpty()}.forEach {
                    var begin: Int
                    var end: Int
                    if (range == null){
                        isDynamicsize = true
                        range = "-${it.length}"
                    }
                    if (range!!.contains("-")) {
                        if (range!![0] == '-') {
                            range = "1-" + range!!.substring(1)
                        }
                        val parts = range!!.trim().split("-")
                        begin = Integer.parseInt(parts[0]) - 1
                        end = Integer.parseInt(parts[1])
                    } else {
                        begin = Integer.parseInt(range) - 1
                        end = it.length
                    }
                    if (isCharBased) {
                        println(stringForChange)
                        begin = max(begin, 0)
                        end = min(end, it.length)
                        if (isDynamicsize){
                            end = it.length
                        }
                        require(end > begin) {
                            "Введён неверный диапазон"
                        }
                        stringForChange = it.substring(begin, end)
                    } else if (isWordBased) {
                        val words = it.split(" ")
                        begin = max(begin, 0)
                        end = min(end, words.size)
                        stringForChange = words.subList(begin, end).joinToString(" ")
                    }
                    fileWriter.write(stringForChange)
                    fileWriter.newLine()
                }
                fileWriter.flush()
            }

        } catch (e: FileNotFoundException) {
            throw RuntimeException()
        } catch (e: IOException) {
            System.err.println("Error: " + e.message)
        }
    }
}

fun main() {
    CutGradle().run(mutableListOf("-c", "-o", "output.txt", "input.txt", "2-3"))
}

