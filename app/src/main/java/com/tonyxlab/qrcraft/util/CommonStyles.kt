package com.tonyxlab.qrcraft.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.tonyxlab.qrcraft.presentation.core.utils.spacing

@Composable
fun Modifier.getTintedIconModifier(tintBg: Color) = this
        .clip(CircleShape)
        .background(color = tintBg, shape = CircleShape)
        .size(MaterialTheme.spacing.spaceLarge)
