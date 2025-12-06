package com.aidsyla.mubble.feature.circle

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButtonShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.common.navigation.LocalNavAnimatedVisibilityScope
import com.aidsyla.mubble.common.navigation.LocalSharedTransitionScope
import com.aidsyla.mubble.common.navigation.sharedelements.PostOrigin
import com.aidsyla.mubble.common.navigation.sharedelements.PostSharedElementKey
import com.aidsyla.mubble.common.navigation.sharedelements.PostSharedElementType
import com.aidsyla.mubble.feature.circle.model.Circle
import com.aidsyla.mubble.feature.circle.model.CircleRepo
import com.aidsyla.mubble.ui.theme.MubbleDesignSystem
import com.aidsyla.mubble.ui.theme.MubbleTheme
import com.aidsyla.mubble.ui.theme.onSurfaceDark

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AllButton(modifier: Modifier = Modifier) {
    FilledIconButton(
        onClick = {},
        modifier =
        modifier
            .minimumInteractiveComponentSize()
            .width(
                48.dp
            ),
        shapes = IconButtonDefaults.shapes()
    ) {
        Text("All", style = MaterialTheme.typography.labelLarge)
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun CircleItem(
    modifier: Modifier = Modifier,
    origin: PostOrigin,
    circle: Circle,
    showIcon: Boolean,
    onCircleClick: (circleId: String) -> Unit
) {
    var checked by remember { mutableStateOf(false) }

    val sharedTransitionScope =
        LocalSharedTransitionScope.current
            ?: throw IllegalStateException("No SharedElementScope found")
    val animatedContentScope =
        LocalNavAnimatedVisibilityScope.current
            ?: throw IllegalStateException("No AnimatedVisibility found")

    val roundedCornerAnimation by animatedContentScope.transition.animateDp {
        when (it) {
            EnterExitState.PreEnter -> 0.dp
            EnterExitState.Visible -> 12.dp
            EnterExitState.PostExit -> 12.dp
        }
    }

    with(sharedTransitionScope) {
        Box(
            modifier =
            modifier
                .clip(MaterialTheme.shapes.medium)
                .clickable { onCircleClick(circle.id) }
                .aspectRatio(1.45f)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.75f),
                    shape = MaterialTheme.shapes.medium
                ).sharedBounds(
                    rememberSharedContentState(
                        key =
                        PostSharedElementKey(
                            postId = circle.id,
                            origin = origin,
                            type = PostSharedElementType.Bounds
                        )
                    ),
                    animatedVisibilityScope = animatedContentScope,
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                    clipInOverlayDuringTransition =
                    OverlayClip(
                        RoundedCornerShape(
                            roundedCornerAnimation
                        )
                    )
                )
        ) {
            Image(
                painter = painterResource(circle.bannerResId),
                contentDescription = null,
                modifier =
                Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier =
                Modifier
                    .matchParentSize()
                    .background(MubbleDesignSystem.Gradients.fadingBlackGradientReversed)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                Modifier
                    .padding(start = 12.dp, end = 4.dp, bottom = if (showIcon) 4.dp else 8.dp)
                    .align(Alignment.BottomStart)
            ) {
                Column {
                    Text(
                        text = circle.name,
                        style =
                        MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                    Text(
                        text = circle.memberCount.toString() + " members",
                        style = MaterialTheme.typography.bodySmall,
                        color = onSurfaceDark
                    )
                }
                if (showIcon) {
                    Spacer(modifier = Modifier.weight(1f))
                    OutlinedIconToggleButton(
                        checked = checked,
                        onCheckedChange = { checked = it },
                        modifier =
                        Modifier
                            .minimumInteractiveComponentSize()
                            .size(
                                IconButtonDefaults.extraSmallContainerSize(
                                    IconButtonDefaults.IconButtonWidthOption.Wide
                                )
                            ),
                        shapes =
                        IconToggleButtonShapes(
                            shape = IconButtonDefaults.extraSmallRoundShape,
                            pressedShape = IconButtonDefaults.extraSmallPressedShape,
                            checkedShape = IconButtonDefaults.extraSmallSquareShape
                        ),
                        colors =
                        IconButtonDefaults.outlinedIconToggleButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White,
                            checkedContainerColor = Color.Transparent,
                            checkedContentColor = Color.White
                        ),
                        border = BorderStroke(1.dp, Color.White)
                    ) {
                        AnimatedContent(
                            targetState = checked
                        ) {
                            Icon(
                                modifier =
                                Modifier.size(
                                    IconButtonDefaults.extraSmallIconSize
                                ),
                                painter = if (it) MubbleDesignSystem.Icons.Check else MubbleDesignSystem.Icons.Add,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

val circle = CircleRepo.dummyCircles.component2()

@Preview
@Composable
private fun AllButtonPreview() {
    MubbleTheme {
        AllButton()
    }
}

@Preview
@Composable
private fun WidePreview() {
    MubbleTheme {
        CircleItem(origin = PostOrigin.None, circle = circle, showIcon = true) {}
    }
}

@Preview
@Composable
private fun SmallPreview() {
    MubbleTheme {
        CircleItem(origin = PostOrigin.None, circle = circle, showIcon = false) {}
    }
}
