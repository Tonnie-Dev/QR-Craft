package com.tonyxlab.qrcraft.presentation.screens.scan.handling

import com.tonyxlab.qrcraft.presentation.core.base.handling.UiState

data class ScanUiState(
    val isLoading: Boolean = false,
    val isFlashLightOn: Boolean = false,
    val camSnackbarShown: Boolean? = null,
) : UiState