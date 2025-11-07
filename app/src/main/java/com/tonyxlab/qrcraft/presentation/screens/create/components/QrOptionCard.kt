package com.tonyxlab.qrcraft.presentation.screens.create.components

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.domain.model.QrUiType
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
import com.tonyxlab.qrcraft.utils.getTintedIconModifier

@Composable
fun QrOptionCard(
    qrUiType: QrUiType,
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
        ) {

            Box(
                    modifier = Modifier.getTintedIconModifier(tintBg = qrUiType.tintBg),
                    contentAlignment = Alignment.Center
            ) {

                Icon(
                        painter = painterResource(qrUiType.iconRes),
                        contentDescription = stringResource(qrUiType.label),
                        tint = qrUiType.tint
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceTwelve))
            Text(
                    text = stringResource(qrUiType.label),
                    style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.onSurface
                    )
            )
        }
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
                    qrUiType = QrUiType(
                            label = R.string.lab_text_text,
                            iconRes = R.drawable.icon_text,
                            tint = TextAccent,
                            tintBg = TextAccentBg
                    ),
                    onClick = {}
            )

            QrOptionCard(
                    qrUiType = QrUiType(
                            label = R.string.lab_text_link,
                            iconRes = R.drawable.icon_link,
                            tint = Link,
                            tintBg = LinkBg
                    ),
                    onClick = {}
            )
            QrOptionCard(
                    qrUiType = QrUiType(
                            label = R.string.lab_text_contact,
                            iconRes = R.drawable.icon_contact,
                            tint = Contact,
                            tintBg = ContactBg
                    ),
                    onClick = {}
            )

            QrOptionCard(
                    qrUiType = QrUiType(
                            label = R.string.lab_text_phone,
                            iconRes = R.drawable.icon_phone,
                            tint = Phone,
                            tintBg = PhoneBg
                    ),
                    onClick = {}
            )

            QrOptionCard(
                    qrUiType = QrUiType(
                            label = R.string.lab_text_geo,
                            iconRes = R.drawable.icon_geo,
                            tint = Geo,
                            tintBg = GeoBg
                    ),
                    onClick = {}
            )

            QrOptionCard(
                    qrUiType = QrUiType(
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