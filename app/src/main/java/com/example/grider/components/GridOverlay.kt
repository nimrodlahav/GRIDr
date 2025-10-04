package com.example.grider.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
fun GridOverlay(
    rows: Int,
    cols: Int,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        if (rows <= 0 || cols <= 0) return@Canvas

        val cellW = size.width / cols
        val cellH = size.height / rows

        // Vertical lines
        for (c in 1 until cols) {
            val x = cellW * c
            drawLine(
                color = Color.Black,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = 2f
            )
        }
        // Horizontal lines
        for (r in 1 until rows) {
            val y = cellH * r
            drawLine(
                color = Color.Black,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 2f
            )
        }
    }
}
