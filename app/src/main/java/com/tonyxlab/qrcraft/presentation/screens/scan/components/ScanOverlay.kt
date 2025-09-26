package com.tonyxlab.qrcraft.presentation.screens.scan.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.theme.ui.OnOverlay
import com.tonyxlab.qrcraft.presentation.theme.ui.Overlay
import com.tonyxlab.qrcraft.presentation.theme.ui.Primary
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme
import com.tonyxlab.qrcraft.util.Constants
import kotlin.math.min

@Composable
fun ScanOverlay(
    modifier: Modifier = Modifier,
    isLoading: Boolean
) {
    val strokeWidth = MaterialTheme.spacing.spaceSmall.value

    Box(modifier = modifier) {
        Canvas(
                modifier = Modifier
                        .matchParentSize()
                        .graphicsLayer {

                            compositingStrategy = CompositingStrategy.Offscreen
                        }) {

            val w = size.width
            val h = size.height

            val roiFraction = Constants.SCREEN_REGION_OF_INTEREST_FRACTION

            val sideDimen = min(w, h) * roiFraction

            val left = (w - sideDimen) / 2f
            val top = (h - sideDimen) / 2f
            val right = left + sideDimen
            val bottom = top + sideDimen

            val radius = 20.dp.toPx()

            // Draw scrim
            drawRect(Overlay)

            // Punch a transparent hole for the frame
            drawRoundRect(
                    color = Color.Red,
                    topLeft = Offset(left, top),
                    size = Size(width = sideDimen, height = sideDimen),
                    cornerRadius = CornerRadius(radius, radius),
                    blendMode = BlendMode.Clear
            )

            drawCornerGuides(
                    left = left,
                    top = top,
                    right = right,
                    bottom = bottom,
                    strokeWidth = strokeWidth
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
                    text = stringResource(id = R.string.cap_text_point_camera),
                    style = MaterialTheme.typography.titleSmall.copy(color = OnOverlay),
                    textAlign = TextAlign.Center
            )
        }

        if (isLoading) {
            Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
            ) {

                CircularProgressIndicator(
                        modifier = Modifier
                                .size(MaterialTheme.spacing.spaceLarge)
                                .padding(bottom = MaterialTheme.spacing.spaceMedium),
                        color = OnOverlay
                )
                Text(
                        text = stringResource(id = R.string.cap_text_loading),
                        style = MaterialTheme.typography.bodyLarge.copy(
                                color = OnOverlay
                        )
                )
            }
        }
    }
}

fun DrawScope.drawCornerGuides(
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
    strokeWidth: Float,
    sideDimen: Float = 0f
) {

    val handleLength = 0.15f * sideDimen
    val curveRadius = 25f
    // Draw Top-Left Corner Guide Handle
    drawPath(
            path = Path().apply {
                moveTo(x = left, y = top + 150)
                lineTo(x = left, y = top + 40)
                quadraticTo(x1 = left + 5, y1 = top, x2 = left + 50, y2 = top)
                lineTo(x = left + 150, y = top)

            },
            color = Primary,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
    )
    // Draw Top-Right Corner Guide Handle

    drawPath(
            path = Path().apply {
                moveTo(x = right - 150, y = top)
                lineTo(x = right - 40, y = top)
                quadraticTo(x1 = right + 5, y1 = top + 5, x2 = right, y2 = top + 50)
                lineTo(x = right, y = top + 150)
            },
            color = Primary,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
    )
    // Draw Bottom-Left Corner Guide Handle

    drawPath(
            path = Path().apply {
                moveTo(x =left, y = bottom-150)
                lineTo(x = left , y = bottom-40)
                quadraticTo(x1 =left + 5, y1 = bottom , x2 = left + 50, y2 = bottom )
                lineTo(x = left + 150, y = bottom )
            },
            color = Primary,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
    )
    // Draw Bottom-Right Corner Guide Handle
    drawPath(
            path = Path().apply {
                moveTo(x = right - 150, y = bottom)
                lineTo(x = right - 40, y = bottom)
                quadraticTo(x1 = right -5, y1 = bottom , x2 = right , y2 = bottom - 50)
                lineTo(x = right, y = bottom - 150)
            },
            color = Primary,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
    )
}

@Preview
@Composable
private fun ScanOverlay_Preview() {

    QRCraftTheme {

        ScanOverlay(
                modifier = Modifier.fillMaxSize(),
                isLoading = false
        )
    }

}
