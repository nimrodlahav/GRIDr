package com.example.grider.utils

fun calculateUsableArea(
    imageWidth: Int,
    imageHeight: Int,
    paperWidth: Int,
    paperHeight: Int
): Pair<Float, Float> {
    val imageAspect = imageWidth.toFloat() / imageHeight.toFloat()
    val paperAspect = paperWidth.toFloat() / paperHeight.toFloat()

    return if (imageAspect > paperAspect) {
        // Fit by width
        paperWidth.toFloat() to (paperWidth / imageAspect)
    } else {
        // Fit by height
        (paperHeight * imageAspect) to paperHeight.toFloat()
    }
}
