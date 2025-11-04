package com.tonyxlab.qrcraft.presentation.screens.scan.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiEvent
import com.tonyxlab.qrcraft.presentation.theme.ui.OnOverlay
import com.tonyxlab.qrcraft.presentation.theme.ui.Overlay
import com.tonyxlab.qrcraft.presentation.theme.ui.Primary
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme
import com.tonyxlab.qrcraft.util.Constants
import kotlin.math.min

@Composable
fun ScanOverlay(
    isLoading: Boolean,
    isFlashLightOn: Boolean,
    onEvent: (ScanUiEvent) -> Unit,
    onSelectImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strokeWidth = MaterialTheme.spacing.spaceSmall.value

    Box(modifier = modifier, contentAlignment = Alignment.TopCenter) {
        Canvas(
                modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            compositingStrategy = CompositingStrategy.Offscreen
                        }
        ) {

            val w = size.width
            val h = size.height

            // Using a constant of 0.75f
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
                    strokeWidth = strokeWidth,
                    sideDimen = sideDimen
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
        FlashLightOverlay(
                isFlashLightOn = isFlashLightOn,
                onEvent = onEvent,
                onSelectImage = onSelectImage
        )
    }
}

fun DrawScope.drawCornerGuides(
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
    strokeWidth: Float,
    sideDimen: Float
) {

    val handleLength = .2f * sideDimen
    val curveRadius = 40f

    // Draw Top-Left Corner Guide Handle
    drawPath(
            path = Path().apply {
                moveTo(x = left, y = top + handleLength)
                lineTo(x = left, y = top + curveRadius)
                quadraticTo(x1 = left, y1 = top, x2 = left + curveRadius, y2 = top)
                lineTo(x = left + handleLength, y = top)

            },
            color = Primary,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
    )

    // Draw Top-Right Corner Guide Handle
    drawPath(
            path = Path().apply {
                moveTo(x = right - handleLength, y = top)
                lineTo(x = right - curveRadius, y = top)
                quadraticTo(x1 = right, y1 = top, x2 = right, y2 = top + curveRadius)
                lineTo(x = right, y = top + handleLength)
            },
            color = Primary,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
    )

    // Draw Bottom-Left Corner Guide Handle
    drawPath(
            path = Path().apply {
                moveTo(x = left, y = bottom - handleLength)
                lineTo(x = left, y = bottom - curveRadius)
                quadraticTo(x1 = left, y1 = bottom, x2 = left + curveRadius, y2 = bottom)
                lineTo(x = left + handleLength, y = bottom)
            },
            color = Primary,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
    )

    // Draw Bottom-Right Corner Guide Handle
    drawPath(
            path = Path().apply {
                moveTo(x = right - handleLength, y = bottom)
                lineTo(x = right - curveRadius, y = bottom)
                quadraticTo(x1 = right, y1 = bottom, x2 = right, y2 = bottom - curveRadius)
                lineTo(x = right, y = bottom - handleLength)
            },
            color = Primary,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
    )
}

@Composable
private fun FlashLightOverlay(
    isFlashLightOn: Boolean,
    onEvent: (ScanUiEvent) -> Unit,
    onSelectImage: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = MaterialTheme.spacing.spaceDoubleDp * 22
) {

    val scale by animateFloatAsState(
            targetValue = if (isFlashLightOn) 1.2f else 1f,
            animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing),
            label = "flashScaleAnim"
    )

    val glowAlpha by animateFloatAsState(
            targetValue = if (isFlashLightOn) 0.5f else 0f,
            animationSpec = tween(durationMillis = 250),
            label = "flashGlowAnim"
    )

    val (painter, contentDescription) = when {
        isFlashLightOn ->
            painterResource(R.drawable.flash_on) to
                    stringResource(id = R.string.cds_text_flash_on)

        else ->
            painterResource(R.drawable.flash_off) to
                    stringResource(id = R.string.cds_text_flash_off)
    }

    Row(
            modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.spaceLarge)
                    .padding(vertical = MaterialTheme.spacing.spaceLarge),
            horizontalArrangement = Arrangement.SpaceBetween,
    ) {

        Box(
                modifier = Modifier
                        .size(iconSize)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .clickable { onEvent(ScanUiEvent.ToggleTorch) },
                contentAlignment = Alignment.Center

        ) {

            Canvas(modifier = Modifier.matchParentSize()) {
                if (glowAlpha > 0f) {
                    drawCircle(
                            color = Color.Yellow.copy(alpha = glowAlpha),
                            radius = size.minDimension / 2,
                            center = center
                    )
                }
            }

            Image(
                    painter = painter,
                    contentDescription = contentDescription,
                    modifier = Modifier.fillMaxSize()
            )
        }

        Image(
                modifier = Modifier
                        .size(iconSize)
                        .clickable {
                            onSelectImage()
                        },
                painter = painterResource(R.drawable.open_gallery),
                contentDescription = stringResource(id = R.string.cds_text_open_gallery)
        )
    }
}

@Preview
@Composable
private fun ScanOverlay_Preview() {

    QRCraftTheme {
        ScanOverlay(
                modifier = Modifier.fillMaxSize(),
                isLoading = false,
                isFlashLightOn = true,
                onEvent = {},
                onSelectImage = {},
        )
    }
}
