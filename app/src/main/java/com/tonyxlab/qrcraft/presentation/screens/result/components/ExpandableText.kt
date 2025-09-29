package com.tonyxlab.qrcraft.presentation.screens.result.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.presentation.core.utils.LocalSpacing
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme
import com.tonyxlab.qrcraft.presentation.theme.ui.ShowLess
import com.tonyxlab.qrcraft.util.Constants
import com.tonyxlab.qrcraft.util.generateLoremIpsum

@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    showMoreText: String = stringResource(R.string.btn_text_show_more),
    showLessText: String = stringResource(R.string.btn_text_show_less),
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    collapsedMaxLines: Int = Constants.DEFAULT_MINIMUM_TEXT_LINE,
    regularTextStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    showMoreTextStyle: SpanStyle = MaterialTheme.typography.labelLarge.toSpanStyle()
            .copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Black
            ),
    showLessTextStyle: SpanStyle = MaterialTheme.typography.labelLarge.toSpanStyle()
            .copy(
                    color = ShowLess,
                    fontWeight = FontWeight.Black
            )
) {

    var isExpanded by remember { mutableStateOf(false) }
    var isClickable by remember { mutableStateOf(false) }
    var lastCharIndex by remember { mutableIntStateOf(0) }

    val annotatedString =
        buildAnnotatedString {
            if (isClickable) {
                // Overflowing
                if (isExpanded) {
                    // Expanded
                    append(text)
                    withLink(
                            link =
                                LinkAnnotation.Clickable(
                                        tag = "Show Less",
                                        linkInteractionListener = { isExpanded = isExpanded.not() },
                                ),
                    ) {
                        withStyle(showLessTextStyle) {

                            append("\n$showLessText")
                        }
                    }
                } else {
                    // Not Expanded
                    val adjustedText =
                        text.substring(startIndex = 0, endIndex = lastCharIndex)
                                // creates space for show more text ...
                                .dropLast(showMoreText.length)
                                .dropLastWhile { Character.isWhitespace(it) || it == '.' }

                    append(adjustedText)
                    append("...")
                    withLink(
                            link =
                                LinkAnnotation.Clickable(
                                        tag = "Show More",
                                        linkInteractionListener = { isExpanded = isExpanded.not() },
                                ),
                    ) {
                        withStyle(style = showMoreTextStyle) { append("\n$showMoreText") }
                    }
                }
            } else {
                // if not overflowing return the echo text as-is
                append(text)
            }
        }

    Text(
            modifier = modifier
                    .fillMaxWidth()
                    .animateContentSize(),
            text = annotatedString,
            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLines,
            color = textColor,
            style = regularTextStyle,

            // callback that is executed when a new text layout is calculated
            onTextLayout = { textLayoutResult ->

                if (isExpanded.not() and textLayoutResult.hasVisualOverflow) {
                    isClickable = true
                    lastCharIndex = textLayoutResult.getLineEnd(collapsedMaxLines - 1)
                }
            },
    )
}

@PreviewLightDark
@Composable
private fun ExpandablePreview() {
    val spacing = LocalSpacing.current
    QRCraftTheme {
        Column(
                modifier =
                    Modifier
                            .padding(spacing.spaceMedium)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                verticalArrangement = Arrangement.spacedBy(spacing.spaceMedium),
        ) {
            ExpandableText(text = generateLoremIpsum(15))

            ExpandableText(text = generateLoremIpsum(135))

            ExpandableText(text = generateLoremIpsum(135))

            ExpandableText(text = generateLoremIpsum(199))

            ExpandableText(text = generateLoremIpsum(0))
        }
    }
}
