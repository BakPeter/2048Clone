package com.bpapps.ex2048clone.model

import androidx.annotation.IntDef
import com.bpapps.ex2048clone.model.MoveOrientation.Companion.HORIZONTAL_MOVE
import com.bpapps.ex2048clone.model.MoveOrientation.Companion.VERTICAL_MOVE

@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.SOURCE)
@IntDef(HORIZONTAL_MOVE, VERTICAL_MOVE)
annotation class MoveOrientation {
    companion object {
        const val HORIZONTAL_MOVE = 1
        const val VERTICAL_MOVE = 2

    }
}