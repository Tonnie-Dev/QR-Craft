package com.tonyxlab.qrcraft.presentation.theme.ui

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tonyxlab.qrcraft.R

val SuseFontFamily = FontFamily(Font(R.font.suse_variable))

val Typography = Typography(

        titleMedium = TextStyle(
                fontFamily = SuseFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                lineHeight = 32.sp

        ),
        titleSmall = TextStyle(
                fontFamily = SuseFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 19.sp,
                lineHeight = 24.sp
        ),
        labelLarge = TextStyle(
                fontFamily = SuseFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 20.sp
        ),
        bodyLarge = TextStyle(
                fontFamily = SuseFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 20.sp
        )
)