package com.tonyxlab.qrcraft.presentation.screens.result.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
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
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultUiEvent
import com.tonyxlab.qrcraft.presentation.theme.ui.Overlay
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme

@Composable
fun EditableText(
    textFieldState: TextFieldState,
    placeHolderText: String,
    isEditing: Boolean,
    onEvent: (ResultUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium
) {
    val isEmpty = textFieldState.text.isEmpty()

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(isEditing) {
        if (isEditing) {
            focusRequester.requestFocus()
        }
    }
    if (isEditing) {

        Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            BasicTextField(
                    modifier = Modifier
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
                    }
            )
        }
    } else {

        Text(
                modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                        ) { onEvent(ResultUiEvent.EditDetectedContent) },
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
                    .fillMaxWidth(), contentAlignment = Alignment.Center

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
                    onEvent = {}
            )
            EditableText(
                    modifier = Modifier.fillMaxWidth(),
                    textFieldState = filledTextField,
                    placeHolderText = "Text",
                    isEditing = false,
                    onEvent = {}
            )
            EditableText(
                    modifier = Modifier.fillMaxWidth(),
                    textFieldState = emptyTextField,
                    placeHolderText = "Text",
                    isEditing = true,
                    onEvent = {}
            )
            EditableText(
                    modifier = Modifier.fillMaxWidth(),
                    textFieldState = filledTextField,
                    placeHolderText = "Text",
                    isEditing = true,
                    onEvent = {}
            )
        }
    }
}

