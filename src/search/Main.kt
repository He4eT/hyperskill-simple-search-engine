package search

import java.io.File
import java.util.*

fun readRecordsFromFile (fileName: String): MutableList<String> {
    val records = mutableListOf<String>()
    File(fileName).forEachLine {records.add(it)}
    return records
}

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    val ui = UI(scanner)

    val records =
            if (args.isNotEmpty()) readRecordsFromFile(args[1])
            else ui.readRecordsFromScanner()

    val index = Index(records)

    ui.mainLoop(index)
}
