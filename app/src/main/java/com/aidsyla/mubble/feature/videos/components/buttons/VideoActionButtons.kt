package com.aidsyla.mubble.feature.videos.components.buttons

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.ui.theme.MubbleDesignSystem
import com.aidsyla.mubble.util.clickableWithScaleIndication
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun VideoActionButtons(
    modifier: Modifier = Modifier,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onSendClick: () -> Unit,
    onSaveClick: () -> Unit,
    isPostLiked: Boolean,
    onLikeChange: (Boolean) -> Unit
) {
    Column(
        modifier =
        modifier
            .padding(end = 0.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BouncingHeartIcon(
            isPostLiked = isPostLiked,
            onLikeChange = onLikeChange,
            count = 5321,
            onClick = onLikeClick
        )
        ActionButton(
            painter = MubbleDesignSystem.Icons.Comment,
            count = "588",
            onClick = onCommentClick
        )
        ActionButton(
            painter = MubbleDesignSystem.Icons.SendNew,
            count = "42",
            onClick = onSendClick
        )
        ActionButton(
            painter = MubbleDesignSystem.Icons.Save,
            count = "54",
            onClick = onSaveClick
        )
    }
}

@Composable
private fun ActionButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    count: String,
    onClick: () -> Unit
) {
    Column(
        modifier =
        modifier
            .requiredSize(48.dp)
            .clickableWithScaleIndication {
                onClick()
            }.background(color = Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
        ) {
            Icon(
                painter = painter,
                contentDescription = null,
                modifier =
                Modifier
                    .size(28.dp)
                    .offset(x = 0.dp, y = 0.dp)
                    .alpha(0.3f)
                    .blur(2.5.dp),
                tint = Color.Black
            )
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painter,
                contentDescription = null,
                tint = Color.White
            )
        }
        Text(
            text = count,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style =
            MaterialTheme.typography.labelSmall.copy(
                shadow =
                Shadow(
                    color = Color.Black.copy(alpha = 0.4f),
                    blurRadius = 6f
                )
            ),
            color = Color.White
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BouncingHeartIcon(
    isPostLiked: Boolean,
    onLikeChange: (Boolean) -> Unit,
    count: Int,
    onClick: () -> Unit
) {
    val sizeAnim = remember { Animatable(0f) }
    val offsetAnim = remember { Animatable(Offset.Zero, Offset.VectorConverter) }

    val alpha by animateFloatAsState(
        targetValue = if (sizeAnim.value != 0f) 0f else 1f
    )

    LaunchedEffect(isPostLiked) {
        if (isPostLiked) {
            sizeAnim.snapTo(0f)
            coroutineScope {
                launch {
                    sizeAnim.animateTo(
                        targetValue = 2.1f,
                        animationSpec = tween(durationMillis = 250, easing = EaseOutQuad)
                    )
                }
                launch {
                    offsetAnim.animateTo(
                        targetValue = Offset(x = 0f, y = -100f),
                        animationSpec = tween(250)
                    )
                }
            }

            coroutineScope {
                launch {
                    sizeAnim.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 200, easing = EaseInOutCubic)
                    )
                }
                launch {
                    offsetAnim.animateTo(
                        targetValue = Offset.Zero,
                        animationSpec = tween(250)
                    )
                }
            }
        } else {
            coroutineScope {
                launch {
                    sizeAnim.animateTo(0f, animationSpec = tween(250))
                }
                launch {
                    offsetAnim.animateTo(
                        targetValue = Offset.Zero,
                        animationSpec = tween(250)
                    )
                }
            }
        }
    }

    Column(
        modifier =
        Modifier
            .requiredSize(48.dp)
            .clickableWithScaleIndication {
                onClick()
                onLikeChange(!isPostLiked)
            }.background(color = Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Box(
                modifier =
                Modifier.alpha(
                    alpha
                )
            ) {
                Icon(
                    painter = MubbleDesignSystem.Icons.Favorite,
                    contentDescription = null,
                    modifier =
                    Modifier
                        .size(26.dp)
                        .scale(1.1f)
                        .alpha(0.3f)
                        .blur(2.5.dp),
                    tint = Color.Black
                )
                Icon(
                    modifier = Modifier.size(26.dp),
                    painter = MubbleDesignSystem.Icons.Favorite,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            if (sizeAnim.value > 0f) {
                Icon(
                    modifier =
                    Modifier
                        .size(26.dp)
                        .graphicsLayer {
                            translationX = offsetAnim.value.x
                            translationY = offsetAnim.value.y
                            scaleX = sizeAnim.value
                            scaleY = sizeAnim.value
                        },
                    painter = MubbleDesignSystem.Icons.Heart,
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        }
        Text(
            text = count.toString(),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style =
            MaterialTheme.typography.labelSmall.copy(
                shadow =
                Shadow(
                    color = Color.Black.copy(alpha = 0.4f),
                    blurRadius = 6f
                )
            ),
            color = Color.White
        )
    }
}
