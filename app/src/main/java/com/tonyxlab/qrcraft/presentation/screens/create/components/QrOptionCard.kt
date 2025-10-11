package com.tonyxlab.qrcraft.presentation.screens.create.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.domain.QrDataType
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.theme.ui.Contact
import com.tonyxlab.qrcraft.presentation.theme.ui.ContactBg
import com.tonyxlab.qrcraft.presentation.theme.ui.Geo
import com.tonyxlab.qrcraft.presentation.theme.ui.GeoBg
import com.tonyxlab.qrcraft.presentation.theme.ui.Link
import com.tonyxlab.qrcraft.presentation.theme.ui.LinkBg
import com.tonyxlab.qrcraft.presentation.theme.ui.Phone
import com.tonyxlab.qrcraft.presentation.theme.ui.PhoneBg
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme
import com.tonyxlab.qrcraft.presentation.theme.ui.TextAccent
import com.tonyxlab.qrcraft.presentation.theme.ui.TextAccentBg
import com.tonyxlab.qrcraft.presentation.theme.ui.WiFi
import com.tonyxlab.qrcraft.presentation.theme.ui.WiFiBg

@Composable
fun QrOptionCard(
    qrTypeUi: QrTypeUi,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val shape = MaterialTheme.shapes.large

    Card(
            shape = shape,
            colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
    ) {

        Column(
                modifier = modifier
                        .fillMaxWidth()
                        .clickable { onClick() }
                        .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                        .padding(vertical = MaterialTheme.spacing.spaceTen * 2),

                horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                    modifier = Modifier
                            .clip(CircleShape)
                            .background(color = qrTypeUi.tintBg, shape = CircleShape)
                            .size(MaterialTheme.spacing.spaceLarge),
                    contentAlignment = Alignment.Center
            ) {

                Icon(
                        painter = painterResource(qrTypeUi.iconRes),
                        contentDescription = stringResource(qrTypeUi.label),
                        tint = qrTypeUi.tint
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceTwelve))
            Text(
                    text = stringResource(qrTypeUi.label),
                    style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.onSurface
                    )
            )
        }
    }
}

data class QrTypeUi(
    @StringRes
    val label: Int,
    @DrawableRes
    val iconRes: Int,
    val tint: Color,
    val tintBg: Color
)

private fun QrDataType.toUi(): QrTypeUi {

    return when (this) {
        QrDataType.TEXT -> QrTypeUi(
                label = R.string.lab_text_text,
                iconRes = R.drawable.icon_text,
                tint = TextAccent,
                tintBg = TextAccentBg
        )

        QrDataType.LINK -> QrTypeUi(
                label = R.string.lab_text_link,
                iconRes = R.drawable.icon_link,
                tint = Link,
                tintBg = LinkBg
        )

        QrDataType.CONTACT -> QrTypeUi(
                label = R.string.lab_text_contact,
                iconRes = R.drawable.icon_contact,
                tint = Contact,
                tintBg = ContactBg
        )

        QrDataType.PHONE_NUMBER -> QrTypeUi(
                label = R.string.lab_text_phone,
                iconRes = R.drawable.icon_phone,
                tint = Phone,
                tintBg = PhoneBg
        )

        QrDataType.GEOLOCATION -> QrTypeUi(
                label = R.string.lab_text_geo,
                iconRes = R.drawable.icon_geo,
                tint = Geo,
                tintBg = GeoBg
        )

        QrDataType.WIFI -> QrTypeUi(
                label = R.string.lab_text_wifi,
                iconRes = R.drawable.icon_wifi,
                tint = WiFi,
                tintBg = WiFiBg
        )
    }
}

@PreviewLightDark
@Composable
private fun QrOptionCard_Preview() {

    QRCraftTheme {

        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = MaterialTheme.spacing.spaceLarge)
                        .padding(vertical = MaterialTheme.spacing.spaceExtraLarge),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {

            QrOptionCard(
                    qrTypeUi = QrTypeUi(
                            label = R.string.lab_text_text,
                            iconRes = R.drawable.icon_text,
                            tint = TextAccent,
                            tintBg = TextAccentBg
                    ),
                    onClick = {}
            )

            QrOptionCard(
                    qrTypeUi = QrTypeUi(
                            label = R.string.lab_text_link,
                            iconRes = R.drawable.icon_link,
                            tint = Link,
                            tintBg = LinkBg
                    ),
                    onClick = {}
            )
            QrOptionCard(
                    qrTypeUi = QrTypeUi(
                            label = R.string.lab_text_contact,
                            iconRes = R.drawable.icon_contact,
                            tint = Contact,
                            tintBg = ContactBg
                    ),
                    onClick = {}
            )

            QrOptionCard(
                    qrTypeUi = QrTypeUi(
                            label = R.string.lab_text_phone,
                            iconRes = R.drawable.icon_phone,
                            tint = Phone,
                            tintBg = PhoneBg
                    ),
                    onClick = {}
            )

            QrOptionCard(
                    qrTypeUi = QrTypeUi(
                            label = R.string.lab_text_geo,
                            iconRes = R.drawable.icon_geo,
                            tint = Geo,
                            tintBg = GeoBg
                    ),
                    onClick = {}
            )

            QrOptionCard(
                    qrTypeUi = QrTypeUi(
                            label = R.string.lab_text_wifi,
                            iconRes = R.drawable.icon_wifi,
                            tint = WiFi,
                            tintBg = WiFiBg
                    ),
                    onClick = {}
            )
        }
    }
}