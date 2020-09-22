package com.bpapps.ex2048clone.model

class SquareMovement(
    var orientation: @MoveOrientation Int,
    val from: Coordinate,
    val to: Coordinate,
    val valueAtStart: Int,
    val merged: Boolean
) {
}