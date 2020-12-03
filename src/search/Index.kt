package search

typealias indexT = MutableMap<String, MutableList<Int>>

class Index (val records: MutableList<String>) {

    private val index = buildIndex(records)

    private fun buildIndex(records: List<String>): indexT {
        val index = mutableMapOf<String, MutableList<Int>>()
        for (i in records.indices) {
            val words = records[i]
                .split(' ')
                .map { it.toLowerCase() }
            for (word in words) {
                if (index.containsKey(word))
                    index[word]?.add(i)
                else index[word] = mutableListOf(i)
            }
        }
        return index
    }

    val searchStrategies = Strategies

    enum class Strategies {
        ALL, ANY, NONE, FILTER;

        companion object {
            override fun toString () =
                values().joinToString(" ")

            fun hasStrategy (str: String) =
                values().any { it.name == str }
        }
    }

    fun search(query: String?, strategy: String?): List<String> {
        if (query === null) return emptyList()
        val lcQuery = query.toLowerCase()
        val words = lcQuery.split(" ")
        return when (strategy) {
            Strategies.ALL.name -> allSearch(words)
            Strategies.ANY.name -> anySearch(words)
            Strategies.NONE.name -> noneSearch(words)
            Strategies.FILTER.name -> filterRecords(lcQuery)
            else -> filterRecords(lcQuery)
        }
    }

    /* Search without inverted index */
    private fun filterRecords (query: String): List<String> =
        records.filter { record -> record.contains(query) }

    /* Search with inverted index */

    private fun indicesToRecords (indices: List<Int>): List<String> =
        indices.map { records[it] }

    /* Lines containing all the words from the query */
    private fun allSearch (words: List<String>): List<String> {
        val res = mutableSetOf<Int>()
        res.addAll(index[words[0]]?: listOf())

        for (word in words.filter { index.containsKey(it) }) {
            res.intersect(index[word]!!.toSet())
        }
        return indicesToRecords(res.toList())
    }

    /* Lines containing at least one word from the query */
    private fun anySearch (words: List<String>): List<String> {
        val res = mutableSetOf<Int>()

        for (word in words.filter { index.containsKey(it) }) {
            index[word]?.let { res.addAll(it) }
        }
        return indicesToRecords(res.toList())
    }

    /* Lines that do not contain words from the query at all */
    private fun noneSearch (words: List<String>): List<String> {
        val res = (0 until records.size).toMutableSet()

        for (key in index.keys.filter { words.contains(it) }) {
            res.removeAll(res.intersect(index[key]!!.toSet()))
        }
        return indicesToRecords(res.toList())
    }
}
