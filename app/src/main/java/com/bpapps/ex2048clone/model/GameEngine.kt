package com.bpapps.ex2048clone.model

import kotlin.random.Random


class GameEngine(
    private var bestScore: Int,
    private var bestScoreUpdatedCallback: IOnBestScoreUpdated?,
    private var scoreUpdatedCallback: IOnGameScoreUpdates?,
    private var addedRandomSquareCallback: IOnRandomSquareAdded?
) {
    private val dimens = Configurations.BOARD_DIMENSIONS

    private var squares: Array<Array<Int>> = Array(dimens) {
        Array(dimens) {
            EMPTY_SQUARE
        }
    }
    val boardStatus: Array<Array<Int>>
        get() = squares

    private var score: Int = 0
    var isGameFinished = false

    init {
        addRandom()
        addRandom()
    }

    private fun addRandom() {
        if (!isGameFinished) {
            val emptySquaresCoordinates: ArrayList<Coordinate> = arrayListOf()

            squares.forEachIndexed { row, lines ->
                lines.forEachIndexed { col, num ->
                    if (num == EMPTY_SQUARE) {
                        emptySquaresCoordinates.add(Coordinate(row, col))
                    }
                }
            }

            if (emptySquaresCoordinates.size > 1) {
                val ind = Random.nextInt(0, emptySquaresCoordinates.size - 1)
                val value = if (Random.nextBoolean()) 2 else 4
                squares[emptySquaresCoordinates[ind].row][emptySquaresCoordinates[ind].col] = value

                addedRandomSquareCallback?.onSquareAdded(emptySquaresCoordinates[ind], value)
            }
        }
    }

    fun swipeUp(): ArrayList<SquareMovement> {
        val retVal = arrayListOf<SquareMovement>()

        if (isGameFinished)
            return retVal

        var col = 0
        while (col < dimens) {
            var row = 0

            while (row < dimens - 1) {
                if (squares[row + 1][col] != EMPTY_SQUARE) {
                    if (squares[row][col] != EMPTY_SQUARE) {
                        if (squares[row][col] == squares[row + 1][col]) {
                            //merge
                            squares[row][col] = squares[row][col] * 2
                            squares[row + 1][col] = EMPTY_SQUARE

                            updateScore(squares[row][col])

                            retVal.add(
                                SquareMovement(
                                    Coordinate(row + 1, col),
                                    Coordinate(row, col),
                                    squares[row][col],
                                    true
                                )
                            )
                        }
                        //move index down
                        row++

                        //don't change squares
                        //move index down
                        //row++
                    } else {
                        //squares[row][col] == EMPTY_SQUARE
                        //move square up
                        squares[row][col] = squares[row + 1][col]
                        squares[row + 1][col] = EMPTY_SQUARE

                        retVal.add(
                            SquareMovement(
                                Coordinate(row + 1, col),
                                Coordinate(row, col),
                                squares[row][col],
                                false
                            )
                        )

                        //move index up unless it is at the beginning then move down
                        if (row > 0) {
                            row--
                        } else {
                            row++
                        }
                    }
                } else {
                    //squares[row + 1][col] == EMPTY_SQUARE
                    //move ind down
                    row++
                }
            }

            col++
        }

        addRandom()

        checkIfGameFinished()

        return retVal
    }

    fun swipeDown(): ArrayList<SquareMovement> {
        val retVal = arrayListOf<SquareMovement>()

        if (isGameFinished)
            return retVal

        var col = 0
        while (col < dimens) {
            var row = dimens - 1

            while (row > 0) {
                if (squares[row - 1][col] != EMPTY_SQUARE) {
                    if (squares[row][col] != EMPTY_SQUARE) {
                        if (squares[row][col] == squares[row - 1][col]) {
                            //merge
                            squares[row][col] = squares[row][col] * 2
                            squares[row - 1][col] = EMPTY_SQUARE

                            updateScore(squares[row][col])

                            retVal.add(
                                SquareMovement(
                                    Coordinate(row - 1, col),
                                    Coordinate(row, col),
                                    squares[row][col],
                                    true
                                )
                            )
                        }
                        //move index up
                        row--

                        //don't change squares
                        //move index down
                        //row++
                    } else {
                        //squares[row][col] == EMPTY_SQUARE
                        //move square down
                        squares[row][col] = squares[row - 1][col]
                        squares[row - 1][col] = EMPTY_SQUARE

                        retVal.add(
                            SquareMovement(
                                Coordinate(row - 1, col),
                                Coordinate(row, col),
                                squares[row][col],
                                false
                            )
                        )

                        //move index down unless it is at the end then move up
                        if (row < dimens - 1) {
                            row++
                        } else {
                            row--
                        }
                    }
                } else {
                    //squares[row - 1][col] == EMPTY_SQUARE
                    //move ind up
                    row--
                }
            }

            col++
        }

        addRandom()

        checkIfGameFinished()

        return retVal
    }

    fun swipeRight(): ArrayList<SquareMovement> {
        val retVal = arrayListOf<SquareMovement>()

        if (isGameFinished)
            return retVal

        var row = 0
        while (row < dimens) {
            var col = dimens - 1
            while (col > 0) {
                if (squares[row][col - 1] != EMPTY_SQUARE) {
                    if (squares[row][col] != EMPTY_SQUARE) {
                        if (squares[row][col] == squares[row][col - 1]) {
                            squares[row][col] = squares[row][col] * 2
                            squares[row][col - 1] = EMPTY_SQUARE
                        }

                        updateScore(squares[row][col])

                        retVal.add(
                            SquareMovement(
                                Coordinate(row - 1, col),
                                Coordinate(row, col),
                                squares[row][col],
                                true
                            )
                        )

                        col--
                    } else {
                        //squares[row][col] == EMPTY_SQUARE
                        squares[row][col] = squares[row][col - 1]
                        squares[row][col - 1] = EMPTY_SQUARE

                        retVal.add(
                            SquareMovement(
                                Coordinate(row - 1, col),
                                Coordinate(row, col),
                                squares[row][col],
                                false
                            )
                        )

                        if (col < dimens - 1) {
                            col++
                        } else {
                            col--
                        }
                    }
                } else {
                    //squares[row][col + 1] == EMPTY_SQUARE
                    col--
                }
            }

            row++
        }

        addRandom()

        checkIfGameFinished()

        return retVal
    }


    fun swipeLeft(): ArrayList<SquareMovement> {
        val retVal = arrayListOf<SquareMovement>()

        if (isGameFinished)
            return retVal

        var row = 0
        while (row < dimens) {
            var col = 0
            while (col < dimens - 1) {
                if (squares[row][col + 1] != EMPTY_SQUARE) {
                    if (squares[row][col] != EMPTY_SQUARE) {
                        if (squares[row][col] == squares[row][col + 1]) {
                            squares[row][col] = squares[row][col] * 2
                            squares[row][col + 1] = EMPTY_SQUARE
                        }

                        updateScore(squares[row][col])

                        retVal.add(
                            SquareMovement(
                                Coordinate(row + 1, col),
                                Coordinate(row, col),
                                squares[row][col],
                                true
                            )
                        )

                        col++
                    } else {
                        //squares[row][col] == EMPTY_SQUARE
                        squares[row][col] = squares[row][col + 1]
                        squares[row][col + 1] = EMPTY_SQUARE

                        retVal.add(
                            SquareMovement(
                                Coordinate(row + 1, col),
                                Coordinate(row, col),
                                squares[row][col],
                                false
                            )
                        )

                        if (col > 0) {
                            col--
                        } else {
                            col++
                        }
                    }
                } else {
                    //squares[row][col + 1] == EMPTY_SQUARE
                    col++
                }
            }

            row++
        }

        addRandom()

        checkIfGameFinished()

        return retVal
    }

    private fun checkIfGameFinished(): Boolean {

        for (i in 0 until dimens) {
            for (j in 0 until dimens) {

                if (squares[i][j] == END_GAME_VALUE) return true

                if (squares[i][j] == EMPTY_SQUARE) return true

                if (i > 0 && squares[i][j] == squares[i - 1][j]) return true
                if (i < dimens - 1 && squares[i][j] == squares[i + 1][j]) return true

                if (j > 0 && squares[i][j] == squares[i][j - 1]) return true
                if (j < dimens - 1 && squares[i][j] == squares[i][j + 1]) return true
            }
        }

        return false
    }

    private fun updateScore(value: Int) {
        score += value
        scoreUpdatedCallback?.onScoreUpdated(score)
        updateBestScore()
    }

    private fun updateBestScore() {
        if (score > bestScore) {
            bestScore = score
            bestScoreUpdatedCallback?.onBestScoreUpdated(bestScore)
        }
    }

    companion object {
        private const val TAG = "TAG.GameEngine"
        const val EMPTY_SQUARE = 1
        const val END_GAME_VALUE = 2048
    }

    interface IOnGameScoreUpdates {
        fun onScoreUpdated(newScore: Int)
    }

    interface IOnRandomSquareAdded {
        fun onSquareAdded(coordinate: Coordinate, value: Int)
    }

    interface IOnBestScoreUpdated {
        fun onBestScoreUpdated(newBestScore: Int)
    }
}