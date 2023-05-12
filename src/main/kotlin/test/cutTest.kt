package test

import main.CutGradle
import org.junit.jupiter.api.BeforeEach
import java.io.*



class CutTests {
    private var cut = CutGradle()
    private lateinit var inputFileName: String
    private lateinit var outputFileName: String
    @BeforeEach
    fun setUp() {
        cut = CutGradle()
        inputFileName = "input.txt"
        outputFileName = "output.txt"
    }

    @org.junit.jupiter.api.Test
    @Throws(IOException::class)
    fun testCharBasedCutNK() {
        val args1 = mutableListOf("-c", "-o", outputFileName, inputFileName, "2-3")
        writeToFile(outputFileName)
        cut.run(args1)
        org.junit.jupiter.api.Assertions.assertEquals(
            """
                ev
                ev
                ev
                ev
                ev
                ev
                
                """.trimIndent(), readFromFile(outputFileName)
        )
    }

    @org.junit.jupiter.api.Test
    @Throws(IOException::class)
    fun testCharBasedCutN() {
        val args2 = mutableListOf("-c", "-o", outputFileName, inputFileName, "3")
        writeToFile(outputFileName)
        cut.run(args2)
        org.junit.jupiter.api.Assertions.assertEquals(
            """
                ver gonna give you up
                ver gonna let you down
                ver gonna run around and desert you
                ver gonna make you cry
                ver gonna say goodbye
                ver gonna tell a lie and hurt you
                
                """.trimIndent(), readFromFile(outputFileName)
        )
    }

    @org.junit.jupiter.api.Test
    @Throws(IOException::class)
    fun testWordBasedCutNK() {
        val args1 = mutableListOf("-w", "-o", outputFileName, inputFileName, "2-3")
        writeToFile(outputFileName)
        cut.run(args1)
        org.junit.jupiter.api.Assertions.assertEquals(
            """
                gonna give
                gonna let
                gonna run
                gonna make
                gonna say
                gonna tell
                
                """.trimIndent(), readFromFile(outputFileName)
        )
    }

    @org.junit.jupiter.api.Test
    @Throws(IOException::class)
    fun testWordBasedCutN() {
        val args2 = mutableListOf("-w", "-o", outputFileName, inputFileName, "3")
        writeToFile(outputFileName)
        cut.run(args2)
        org.junit.jupiter.api.Assertions.assertEquals(
            """
                give you up
                let you down
                run around and desert you
                make you cry
                say goodbye
                tell a lie and hurt you
                
                """.trimIndent(), readFromFile(outputFileName)
        )
    }

    @org.junit.jupiter.api.Test
    @Throws(IOException::class)
    fun testNoRangeCut() {
        val args = mutableListOf("-c", "-o", outputFileName, inputFileName)
        writeToFile(outputFileName)
        cut.run(args)
        org.junit.jupiter.api.Assertions.assertEquals(
            """
                Never gonna give you up
                Never gonna let you down
                Never gonna run around and desert you
                Never gonna make you cry
                Never gonna say goodbye
                Never gonna tell a lie and hurt you
                
                """.trimIndent(), readFromFile(outputFileName)
        )
    }

    @Throws(IOException::class)
    private fun writeToFile(fileName: String?) {
        BufferedWriter(FileWriter(fileName)).use { writer -> writer.write("first second third\nsecond third fourth fifth\nthird fourth fifth sixth\n") }
    }

    @Throws(IOException::class)
    private fun readFromFile(fileName: String?): String {
        val sb = StringBuilder()
        BufferedReader(FileReader(fileName)).use { reader ->
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                sb.append(line).append("\n")
            }
        }
        return sb.toString()
    }
}