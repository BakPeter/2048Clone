package com.bpapps.ex2048clone.viewmodel

import androidx.lifecycle.ViewModel
import com.bpapps.ex2048clone.model.Coordinate
import com.bpapps.ex2048clone.model.GameEngine
import com.bpapps.ex2048clone.model.SquareMovement

class AppViewModel : ViewModel(), GameEngine.IOnGameScoreUpdates, GameEngine.IOnRandomSquareAdded,
    GameEngine.IOnBestScoreUpdated, GameEngine.IOnMoveFinishedListener {

    lateinit var gameEngine: GameEngine

    private var scoreUpdateCallBack: IOnScoreUpdatedListener? = null
    private var bestScoreUpdateCallBack: IOnBestScoreUpdatedListener? = null
    private var randomAddedCallback: IOnRandomAddedListener? = null
    private var gameOverCallback: IOnGameOverListener? = null
    private var moveFinishedCallback: IOnMoveFinishedListener? = null

    var bestScore: Int = 0

    fun startGame(callBack: IOnGameStartedListener?) {
        gameEngine = GameEngine(bestScore, this, this, this, this)
        callBack?.onStarted(gameEngine.boardStatus)
        onScoreUpdated(0)
        onBestScoreUpdated(bestScore)
    }

    fun swipeUp(callback: IOnSwipeUpListener?) {
        val squaresToMove = gameEngine.swipeUp()
        callback?.onSwipeUp(squaresToMove)

        gameOverCheck()
    }

    fun swipeDown(callback: IOnSwipeDownListener?) {
        val squaresToMove = gameEngine.swipeDown()
        callback?.onSwipeDown(squaresToMove)

        gameOverCheck()
    }

    fun swipeRight(callback: IOnSwipeRightListener?) {
        val squaresToMove = gameEngine.swipeRight()
        callback?.onSwipeRight(squaresToMove)

        gameOverCheck()
    }

    fun swipeLeft(callback: IOnSwipeLeftListener?) {
        val squaresToMove = gameEngine.swipeLeft()
        callback?.onSwipeLeft(squaresToMove)

        gameOverCheck()
    }

    private fun gameOverCheck() {
        if (gameEngine.isGameFinished) {
            gameOverCallback?.onGameOver()
        }
    }

    override fun onScoreUpdated(newScore: Int) {
        scoreUpdateCallBack?.onScoreUpdated(newScore)
    }

    override fun onSquareAdded(coordinate: Coordinate, value: Int) {
        randomAddedCallback?.onRandomAdded(coordinate, value)
    }

    override fun onBestScoreUpdated(newBestScore: Int) {
        bestScore = newBestScore
        bestScoreUpdateCallBack?.onBestScoreUpdated(newBestScore)
    }

    override fun onMoveFinished(move: SquareMovement) {
        moveFinishedCallback?.onMoveFinished(move)
    }

    fun registerForScoreUpdateCallback(callBack: IOnScoreUpdatedListener) {
        scoreUpdateCallBack = callBack
    }

    fun unRegisterForScoreUpdateCallback() {
        scoreUpdateCallBack = null
    }

    fun registerForBestScoreUpdateCallback(callBack: IOnBestScoreUpdatedListener) {
        bestScoreUpdateCallBack = callBack
    }

    fun unRegisterForBestScoreUpdateCallback() {
        bestScoreUpdateCallBack = null
    }

    fun registerForRandomAddedCallback(callBack: IOnRandomAddedListener) {
        randomAddedCallback = callBack
    }

    fun unRegisterForRandomAddedCallback() {
        randomAddedCallback = null
    }

    fun registerForGameOverCallback(callBack: IOnGameOverListener) {
        gameOverCallback = callBack
    }

    fun unRegisterForGameOverCallback() {
        gameOverCallback = null
    }

    fun registerForMoveFinishedCallback(callBack: IOnMoveFinishedListener) {
        moveFinishedCallback = callBack
    }

    fun unRegisterForMoveFinishedCallback() {
        moveFinishedCallback = null
    }

    interface IOnSwipeUpListener {
        fun onSwipeUp(movements: ArrayList<SquareMovement>)
    }

    interface IOnSwipeDownListener {
        fun onSwipeDown(movements: ArrayList<SquareMovement>)
    }

    interface IOnSwipeRightListener {
        fun onSwipeRight(movements: ArrayList<SquareMovement>)
    }

    interface IOnSwipeLeftListener {
        fun onSwipeLeft(movements: ArrayList<SquareMovement>)
    }

    interface IOnRandomAddedListener {
        fun onRandomAdded(coordinate: Coordinate, valueToSet: Int)
    }

    interface IOnGameStartedListener {
        fun onStarted(boardStatus: Array<Array<Int>>)
    }

    interface IOnScoreUpdatedListener {
        fun onScoreUpdated(score: Int)
    }

    interface IOnBestScoreUpdatedListener {
        fun onBestScoreUpdated(bestScore: Int)
    }

    interface IOnGameOverListener {
        fun onGameOver()
    }

    interface IOnMoveFinishedListener {
        fun onMoveFinished(move: SquareMovement)
    }


}