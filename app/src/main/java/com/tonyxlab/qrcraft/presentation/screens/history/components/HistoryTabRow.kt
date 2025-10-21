package com.tonyxlab.qrcraft.presentation.screens.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.screens.history.handling.HistoryUiState
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme
import com.tonyxlab.qrcraft.util.getRandomQrDataItems
import kotlinx.coroutines.launch

@Composable
fun HistoryTabRow(
    uiState: HistoryUiState,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    indicatorColor: Color = MaterialTheme.colorScheme.onSurface,
    indicatorHeight: Dp = 2.dp,
    indicatorWidth: Dp = 180.dp,
) {

    val tabs = listOf(
            R.string.header_text_tab_scanned,
            R.string.header_text_tab_generated
    )

    val coroutineScope = rememberCoroutineScope()

    TabRow(
            modifier = modifier.padding(MaterialTheme.spacing.spaceMedium),
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(
                                currentTabPosition = tabPositions[pagerState.currentPage]
                        ),
                        color = indicatorColor,
                        height = indicatorHeight,
                        width = indicatorWidth
                )
            }
    ) {

        tabs.forEachIndexed { i, tab ->
            Tab(
                    selected = pagerState.currentPage == i,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(page = i) } },
                    text = {
                        Text(
                                text = stringResource(id = tabs[i]),
                                style = MaterialTheme.typography.labelMedium
                                        .copy(
                                                color = MaterialTheme.colorScheme.onSurface,
                                                textAlign = TextAlign.Center
                                        )
                        )
                    }
            )
        }
    }

    HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1
    ) { page ->

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(MaterialTheme.spacing.spaceMedium),
                    verticalArrangement =
                        Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
            ) {
                items(items = uiState.historyList, key = { it.id }) { qrData ->
                    HistoryListItem(qrData = qrData)
                }
            }

            Box(
                    modifier = Modifier
                            .align(alignment = Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(MaterialTheme.spacing.spaceOneHundredFifty)
                            .background(
                                    brush = Brush.verticalGradient(
                                            colors = listOf(
                                                    Color.Transparent,
                                                    MaterialTheme.colorScheme.surface.copy(alpha = .95f)
                                            )
                                    )
                            )
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun HistoryTabRow_Preview() {
    val pagerState = rememberPagerState(initialPage = 0) { 2 }

    QRCraftTheme {

        Box(
                modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.surface)
                        .fillMaxSize()
        ) {

            HistoryTabRow(
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                    uiState = HistoryUiState(historyList = getRandomQrDataItems(count = 15)),
                    pagerState = pagerState
            )
        }
    }
}