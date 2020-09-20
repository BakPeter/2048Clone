package com.bpapps.ex2048clone.model

class SquareMovement(
    val from: Coordinate,
    val to: Coordinate,
    val valueAtEnd: Int,
    val merged: Boolean
) {
}