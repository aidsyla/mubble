package com.aidsyla.mubble.feature.videos.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.ui.theme.MubbleDesignSystem
import java.util.UUID
import kotlin.random.Random

internal data class Like(
    val id: UUID,
    val offset: Offset
)

@Composable
fun AnimatingHeart(
    offset: Offset,
    onAnimationFinished: () -> Unit
) {
    val sizeAnim = remember { Animatable(0f) }

    val randomRotation =
        remember {
            Random.nextFloat() * 30f - 15f
        }

    LaunchedEffect(Unit) {
        sizeAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 300, easing = EaseOutQuad)
        )
        sizeAnim.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 300, easing = EaseInOutCubic)
        )
        onAnimationFinished()
    }

    Icon(
        modifier =
        Modifier
            .size(125.dp)
            .graphicsLayer {
                translationX = offset.x - (size.width / 2)
                translationY = offset.y - (size.height / 2)
                scaleX = sizeAnim.value
                scaleY = sizeAnim.value
                alpha = sizeAnim.value
                rotationZ = randomRotation
            }.blur(
                radius = 8.dp,
                edgeTreatment = BlurredEdgeTreatment.Unbounded
            ),
        painter = MubbleDesignSystem.Icons.FavoriteFilled,
        contentDescription = null,
        tint = Color.Black.copy(alpha = 0.3f)
    )

    Icon(
        modifier =
        Modifier
            .size(120.dp)
            .graphicsLayer {
                translationX = offset.x - (size.width / 2)
                translationY = offset.y - (size.height / 2)
                scaleX = sizeAnim.value
                scaleY = sizeAnim.value
                alpha = sizeAnim.value
                rotationZ = randomRotation
            },
        painter = MubbleDesignSystem.Icons.Heart,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
