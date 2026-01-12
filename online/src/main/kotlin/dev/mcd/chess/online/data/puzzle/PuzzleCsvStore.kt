package dev.mcd.chess.online.data.puzzle

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.mcd.chess.online.domain.entity.Puzzle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class PuzzleCsvStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    @Volatile
    private var cached: List<Puzzle>? = null

    suspend fun puzzles(): List<Puzzle> {
        return cached ?: withContext(Dispatchers.IO) {
            cached ?: loadPuzzles().also { cached = it }
        }
    }

    private fun loadPuzzles(): List<Puzzle> {
        val inputStream = runCatching { context.assets.open(FILE_NAME) }.getOrNull() ?: return emptyList()
        inputStream.use { stream ->
            val reader = stream.bufferedReader()
            val lines = reader.readLines()
            if (lines.isEmpty()) return emptyList()

            val header = parseCsvLine(lines.first())
            val indices = PuzzleCsvIndices.fromHeader(header)
            val startIndex = if (indices.hasHeader) 1 else 0
            return lines.drop(startIndex).mapNotNull { line ->
                if (line.isBlank()) return@mapNotNull null
                val values = parseCsvLine(line)
                val puzzleId = values.getOrNull(indices.puzzleIdIndex)?.trim().orEmpty()
                val fen = values.getOrNull(indices.fenIndex)?.trim().orEmpty()
                val movesValue = values.getOrNull(indices.movesIndex)?.trim().orEmpty()
                val ratingValue = values.getOrNull(indices.ratingIndex)?.trim()
                if (puzzleId.isEmpty() || fen.isEmpty() || movesValue.isEmpty() || ratingValue.isNullOrBlank()) {
                    return@mapNotNull null
                }
                val rating = ratingValue.toIntOrNull() ?: return@mapNotNull null
                val moves = movesValue.split(' ').filter { it.isNotBlank() }
                val themes = values.getOrNull(indices.themesIndex)
                    ?.split(' ')
                    ?.filter { it.isNotBlank() }
                    ?: emptyList()
                Puzzle(
                    puzzleId = puzzleId,
                    fen = fen,
                    moves = moves,
                    rating = rating,
                    themes = themes,
                )
            }
        }
    }

    private fun parseCsvLine(line: String): List<String> {
        val results = mutableListOf<String>()
        val current = StringBuilder()
        var inQuotes = false
        var index = 0
        while (index < line.length) {
            val char = line[index]
            when {
                char == '"' -> {
                    val nextIndex = index + 1
                    if (inQuotes && nextIndex < line.length && line[nextIndex] == '"') {
                        current.append('"')
                        index++
                    } else {
                        inQuotes = !inQuotes
                    }
                }
                char == ',' && !inQuotes -> {
                    results.add(current.toString())
                    current.setLength(0)
                }
                else -> current.append(char)
            }
            index++
        }
        results.add(current.toString())
        return results
    }

    private data class PuzzleCsvIndices(
        val puzzleIdIndex: Int,
        val fenIndex: Int,
        val movesIndex: Int,
        val ratingIndex: Int,
        val themesIndex: Int,
        val hasHeader: Boolean,
    ) {
        companion object {
            fun fromHeader(header: List<String>): PuzzleCsvIndices {
                val normalized = header.map { it.trim().lowercase() }
                val hasHeader = normalized.contains("puzzleid") || normalized.contains("fen")
                if (!hasHeader) {
                    return PuzzleCsvIndices(
                        puzzleIdIndex = 0,
                        fenIndex = 1,
                        movesIndex = 2,
                        ratingIndex = 3,
                        themesIndex = 7,
                        hasHeader = false,
                    )
                }
                fun indexOf(name: String, fallback: Int): Int {
                    val idx = normalized.indexOf(name)
                    return if (idx == -1) fallback else idx
                }
                return PuzzleCsvIndices(
                    puzzleIdIndex = indexOf("puzzleid", 0),
                    fenIndex = indexOf("fen", 1),
                    movesIndex = indexOf("moves", 2),
                    ratingIndex = indexOf("rating", 3),
                    themesIndex = indexOf("themes", 7),
                    hasHeader = true,
                )
            }
        }
    }

    private companion object {
        private const val FILE_NAME = "puzzles.csv"
    }
}
