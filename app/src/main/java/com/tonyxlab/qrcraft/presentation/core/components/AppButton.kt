package com.tonyxlab.qrcraft.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme

@Composable
fun AppButton(
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonShape: Shape = MaterialTheme.shapes.extraLarge,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    leadingIcon: Painter? = null
) {

    Button(
            modifier = modifier,
            onClick = onClick,
            shape = buttonShape,
            colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = contentColor,
                    containerColor = containerColor,
            )
    ) {

        if (leadingIcon != null) {
            Icon(
                    modifier = Modifier
                            .size(MaterialTheme.spacing.spaceMedium),
                    painter = leadingIcon,
                    contentDescription = buttonText
            )
        }
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.spaceSmall))
        Text(
                text = buttonText,
                style = MaterialTheme.typography.labelLarge
        )
    }
}

@PreviewLightDark
@Composable
private fun AppButton_Preview() {
    QRCraftTheme {

        Column(
                modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AppButton(
                    buttonText = "Share",
                    onClick = {},
                    leadingIcon = painterResource(R.drawable.share_icon)
            )

            AppButton(
                    buttonText = "Copy",
                    onClick = {},
                    leadingIcon = painterResource(R.drawable.copy_icon)
            )


            AppButton(
                    buttonText = "Grant",
                    onClick = {},
            )


            AppButton(
                    buttonText = "Close App",
                    onClick = {},
                    contentColor = MaterialTheme.colorScheme.error
            )
        }
    }
}

