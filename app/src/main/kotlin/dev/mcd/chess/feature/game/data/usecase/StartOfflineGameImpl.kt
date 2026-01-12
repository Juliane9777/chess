package dev.mcd.chess.feature.game.data.usecase

import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.Constants
import com.github.bhlangonijr.chesslib.Side
import dev.mcd.chess.common.game.GameSession
import dev.mcd.chess.common.player.HumanPlayer
import dev.mcd.chess.common.player.PlayerImage
import dev.mcd.chess.feature.common.domain.Translations
import dev.mcd.chess.feature.game.domain.GameSessionRepository
import dev.mcd.chess.feature.game.domain.usecase.StartOfflineGame
import java.util.UUID
import javax.inject.Inject

class StartOfflineGameImpl @Inject constructor(
    private val gameSessionRepository: GameSessionRepository,
    private val translations: Translations,
) : StartOfflineGame {
    override suspend fun invoke() {
        val board = Board().apply {
            loadFromFen(Constants.startStandardFENPosition)
        }
        val game = GameSession(
            id = UUID.randomUUID().toString(),
            self = HumanPlayer(
                name = translations.playerOne,
                image = PlayerImage.Default,
            ),
            opponent = HumanPlayer(
                name = translations.playerTwo,
                image = PlayerImage.Default,
            ),
            selfSide = Side.WHITE,
        )
        game.setBoard(board)
        gameSessionRepository.updateActiveGame(game)
    }
}
