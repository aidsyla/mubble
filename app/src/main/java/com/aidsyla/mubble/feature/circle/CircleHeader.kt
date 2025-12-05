package com.aidsyla.mubble.feature.circle

import androidx.annotation.DrawableRes
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.aidsyla.mubble.common.navigation.LocalNavAnimatedVisibilityScope
import com.aidsyla.mubble.common.navigation.LocalSharedTransitionScope
import com.aidsyla.mubble.feature.profile.components.FullScreenMediaSharedElementKey
import com.aidsyla.mubble.feature.profile.components.FullScreenMediaType

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun CircleHeader(
    modifier: Modifier = Modifier,
    title: String,
    memberCount: Int,
    @DrawableRes bannerResId: Int,
    onMediaClick: (Int, FullScreenMediaType) -> Unit,
) {
    val sharedTransitionScope =
        LocalSharedTransitionScope.current
            ?: throw IllegalStateException("No SharedElementScope found")
    val animatedContentScope =
        LocalNavAnimatedVisibilityScope.current
            ?: throw IllegalStateException("No AnimatedVisibility found")

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Box {
            with(sharedTransitionScope) {
                with(animatedContentScope) {
                    Box(
                        modifier =
                            Modifier
                                .align(Alignment.TopCenter)
                                .zIndex(1f)
                                .fillMaxWidth()
                                .windowInsetsTopHeight(
                                    WindowInsets.statusBars.add(WindowInsets(top = TopAppBarDefaults.TopAppBarExpandedHeight)),
                                ).renderInSharedTransitionScopeOverlay(
                                    zIndexInOverlay = 1f,
                                ).animateEnterExit()
                                .background(
                                    Brush.verticalGradient(
                                        colors =
                                            listOf(
                                                Color.Black.copy(alpha = 0.3f),
                                                Color.Transparent,
                                            ),
                                    ),
                                ),
                    )
                }
            }
            with(sharedTransitionScope) {
                Image(
                    painter = painterResource(bannerResId),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(2.25f)
                            .clickable {
                                onMediaClick(bannerResId, FullScreenMediaType.BANNER)
                            }.sharedElement(
                                rememberSharedContentState(
                                    key =
                                        FullScreenMediaSharedElementKey(
                                            imgId = bannerResId,
                                            fullScreenMediaType = FullScreenMediaType.BANNER,
                                        ),
                                ),
                                animatedVisibilityScope = animatedContentScope,
                            ),
                    contentScale = ContentScale.FillWidth,
                )
            }
        }
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp, bottom = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = "$memberCount members",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                FilledTonalButton(
                    onClick = {},
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                ) {
                    Text(text = "Join")
                }
            }
            Text(
                text = "Obviously the Material You design doesn't improve branding, but you already know the point is for cohesion.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
