package com.tonyxlab.qrcraft.presentation.screens.scan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.theme.ui.OnOverlay
import com.tonyxlab.qrcraft.presentation.theme.ui.Overlay
import kotlin.io.path.Path
import kotlin.math.min

/*@Composable
fun ScanOverlayTwo(modifier: Modifier = Modifier) {

    Column(
            modifier = modifier
                    .fillMaxSize()
                    .background(color = Overlay),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Point Camera", color = OnOverlay)
        Box(
                modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .fillMaxHeight(0.4f)
                        .aspectRatio(1f)
                        .background(color = Color.Transparent, shape = MaterialTheme.shapes.medium)
                        .padding(top = MaterialTheme.spacing.spaceMedium)
                        .padding(start = MaterialTheme.spacing.spaceMedium)
                        .padding(end = MaterialTheme.spacing.spaceMedium)
        )
    }
}*/

@Composable
fun ScanOverlayTwo(
    modifier: Modifier = Modifier,
    holeSizeFraction: Float = .9f,       // 0.55–0.72 works nicely
    cornerRadius: Dp = 20.dp,
    scrimColor: Color = Overlay,           // your dim color
    borderColor: Color = MaterialTheme.colorScheme.primary,
    borderWidth: Dp = 3.dp
) {
    Box(
            modifier = modifier
                    .fillMaxSize()
                    .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            }
                    .drawWithContent {


                        // Draw your normal content (e.g., hint text) on top later
                        //drawContent()

                        // Compute centered square
                        val w = size.width
                        val h = size.height
                        val dim = min(w, h) * holeSizeFraction
                        val left = (w - dim) / 2f
                        val top = (h - dim) / 2f
                        val rr = RoundRect(
                                left = left,
                                top = top,
                                right = left + dim,
                                bottom = top + dim,
                                cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())
                        )

                        // Build a path for the hole
                        val path = Path().apply { addRoundRect(rr) }

                        // 1) Draw the scrim ONLY outside the hole (Difference clip)
                        clipPath(path, clipOp = ClipOp.Difference) {
                            drawRect(color = scrimColor)
                        }

                        // 2) Optional: draw a neat border around the hole
                        drawRoundRect(
                                color = borderColor,
                                topLeft = Offset(left, top),
                                size = Size(dim, dim),
                                cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx()),
                                style = Stroke(width = borderWidth.toPx())
                        )
                    }
    ) {
        // Hint text
        Text(
                text = "Point your camera at a QR code",
                color = OnOverlay,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 32.dp, start = 24.dp, end = 24.dp)
        )
    }
}

