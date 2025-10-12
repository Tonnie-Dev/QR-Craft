package com.tonyxlab.qrcraft.presentation.theme.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

val customMaterialShapes = Shapes(
        // Pre-Defined M3 Shapes
        extraSmall = RoundedCornerShape(4.dp),
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(16.dp),

        // Extra-Large Override
        extraLarge = RoundedCornerShape(20.dp)

)

object ExtendedShapes {

    val RoundedCornerShape100 = RoundedCornerShape(100.dp)
}

val Shapes.RoundedCornerShape100
    @Composable
    get() = ExtendedShapes.RoundedCornerShape100
