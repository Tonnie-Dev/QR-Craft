package com.tonyxlab.qrcraft.utils

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

inline fun Modifier.ifThen(flag: Boolean, modifierBuilder: Modifier.() -> Modifier): Modifier =
    if (flag) this.modifierBuilder() else this



@Composable
fun SetStatusBarIconsColor(darkIcons: Boolean) {

    val view = LocalView.current

    val insetsController = remember(view) {
        (view.context as? Activity)
                ?.window
                ?.let { WindowCompat.getInsetsController(it, it.decorView) }
    }

    SideEffect {
        insetsController?.isAppearanceLightStatusBars = darkIcons
    }
}
