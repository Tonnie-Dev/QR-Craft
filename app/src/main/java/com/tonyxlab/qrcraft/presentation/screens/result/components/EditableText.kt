package com.tonyxlab.qrcraft.presentation.screens.result.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.theme.ui.Overlay
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme
import com.tonyxlab.qrcraft.utils.Constants.MAX_QR_DESC_CHAR_COUNT
import kotlin.math.max

@Composable
fun EditableText(
    textFieldState: TextFieldState,
    placeHolderText: String,
    isEditing: Boolean,
    onClickText: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium
) {
    val isEmpty = textFieldState.text.isEmpty()

    val focusRequester = remember { FocusRequester() }

    val charCount = textFieldState.text.length
    val remainingCharCount = max((MAX_QR_DESC_CHAR_COUNT - charCount), 0)

    LaunchedEffect(isEditing) {

        if (isEditing) {
            focusRequester.requestFocus()
        }
    }

    if (isEditing) {
        Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
        ) {
            BasicTextField(
                    modifier = modifier
                            .focusRequester(focusRequester)
                            .fillMaxWidth(),
                    state = textFieldState,
                    textStyle = textStyle.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                    decorator = { innerTextField ->
                        TextDecorator(
                                modifier = Modifier,
                                placeHolderText = placeHolderText,
                                isEmpty = isEmpty,
                                innerTextField = innerTextField
                        )
                    },
                    outputTransformation = {

                        if (length > MAX_QR_DESC_CHAR_COUNT) {
                            delete(MAX_QR_DESC_CHAR_COUNT, length)
                        }
                    }
            )

            Text(
                    modifier = Modifier.padding(MaterialTheme.spacing.spaceExtraSmall),
                    text = "$remainingCharCount/$MAX_QR_DESC_CHAR_COUNT",
                    style = MaterialTheme.typography.labelSmall.copy(
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .7f),
                            textAlign = TextAlign.Center
                    )
            )
        }
    } else {

        Text(
                modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                        ) { onClickText() },
                text = textFieldState.text.toString()
                        .ifBlank { placeHolderText },
                style = textStyle.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                )
        )
    }
}

@Composable
private fun TextDecorator(
    placeHolderText: String,
    isEmpty: Boolean,
    innerTextField: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
            modifier = modifier
                    .fillMaxWidth(),
            contentAlignment = Alignment.Center

    ) {

        if (isEmpty) {
            Text(
                    text = placeHolderText,
                    style = MaterialTheme.typography.titleMedium.copy(
                            color = Overlay,
                            textAlign = TextAlign.Center
                    )
            )
        } else {
            innerTextField()
        }
    }
}

@PreviewLightDark
@Composable
private fun EditableText_Preview() {

    val emptyTextField = remember { TextFieldState() }
    val filledTextField = remember { TextFieldState(initialText = "Tonnie XIII") }

    QRCraftTheme {
        Column(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {
            EditableText(
                    modifier = Modifier.fillMaxWidth(),
                    textFieldState = emptyTextField,
                    placeHolderText = "Text",
                    isEditing = false,
                    onClickText = {}
            )
            EditableText(
                    modifier = Modifier.fillMaxWidth(),
                    textFieldState = filledTextField,
                    placeHolderText = "Text",
                    isEditing = false,
                    onClickText = {}
            )
            EditableText(
                    modifier = Modifier.fillMaxWidth(),
                    textFieldState = emptyTextField,
                    placeHolderText = "Text",
                    isEditing = true,
                    onClickText = {}
            )
            EditableText(
                    modifier = Modifier.fillMaxWidth(),
                    textFieldState = filledTextField,
                    placeHolderText = "Text",
                    isEditing = true,
                    onClickText = {}
            )
        }
    }
}

