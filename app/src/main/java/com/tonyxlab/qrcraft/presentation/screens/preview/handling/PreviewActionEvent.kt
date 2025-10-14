package com.tonyxlab.qrcraft.presentation.screens.preview.handling

import com.tonyxlab.qrcraft.presentation.core.base.handling.ActionEvent

sealed interface PreviewActionEvent : ActionEvent{


    data object NavigateToEntryScreen: PreviewActionEvent

}