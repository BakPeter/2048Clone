package com.bpapps.ex2048clone.view

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bpapps.ex2048clone.R
import com.bpapps.ex2048clone.model.Coordinate
import com.bpapps.ex2048clone.model.GameEngine
import com.bpapps.ex2048clone.model.SquareMovement
import com.bpapps.ex2048clone.viewmodel.AppViewModel
import kotlin.math.abs


class MainViewFragment : Fragment(), View.OnTouchListener, AppViewModel.IOnGameStartedListener,
    AppViewModel.IOnScoreUpdatedListener, AppViewModel.IOnBestScoreUpdatedListener,
    AppViewModel.IOnRandomAddedListener,
    AppViewModel.IOnGameOverListener {
    private val viewModel by viewModels<AppViewModel>()

    private lateinit var squares: Array<Array<AppCompatTextView>>
    private lateinit var movableSquare: AppCompatTextView
    private lateinit var btnNewGame: AppCompatButton
    private lateinit var tvScore: AppCompatTextView
    private lateinit var tvBestScore: AppCompatTextView

    private lateinit var board: FrameLayout
    private var x1: Float = 0F
    private var x2: Float = 0F
    private var y1: Float = 0F
    private var y2: Float = 0F

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_view, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)?.let { sharedPref ->
            viewModel.bestScore = sharedPref.getInt(PREFERENCES_BEST_SCORE, 0)
        }

        btnNewGame = view.findViewById(R.id.btnNewGame)
        btnNewGame.setOnClickListener {
            viewModel.startGame(this)
        }

        board = view.findViewById(R.id.flBoardContainer)
        board.setOnTouchListener(this)

        tvBestScore = view.findViewById(R.id.tvBestScore)
        onBestScoreUpdated(viewModel.bestScore)

        tvScore = view.findViewById(R.id.tvScore)

        initSquares(view)
        movableSquare = view.findViewById(R.id.movableSquare)

        viewModel.startGame(this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.registerForScoreUpdateCallback(this)
        viewModel.registerForBestScoreUpdateCallback(this)
        viewModel.registerForRandomAddedCallback(this)
        viewModel.registerForGameOverCallback(this)
    }

    override fun onStop() {
        super.onStop()
        viewModel.unRegisterForScoreUpdateCallback()
        viewModel.unRegisterForBestScoreUpdateCallback()
        viewModel.unRegisterForRandomAddedCallback()
        viewModel.unRegisterForGameOverCallback()

        activity?.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)?.edit()
            .let { editor ->
                editor?.putInt(PREFERENCES_BEST_SCORE, viewModel.bestScore)
                editor?.commit()
            }
    }

    private fun initSquares(view: View) {
        val tv00: AppCompatTextView = view.findViewById(R.id.tvSquare00)
        val tv01: AppCompatTextView = view.findViewById(R.id.tvSquare01)
        val tv02: AppCompatTextView = view.findViewById(R.id.tvSquare02)
        val tv03: AppCompatTextView = view.findViewById(R.id.tvSquare03)
        val tv10: AppCompatTextView = view.findViewById(R.id.tvSquare10)
        val tv11: AppCompatTextView = view.findViewById(R.id.tvSquare11)
        val tv12: AppCompatTextView = view.findViewById(R.id.tvSquare12)
        val tv13: AppCompatTextView = view.findViewById(R.id.tvSquare13)
        val tv20: AppCompatTextView = view.findViewById(R.id.tvSquare20)
        val tv21: AppCompatTextView = view.findViewById(R.id.tvSquare21)
        val tv22: AppCompatTextView = view.findViewById(R.id.tvSquare22)
        val tv23: AppCompatTextView = view.findViewById(R.id.tvSquare23)
        val tv30: AppCompatTextView = view.findViewById(R.id.tvSquare30)
        val tv31: AppCompatTextView = view.findViewById(R.id.tvSquare31)
        val tv32: AppCompatTextView = view.findViewById(R.id.tvSquare32)
        val tv33: AppCompatTextView = view.findViewById(R.id.tvSquare33)

        squares = arrayOf(
            arrayOf(tv00, tv01, tv02, tv03),
            arrayOf(tv10, tv11, tv12, tv13),
            arrayOf(tv20, tv21, tv22, tv23),
            arrayOf(tv30, tv31, tv32, tv33)
        )
    }

    override fun onStarted(boardStatus: Array<Array<Int>>) {
        for (i in boardStatus.indices) {
            for (j in boardStatus[i].indices) {
                setSquare(squares[i][j], boardStatus[i][j])

//                if (boardStatus[i][j] != GameEngine.EMPTY_SQUARE) {
//                    squares[i][j].text = boardStatus[i][j].toString()
//                } else {
//                    squares[i][j].text = ""
//                }
            }
        }
    }

    private fun setSquare(square: AppCompatTextView, value: Int) {
        if (value == GameEngine.EMPTY_SQUARE)
            square.text = ""
        else
            square.text = value.toString()

        when (value) {
            2 -> square.setBackgroundColor(resources.getColor(R.color._2Back, null))
            4 -> square.setBackgroundColor(resources.getColor(R.color._4Back, null))
            8 -> square.setBackgroundColor(resources.getColor(R.color._8Back, null))
            16 -> square.setBackgroundColor(resources.getColor(R.color._16Back, null))
            32 -> square.setBackgroundColor(resources.getColor(R.color._32Back, null))
            64 -> square.setBackgroundColor(resources.getColor(R.color._64Back, null))
            128 -> square.setBackgroundColor(resources.getColor(R.color._128Back, null))
            256 -> square.setBackgroundColor(resources.getColor(R.color._256Back, null))
            512 -> square.setBackgroundColor(resources.getColor(R.color._512Back, null))
            1024 -> square.setBackgroundColor(resources.getColor(R.color._1024Back, null))
            2048 -> square.setBackgroundColor(resources.getColor(R.color._2048Back, null))
            else -> square.setBackgroundColor(
                resources.getColor(
                    R.color.empty_square_background,
                    null
                )
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        var retVal = true

        when (p1?.action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = p1.x
                y1 = p1.y

//                Log.d(TAG, "(x1, y1)= ($x1, $y1)")
            }
            MotionEvent.ACTION_UP -> {
                x2 = p1.x
                y2 = p1.y

//                Log.d(TAG, "(x2, y2)= ($x2, $y2)")

                val dX = abs(x1 - x2)
                val dY = abs(y1 - y2)

                when {
                    dX < dY -> {
                        if (y1 > y2) {
                            Log.d(TAG, "swipe up")
                            viewModel.swipeUp(object : AppViewModel.IOnSwipeUpListener {
                                override fun onSwipeUp(movements: ArrayList<SquareMovement>) {
                                    onStarted(viewModel.gameEngine.boardStatus)
                                    animateMoves(movements)
                                }
                            })
                        } else {
                            Log.d(TAG, "swipe down")
                            viewModel.swipeDown(object : AppViewModel.IOnSwipeDownListener {
                                override fun onSwipeDown(movements: ArrayList<SquareMovement>) {
                                    onStarted(viewModel.gameEngine.boardStatus)
                                    animateMoves(movements)
                                }
                            })
                        }
                    }
                    dX > dY -> {
                        if (x1 > x2) {
                            Log.d(TAG, "swipe left")
                            viewModel.swipeLeft(object : AppViewModel.IOnSwipeLeftListener {
                                override fun onSwipeLeft(movements: ArrayList<SquareMovement>) {
                                    onStarted(viewModel.gameEngine.boardStatus)
                                    animateMoves(movements)
                                }
                            })
                        } else {
                            Log.d(TAG, "swipe right")
                            viewModel.swipeRight(object : AppViewModel.IOnSwipeRightListener {
                                override fun onSwipeRight(movements: ArrayList<SquareMovement>) {
                                    onStarted(viewModel.gameEngine.boardStatus)
                                    animateMoves(movements)
                                }
                            })
                        }
                    }
                    dX == dY -> {
                        Log.d(TAG, "touch, not swipe")

                        retVal = false
                    }
                }
            }
        }

        return retVal
    }

    private fun animateMoves(movements: java.util.ArrayList<SquareMovement>) {
        board.isEnabled = false

        movements.forEach { move ->
            val fromSquare = squares[move.from.row][move.from.col]
            val toSquare = squares[move.to.row][move.to.col]

            movableSquare.x = fromSquare.x
            movableSquare.y = fromSquare.y

            ObjectAnimator.ofFloat(movableSquare, "translationY", toSquare.y).apply {
                duration = 2000
                start()
            }

//            val ta =
//                TranslateAnimation(fromSquare.x, toSquare.x, fromSquare.y, toSquare.y).also { ta ->
//                    ta.duration = 2000
//                    ta.setAnimationListener(object :
//                        Animation.AnimationListener {
//                        override fun onAnimationRepeat(animation: Animation?) {
//                            TODO("Not yet implemented")
//                        }
//
//                        override fun onAnimationEnd(animation: Animation?) {
//                            Log.d(TAG, "onAnimation end")
////                            movableSquare.visibility = View.GONE
//                        }
//
//                        override fun onAnimationStart(animation: Animation?) {
//                            Log.d(TAG, "onAnimationStart")
//                            movableSquare.x = fromSquare.x
//                            movableSquare.y = fromSquare.y
////                            setSquare(movableSquare, move.valueAtStart)
//                            movableSquare.visibility = View.VISIBLE
//                        }
//                    })
//                }
//
//            movableSquare.startAnimation(ta)
        }

        board.isEnabled = true
    }

    companion object {
        private const val TAG = "TAG.MainViewFragment"
        private const val PREFERENCES_NAME =
            "com.bpapps.ex2048clone.view.preferences_name"
        private const val PREFERENCES_BEST_SCORE =
            "com.bpapps.ex2048clone.view.preferences_best_score"
    }

    override fun onScoreUpdated(score: Int) {
        tvScore.text = score.toString()
    }

    override fun onBestScoreUpdated(bestScore: Int) {
        tvBestScore.text = bestScore.toString()
    }

    override fun onRandomAdded(coordinate: Coordinate, valueToSet: Int) {
        setSquare(squares[coordinate.row][coordinate.col], valueToSet)
    }

    override fun onGameOver() {
        AlertDialog.Builder(requireContext()).setTitle("Game Over!!")
            .setMessage("Push 'NEW GAME' to start again.")
            .setPositiveButton("OK") { dialogInterface, _ ->
                dialogInterface.dismiss()

            }
            .create()
            .show()
    }
}
