package com.tonyxlab.qrcraft.util

import androidx.compose.ui.Modifier

inline fun Modifier.ifThen(flag: Boolean, modifierBuilder: Modifier.() -> Modifier): Modifier =
    if (flag) this.modifierBuilder() else this
