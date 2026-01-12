package dev.mcd.chess.online.data.usecase

import dev.mcd.chess.online.data.puzzle.PuzzleCsvStore
import dev.mcd.chess.online.domain.ChessApi
import dev.mcd.chess.online.domain.PuzzleOptions
import dev.mcd.chess.online.domain.entity.Puzzle
import dev.mcd.chess.online.domain.usecase.GetRandomPuzzle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class GetRandomPuzzleImpl @Inject constructor(
    internal val chessApi: ChessApi,
    private val puzzleCsvStore: PuzzleCsvStore,
) : GetRandomPuzzle {
    override suspend operator fun invoke(ratingRange: IntRange): Puzzle {
        val options = PuzzleOptions(ratingRange = ratingRange)

        return withContext(Dispatchers.IO) {
            val localPuzzles = puzzleCsvStore.puzzles()
            val matchingLocal = localPuzzles.filter { it.rating in ratingRange }
            when {
                matchingLocal.isNotEmpty() -> matchingLocal.random()
                localPuzzles.isNotEmpty() -> localPuzzles.random()
                else -> chessApi.getRandomPuzzle(options)
            }
        }
    }

}
