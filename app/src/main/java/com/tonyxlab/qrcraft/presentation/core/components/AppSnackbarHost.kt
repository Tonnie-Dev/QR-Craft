package com.tonyxlab.qrcraft.presentation.core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.theme.ui.Success
import com.tonyxlab.qrcraft.utils.ifThen

@Composable
fun AppSnackbarHost(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {

    val containerColor =
        if (isError) MaterialTheme.colorScheme.error else Success
    val contentColor = contentColorFor(containerColor)

    val configuration = LocalWindowInfo.current.containerSize
    val screenWidthDp = configuration.width.dp

    SnackbarHost(
            modifier = modifier
                    .padding(MaterialTheme.spacing.spaceMedium)
                    .navigationBarsPadding(),
            hostState = snackbarHostState
    )
    { snackbarData ->

        val actionLabelActive = !snackbarData.visuals.actionLabel.isNullOrBlank()

        Snackbar(
                modifier = Modifier.widthIn(
                        max = minOf(screenWidthDp * .6f , 400.dp)
                ).padding(horizontal = MaterialTheme.spacing.spaceMedium),
                       /* .ifThen(actionLabelActive) { fillMaxWidth() }
                        .ifThen(!actionLabelActive) { fillMaxWidth(.8f) },*/
                shape = MaterialTheme.shapes.small,
                contentColor = contentColor,
                containerColor = containerColor
        ) {
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
            ) {

                Icon(
                        modifier = Modifier.padding(end = MaterialTheme.spacing.spaceSmall),
                        imageVector = if (isError) Icons.Default.Error else Icons.Default.Check,
                        contentDescription = stringResource(id = R.string.cds_text_check_mark),
                        tint = contentColor
                )
                Text(
                        text = snackbarData.visuals.message,
                        style = MaterialTheme.typography.labelLarge.copy(
                                color = contentColor
                        )
                )

                if (actionLabelActive) {
                    Text(
                            modifier = Modifier
                                    .clickable { snackbarData.performAction() }
                                    .padding(start = MaterialTheme.spacing.spaceLarge),
                            text = snackbarData.visuals.actionLabel!!,
                            style = MaterialTheme.typography.labelLarge.copy(
                                    color = contentColor,
                                    fontWeight = FontWeight.SemiBold
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun ShowAppSnackbar(
    triggerId: Int,
    snackbarHostState: SnackbarHostState,
    message: String,
    actionLabel: String = "",
    onActionClick: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    LaunchedEffect(triggerId) {
        if (triggerId > 0) {

            val result = snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel,
                    duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                onActionClick()
            }
            onDismiss()
        }
    }
}

class SnackbarController<T> {

    var triggerId by mutableIntStateOf(0)
    var message by mutableStateOf("")
    var actionLabel by mutableStateOf("")
    var actionEvent by mutableStateOf<T?>(null)

    fun showSnackbar(
        message: String,
        actionLabel: String = "",
        actionEvent: T? = null
    ) {
        this.message = message
        this.actionLabel = actionLabel
        this.actionEvent = actionEvent
        triggerId++
    }

    fun dismissSnackbar() {
        triggerId = 0
        actionEvent = null
    }

}

@Composable
fun <T> rememberSnackbarController(): SnackbarController<T> {
    return remember { SnackbarController() }
}

