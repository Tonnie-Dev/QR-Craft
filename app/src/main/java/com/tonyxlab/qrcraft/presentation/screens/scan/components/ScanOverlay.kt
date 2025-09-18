package com.tonyxlab.qrcraft.presentation.screens.scan.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.min

@Composable
fun ScanOverlay(
    modifier: Modifier = Modifier,
    hint: String
) {
    val cfg = LocalConfiguration.current
    val minSide = min(cfg.screenWidthDp, cfg.screenHeightDp).dp
    // Frame size: ~70% of the min dimension (looks good across phones)
    val frameSize = minSide * 0.70f
    val cornerStroke = with(LocalDensity.current) { 4.dp.toPx() }
    val cornerLen = with(LocalDensity.current) { (frameSize * 0.12f).toPx() }

    val frameColor = MaterialTheme.colorScheme.primary
    val scrimColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.65f)
    val hintColor = MaterialTheme.colorScheme.onBackground

    // Laser animation
    val infinite = rememberInfiniteTransition(label = "laser")
    val t by infinite.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1600, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
            ),
            label = "laserProgress"
    )

    Box(modifier = modifier) {
        // 2.1 Scrim with a clear rounded-rect hole
        // (Falls back to simple translucent overlay on very old API)
        Canvas(Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height
            val fw = frameSize.toPx()
            val fh = fw // square
            val left = (w - fw) / 2f
            val top = (h - fh) / 2f
            val r = 24.dp.toPx()

            // Draw scrim
            drawRect(scrimColor)

            // Punch a transparent hole for the frame (BlendMode.Clear available API 29+)
            drawRoundRect(
                    color = Color.Transparent,
                    topLeft = Offset(left, top),
                    size = Size(fw, fh),
                    cornerRadius = CornerRadius(r, r),
                    blendMode = BlendMode.Clear
            )

            // Corner guides
            fun corner(from: Offset, dx: Float, dy: Float) {
                drawLine(
                        color = frameColor,
                        start = from,
                        end = Offset(from.x + dx, from.y),
                        strokeWidth = cornerStroke,
                        cap = StrokeCap.Round
                )
                drawLine(
                        color = frameColor,
                        start = from,
                        end = Offset(from.x, from.y + dy),
                        strokeWidth = cornerStroke,
                        cap = StrokeCap.Round
                )
            }

            // TL, TR, BR, BL
            corner(Offset(left, top), cornerLen, 0f)
            corner(Offset(left, top), 0f, cornerLen)

            corner(Offset(left + fw, top), -cornerLen, 0f)
            corner(Offset(left + fw, top), 0f, cornerLen)

            corner(Offset(left + fw, top + fh), -cornerLen, 0f)
            corner(Offset(left + fw, top + fh), 0f, -cornerLen)

            corner(Offset(left, top + fh), cornerLen, 0f)
            corner(Offset(left, top + fh), 0f, -cornerLen)

            // Animated scanning "laser"
            val y = top + (fh * t)
            drawLine(
                    color = frameColor.copy(alpha = 0.95f),
                    start = Offset(left + 12.dp.toPx(), y),
                    end = Offset(left + fw - 12.dp.toPx(), y),
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round
            )
        }

        // 2.2 Hint text above the frame
        Column(
                Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
        ) {
            Spacer(Modifier.height(32.dp))
            Text(
                    text = hint,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center
            )
        }
    }
}
