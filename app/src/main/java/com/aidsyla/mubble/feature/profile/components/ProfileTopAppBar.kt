package com.aidsyla.mubble.feature.profile.components

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.core.view.WindowCompat
import com.aidsyla.mubble.common.components.layout.STICKY_HEADER
import com.aidsyla.mubble.common.components.layout.STICKY_HEADER_INDEX
import com.aidsyla.mubble.ui.theme.MubbleTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopAppBar(
    modifier: Modifier = Modifier,
    transitionLength: Dp = 200.dp,
    lazyListState: LazyListState,
    offsetAmount: Dp,
    onNavigateToSettings: () -> Unit,
    onBackClick: () -> Unit,
) {
    val surface = MaterialTheme.colorScheme.surface
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val shadowColor = Color.Black

    val targetAlpha by rememberTargetAlpha(
        lazyListState = lazyListState,
        transitionLength = transitionLength,
        offsetAmount = offsetAmount
    )
    val topAppBarBackgroundColor = surface.copy(alpha = targetAlpha)

    val iconColor by remember(targetAlpha) {
        derivedStateOf {
            lerp(Color.White, onSurface, targetAlpha)
        }
    }
    val iconVariantColor by remember(targetAlpha) {
        derivedStateOf {
            lerp(Color.White, onSurfaceVariant, targetAlpha)
        }
    }
    val shadowAlpha by remember(targetAlpha) {
        derivedStateOf {
            lerp(0.5f, 0f, targetAlpha)
        }
    }

    val view = LocalView.current
    val window = (view.context as? Activity)?.window ?: return

    val isLightMode = !isSystemInDarkTheme()
    val statusBarIconColorChangeThreshold = 0.3f
    val useDarkStatusBarIcons = remember(targetAlpha, isLightMode) {
        if (targetAlpha >= statusBarIconColorChangeThreshold) isLightMode else false
    }

    val insetsController = remember(window, view) {
        WindowCompat.getInsetsController(window, view)
    }

    DisposableEffect(insetsController, useDarkStatusBarIcons) {
        insetsController.isAppearanceLightStatusBars = useDarkStatusBarIcons

        onDispose {
            insetsController.isAppearanceLightStatusBars = isLightMode
        }
    }

    Column(
        modifier = modifier
            .background(color = topAppBarBackgroundColor)
            .windowInsetsPadding(TopAppBarDefaults.windowInsets)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(TopAppBarDefaults.TopAppBarExpandedHeight)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = iconColor
                )
            ) {
                Icon(
                    imageVector = MubbleTheme.Icons.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .offset(x = 1.dp, y = 1.dp)
                        .alpha(shadowAlpha)
                        .blur(2.dp, BlurredEdgeTreatment(CircleShape)),
                    tint = shadowColor,
                )
                Icon(
                    imageVector = MubbleTheme.Icons.ArrowBack,
                    contentDescription = "Back",
                )
            }

            if (targetAlpha > 0f) {
                Text(
                    text = "Alex Smith",
                    modifier = Modifier
                        .weight(1f)
                        .alpha(targetAlpha),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    color = onSurface
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
            IconButton(
                onClick = onNavigateToSettings,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = iconVariantColor
                )
            ) {
                Icon(
                    imageVector = MubbleTheme.Icons.Settings,
                    contentDescription = null,
                    modifier = Modifier
                        .offset(x = 1.dp, y = 1.dp)
                        .alpha(shadowAlpha)
                        .blur(2.dp, BlurredEdgeTreatment(CircleShape)),
                    tint = shadowColor,
                )
                Icon(
                    imageVector = MubbleTheme.Icons.Settings,
                    contentDescription = "Settings",
                )
            }
        }
    }
}

@Composable
private fun rememberTargetAlpha(
    lazyListState: LazyListState,
    transitionLength: Dp,
    offsetAmount: Dp,
): State<Float> {
    val density = LocalDensity.current
    val alphaTransitionLengthPx = remember(transitionLength, density) {
        with(density) { (transitionLength / 2f).toPx() }
    }
    return remember(offsetAmount, alphaTransitionLengthPx, density) {
        derivedStateOf {
            val stickyKeyItemDockingOffsetPx = with(density) { offsetAmount.toPx() }

            val layoutInfo = lazyListState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            val header = visibleItems.find { it.key == STICKY_HEADER }

            if (header != null) {
                val scrollOffsetPxRelativeToDock =
                    header.offset.toFloat() - stickyKeyItemDockingOffsetPx
                when {
                    scrollOffsetPxRelativeToDock <= 0f || alphaTransitionLengthPx <= 0f -> 1f
                    scrollOffsetPxRelativeToDock >= alphaTransitionLengthPx -> 0f
                    else -> {
                        val progressTowardsOpaque =
                            alphaTransitionLengthPx - scrollOffsetPxRelativeToDock
                        val fraction = progressTowardsOpaque / alphaTransitionLengthPx
                        fraction.coerceIn(0f, 1f)
                    }
                }
            } else {
                if (layoutInfo.totalItemsCount > STICKY_HEADER_INDEX) {
                    if (lazyListState.firstVisibleItemIndex > STICKY_HEADER_INDEX) 1f else 0f
                } else {
                    0f
                }
            }
        }
    }
}
