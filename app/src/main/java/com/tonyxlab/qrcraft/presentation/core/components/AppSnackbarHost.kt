package com.tonyxlab.qrcraft.presentation.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.theme.ui.Success

@Composable
fun AppSnackbarHost(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {

    SnackbarHost(
            modifier = modifier
                    .padding(MaterialTheme.spacing.spaceMedium)
                    .padding(vertical = MaterialTheme.spacing.spaceExtraSmall)
                    .navigationBarsPadding(),
            hostState = snackbarHostState
    )
    {
        Snackbar(
                modifier = Modifier.fillMaxWidth(.8f),
                shape = MaterialTheme.shapes.small,
                contentColor = MaterialTheme.colorScheme.onSurface,
                containerColor = Success
        ) {
            Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
            ) {

                Icon(
                        modifier = Modifier.padding(end = MaterialTheme.spacing.spaceSmall),
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(id = R.string.cds_text_check_mark)
                )
                Text(
                        text = stringResource(id = R.string.snack_text_camera_perm_granted),
                        style = MaterialTheme.typography.labelLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface
                        )
                )
            }
        }
    }
}

