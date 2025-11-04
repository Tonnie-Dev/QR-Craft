package com.tonyxlab.qrcraft.presentation.screens.scan.handling

import android.net.Uri
import com.tonyxlab.qrcraft.presentation.core.base.handling.UiState

data class ScanUiState(
    val isLoading: Boolean = false,
    val isFlashLightOn: Boolean = false,
    val camSnackbarShown: Boolean? = null,
    val imageUri: Uri? = null,
    val showDialog: Boolean = false
) : UiState