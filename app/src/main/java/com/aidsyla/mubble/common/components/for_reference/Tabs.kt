package com.aidsyla.mubble.common.components.for_reference

import android.util.Log
import android.view.ContextThemeWrapper
import android.view.ViewGroup
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.aidsyla.mubble.R
import com.aidsyla.mubble.feature.profile.ProfileBubbleList
import com.aidsyla.mubble.feature.profile.ProfilePostGrid
import com.aidsyla.mubble.feature.profile.UserDetails
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import kotlin.math.floor

@Composable
fun ProfileSavedScreen(modifier: Modifier = Modifier) {
    HybridTabScreen(
        tabTitles = listOf("Posts", "Bubbles"),
        tabContent = listOf(
            { ProfilePostGrid() },
            { ProfileBubbleList() }
        )
    ) {}
}

@Composable
fun HybridTabScreen(
    modifier: Modifier = Modifier,
    tabTitles: List<String> = listOf("Posts", "Bubbles", "Saved"),
    tabContent: List<@Composable () -> Unit> = listOf(
        { ProfilePostGrid() },
        { ProfileBubbleList() },
        { ProfileSavedScreen() }
    ),
    header: @Composable () -> Unit,
) {
    require(tabTitles.size == tabContent.size) { "Tab titles and content must have the same size" }

    val pagerState = rememberPagerState(pageCount = { tabContent.size })
    val coroutineScope = rememberCoroutineScope()
    val tabLayoutRef = remember { mutableStateOf<TabLayout?>(null) }

    BoxWithConstraints(
    ) {
        val screenHeight = maxHeight
        val scrollState = rememberScrollState()
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            header()
            Column(
                Modifier.height(screenHeight)
            ) {
                val primary = MaterialTheme.colorScheme.primary.toArgb()
                val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()
                AndroidView(
                    factory = { context ->
                        TabLayout(context).apply {
                            tabTitles.forEach { addTab(newTab().setText(it)) }
                            tabLayoutRef.value = this
                            setSelectedTabIndicatorColor(primary)
                            setTabTextColors(onSurfaceVariant, primary)
                            setBackgroundColor(Color.Transparent.toArgb())
                        }
                    },
                    modifier = modifier.fillMaxWidth()
                )
                HorizontalDivider()
                val nestedScrollConnection = remember {
                    object : NestedScrollConnection {
                        override fun onPreScroll(
                            available: Offset,
                            source: NestedScrollSource,
                        ): Offset {
                            return if (available.y > 0) Offset.Zero else Offset(
                                x = 0f,
                                y = -scrollState.dispatchRawDelta(-available.y)
                            )
                        }
                    }
                }
                HorizontalPager(
                    state = pagerState,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .wrapContentHeight()
                        .nestedScroll(nestedScrollConnection)
                ) { page ->
                    tabContent.getOrNull(page)?.invoke() ?: run {
                        Text("Invalid tab content", modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
    LaunchedEffect(pagerState) {
        snapshotFlow {
            Pair(
                pagerState.currentPage + pagerState.currentPageOffsetFraction,
                pagerState.currentPage
            )
        }.collect { (scrollPosition, currentPage) ->
            val adjustedPosition = scrollPosition.coerceIn(0f, (pagerState.pageCount - 1).toFloat())
            val position = adjustedPosition.toInt()
            val offset = adjustedPosition - position
            tabLayoutRef.value?.setScrollPosition(position, offset, false)
            if (pagerState.currentPageOffsetFraction == 0f) {
                debugLog("Final position: $currentPage")
                tabLayoutRef.value?.getTabAt(currentPage)?.let { tab ->
                    if (tab.position != tabLayoutRef.value?.selectedTabPosition) {
                        debugLog("Updating selected tab to $currentPage")
                        coroutineScope.launch {
                            // Use post to ensure smooth UI update
                            tabLayoutRef.value?.post {
                                tabLayoutRef.value?.selectTab(tab)
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(tabLayoutRef.value) {
        tabLayoutRef.value?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { coroutineScope.launch { pagerState.animateScrollToPage(it.position) } }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })
    }
}

@Composable
fun ComposeTabLayout(
    modifier: Modifier = Modifier,
) {
    val tabTitles = listOf("Posts", "Bubbles", "Bruh")
    val pagerState = rememberPagerState(pageCount = { tabTitles.size })
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val indicatorPage = remember { mutableIntStateOf(0) }
    val indicatorFraction = remember { mutableFloatStateOf(0f) }

    LaunchedEffect(pagerState) {
        snapshotFlow {
            pagerState.currentPage + pagerState.currentPageOffsetFraction
        }.collect { scrollPos ->
            val clamped = scrollPos.coerceIn(0f, (pagerState.pageCount).toFloat())
            val base = floor(clamped).toInt()
            indicatorPage.intValue = pagerState.currentPage
            indicatorFraction.floatValue = clamped - base
        }
    }

    BoxWithConstraints {
        val screenHeight = maxHeight

        Column(modifier.verticalScroll(scrollState)) {
            UserDetails()

            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth(),
                indicator = { tabPositions ->
                    val current = tabPositions[indicatorPage.intValue]
                    val next = tabPositions.getOrNull(indicatorPage.intValue + 1) ?: current

                    val start = lerp(0.dp, next.left, indicatorFraction.floatValue)

                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(currentTabPosition = current)
                            .offset { IntOffset(next.left.roundToPx(), 0) }
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(title) },
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .height(screenHeight)
                    .nestedScroll(remember {
                        object : NestedScrollConnection {
                            override fun onPreScroll(
                                available: Offset,
                                source: NestedScrollSource,
                            ) = if (available.y > 0) Offset.Zero
                            else Offset(x = 0f, y = -scrollState.dispatchRawDelta(-available.y))
                        }
                    }),
                verticalAlignment = Alignment.Top,
            ) { page ->
                when (page) {
                    0 -> ProfilePostGrid()
                    1 -> ProfileBubbleList()
                }
            }
        }
    }
}


@Composable
fun BasicTabbedNavigation(
    tabTitles: List<String>,
    pages: List<@Composable () -> Unit>,
    isPrimary: Boolean,
) {
    val context = LocalContext.current
    val viewPager = remember { ViewPager2(context) }
    val tabLayout = remember {
        if (isPrimary) TabLayout(context) else TabLayout(
            ContextThemeWrapper(
                context,
                R.style.Theme_Secondary_Mubble
            )
        )
    }

    val primary = MaterialTheme.colorScheme.primary.toArgb()
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()

    val adapter = remember {
        object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int,
            ): RecyclerView.ViewHolder {
                val composeView = ComposeView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setContent {
                        pages[viewType]()
                    }
                }
                return object : RecyclerView.ViewHolder(composeView) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}

            override fun getItemCount(): Int = pages.size

            override fun getItemViewType(position: Int): Int = position
        }
    }

    LaunchedEffect(viewPager, adapter) {
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    Column {
        AndroidView(
            factory = {
                tabLayout.apply {
                    setSelectedTabIndicatorColor(primary)
                    setTabTextColors(onSurfaceVariant, primary)
                    setBackgroundColor(Color.Transparent.toArgb())
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        HorizontalDivider()
        AndroidView(
            factory = {
                viewPager.apply {
                    isScrollContainer = false
                    ViewCompat.setNestedScrollingEnabled(this, true)
                }
            },
            modifier = Modifier.heightIn(0.dp, 10000.dp)
        )
    }
}

fun debugLog(message: String) {
    Log.d("TabDebug", message)
}

@Composable
fun getScreenHeight(): Dp {
    val configuration = LocalConfiguration.current
    return configuration.screenHeightDp.dp
}