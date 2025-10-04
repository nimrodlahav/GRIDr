package com.example.grider.components

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlin.math.min

@Composable
fun UploadImageView(
    selectedImageUri: Uri?,
    rows: Int,
    cols: Int,
    paperWidthCm: Float,
    paperHeightCm: Float
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        // ---------------- Uploaded image with grid ----------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = "Uploaded Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
                GridOverlay(rows, cols)
            } else {
                Text("No Image", color = Color.DarkGray)
            }
        }

        // ---------------- Sheet preview with grid + partition ----------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Scale factors (usable area maintaining aspect ratio of image)
                    val scale = min(size.width / paperWidthCm, size.height / paperHeightCm)
                    val usableWidth = paperWidthCm * scale
                    val usableHeight = paperHeightCm * scale

                    // Draw brighter usable area
                    drawRect(
                        color = Color.White,
                        topLeft = Offset((size.width - usableWidth) / 2, (size.height - usableHeight) / 2),
                        size = androidx.compose.ui.geometry.Size(usableWidth, usableHeight)
                    )

                    // Draw darker unused area
                    drawRect(
                        color = Color.Gray.copy(alpha = 0.2f),
                        topLeft = Offset.Zero,
                        size = size
                    )

                    // Partition line at bottom of usable area
                    val bottomY = (size.height - usableHeight) / 2 + usableHeight
                    drawLine(
                        color = Color.Red,
                        start = Offset(0f, bottomY),
                        end = Offset(size.width, bottomY),
                        strokeWidth = 3f
                    )

                    // Draw grid inside usable area
                    if (rows > 0 && cols > 0) {
                        val cellW = usableWidth / cols
                        val cellH = usableHeight / rows

                        for (c in 1 until cols) {
                            drawLine(
                                color = Color.Black,
                                start = Offset((size.width - usableWidth) / 2 + c * cellW, (size.height - usableHeight) / 2),
                                end = Offset((size.width - usableWidth) / 2 + c * cellW, (size.height - usableHeight) / 2 + usableHeight),
                                strokeWidth = 2f
                            )
                        }
                        for (r in 1 until rows) {
                            drawLine(
                                color = Color.Black,
                                start = Offset((size.width - usableWidth) / 2, (size.height - usableHeight) / 2 + r * cellH),
                                end = Offset((size.width - usableWidth) / 2 + usableWidth, (size.height - usableHeight) / 2 + r * cellH),
                                strokeWidth = 2f
                            )
                        }

                        // Labels outside sheet
                        val cellWidthCm = paperWidthCm / cols
                        val cellHeightCm = paperHeightCm / rows
                        val unusedHeightCm = paperHeightCm - (usableHeight / scale)

                        drawContext.canvas.nativeCanvas.apply {
                            drawText(
                                "${"%.1f".format(cellWidthCm)} cm",
                                size.width / 2,
                                (size.height - usableHeight) / 2 - 10,
                                android.graphics.Paint().apply {
                                    color = android.graphics.Color.BLACK
                                    textAlign = android.graphics.Paint.Align.CENTER
                                    textSize = 32f
                                    alpha = 150 // softer
                                }
                            )
                            drawText(
                                "${"%.1f".format(cellHeightCm)} cm",
                                (size.width - usableWidth) / 2 - 40,
                                size.height / 2,
                                android.graphics.Paint().apply {
                                    color = android.graphics.Color.BLACK
                                    textAlign = android.graphics.Paint.Align.CENTER
                                    textSize = 32f
                                    alpha = 150
                                }
                            )
                            drawText(
                                "Unused: ${"%.1f".format(unusedHeightCm)} cm",
                                size.width / 2,
                                bottomY + 40,
                                android.graphics.Paint().apply {
                                    color = android.graphics.Color.BLACK
                                    textAlign = android.graphics.Paint.Align.CENTER
                                    textSize = 32f
                                    alpha = 150
                                }
                            )
                        }
                    }
                }
            } else {
                Text("No Sheet", color = Color.Gray)
            }
        }
    }
}
