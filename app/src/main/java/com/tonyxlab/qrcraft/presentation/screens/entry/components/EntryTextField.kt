package com.tonyxlab.qrcraft.presentation.screens.entry.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme
import com.tonyxlab.qrcraft.presentation.theme.ui.RoundedCornerShape100

@Composable
fun EntryTextField(
    textFieldState: TextFieldState,
    placeholderString: String,
    modifier: Modifier = Modifier
) {

    val isTextFieldEmpty = textFieldState.text.isEmpty()
    var isFocused by remember { mutableStateOf(false) }




      BasicTextField(
                modifier = Modifier
                        .onFocusChanged { isFocused = it.isFocused },
                state = textFieldState,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorator = { innerTextField ->
                    TextFieldDecorator(
                            isFocused = isFocused,
                            innerTextField = innerTextField,
                            placeholderString = placeholderString,
                            isTextFieldEmpty = isTextFieldEmpty

                    )
                }
        )
}

@Composable
private fun TextFieldDecorator(
    innerTextField: @Composable () -> Unit,
    isFocused: Boolean,
    placeholderString: String,
    isTextFieldEmpty: Boolean,

) {

    Box(
            modifier = Modifier
                    .fillMaxWidth()
                    .background(
                           color =  MaterialTheme.colorScheme.surface,
                            shape = MaterialTheme.shapes.RoundedCornerShape100
                    )

                    .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                    .padding(vertical = MaterialTheme.spacing.spaceDoubleDp * 9)
    ) {
            if (isTextFieldEmpty && !isFocused) {
                Text(
                        text = placeholderString,
                        style = MaterialTheme.typography.bodyLarge
                )
            } else {
                innerTextField()
            }
        }
}

@PreviewLightDark
@Composable
private fun EntryTextField_Preview() {

    val emptyTextFieldState = remember { TextFieldState() }
    val filledTextFieldState = remember { TextFieldState(initialText = "Tonnie") }

    QRCraftTheme {
        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .padding(MaterialTheme.spacing.spaceMedium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {


            EntryTextField(
                    textFieldState = emptyTextFieldState,
                    placeholderString = "URL"
            )

            EntryTextField(
                    textFieldState = filledTextFieldState,
                    placeholderString = "URL"
            )

            TextField(value = "", onValueChange = {})
        }
    }
}
