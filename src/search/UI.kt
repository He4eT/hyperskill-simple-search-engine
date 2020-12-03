package search

import java.util.*

class UI (private val scanner: Scanner){

    private fun renderRecords (records: List<String>) {
        for (record in records) println(record)
    }

    private fun renderFullList (records: List<String>) {
        println(
            if (records.isNotEmpty()) "All records:"
            else "No records")
        renderRecords(records)
    }

    private fun renderSearchResult (records: List<String>) {
        if (records.isEmpty()) {
            println("Not Found")
        } else {
            println("Search result:")
            renderRecords(records)
        }
    }

    private fun getStrategyName (index: Index): String {
        while (true) {
            println("Select a matching strategy:")
            println(index.searchStrategies)

            val strategy = scanner.nextLine().trim()
            if (index.searchStrategies.hasStrategy(strategy)) {
                return strategy
            } else println("Incorrect option! Try again.")
        }
    }

    private fun searchRecord (index: Index) {
        val strategy = getStrategyName(index)

        println("\nEnter query:")
        val query = scanner.nextLine().toLowerCase()

        renderSearchResult(index.search(query, strategy))
    }

    fun readRecordsFromScanner (): MutableList<String> {
        val records = mutableListOf<String>()

        println("Enter the number of people:")
        val n = scanner.nextLine().toIntOrNull()

        if (n === null || n == 0) return records

        println("Enter all people:")
        repeat(n) {
            records.add(scanner.nextLine())
        }
        return records
    }

    fun mainLoop (index: Index) {
        while (true) {
            println("\nMenu:\n1. Search\n2. List\n0. Exit")
            when (scanner.nextLine().toIntOrNull()) {
                0 -> {
                    println("Bye!")
                    return
                }
                1 -> searchRecord(index)
                2 -> renderFullList(index.records)
                else -> println("Incorrect option! Try again.")
            }
        }
    }
}
