package com.tonyxlab.qrcraft.presentation.screens.scan.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tonyxlab.qrcraft.presentation.theme.ui.OnOverlay
import kotlin.math.min

@Composable
fun ScanOverlay(
    modifier: Modifier = Modifier,
    hint: String
) {

    val scrimColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.65f)

    Box(modifier = modifier) {
        Canvas(
                Modifier
                        .matchParentSize()
                        .graphicsLayer {
                            compositingStrategy = CompositingStrategy.Offscreen
                        }

        ) {

            val w = size.width
            val h = size.height

            val punchHoleFraction = .9f

            val frameSquareDimen = min(w, h) * punchHoleFraction

            val left = (w - frameSquareDimen) / 2f
            val top = (h - frameSquareDimen) / 2f

            val radius = 20.dp.toPx()

            // Draw scrim
            drawRect(scrimColor)

            // Punch a transparent hole for the frame
            drawRoundRect(
                    color = Color.Transparent,
                    topLeft = Offset(left, top),
                    size = Size(width = frameSquareDimen, height = frameSquareDimen),
                    cornerRadius = CornerRadius(radius, radius),
                    blendMode = BlendMode.Clear
            )
        }

        // Hint Text
        Column(
                Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.fillMaxHeight(0.25f))
            Text(
                    text = hint,
                    style = MaterialTheme.typography.titleSmall.copy(color = OnOverlay),
                    textAlign = TextAlign.Center
            )
        }
    }
}
