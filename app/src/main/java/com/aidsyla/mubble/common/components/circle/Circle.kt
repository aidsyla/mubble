package com.aidsyla.mubble.common.components.circle

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
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
import com.aidsyla.mubble.feature.circle.model.Circle
import com.aidsyla.mubble.feature.circle.model.CircleRepo
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CircleItem(
    modifier: Modifier = Modifier,
    circle: Circle,
    showIcon: Boolean,
) {
    var checked by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .aspectRatio(1.45f)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant, shape = MaterialTheme.shapes.medium)
    ) {
        Image(
            painter = painterResource(circle.bannerResId),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(MubbleTheme.Gradients.fadingBlackGradient2)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 12.dp, end = 4.dp, bottom = if (showIcon) 4.dp else 8.dp)
                .align(Alignment.BottomStart),
        ) {
            Column {
                Text(
                    text = circle.name, style = MaterialTheme.typography.titleMedium.copy(
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
                    shapes = IconToggleButtonShapes(
                        shape = IconButtonDefaults.extraSmallRoundShape,
                        pressedShape = IconButtonDefaults.extraSmallPressedShape,
                        checkedShape = IconButtonDefaults.extraSmallSquareShape
                    ),
                    colors = IconButtonDefaults.outlinedIconToggleButtonColors(
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
                            modifier = Modifier.size(
                                IconButtonDefaults.extraSmallIconSize
                            ),
                            imageVector = if (it) Icons.Default.Check else Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White
                        )
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
        CircleItem(circle = circle, showIcon = true)
    }
}

@Preview
@Composable
private fun SmallPreview() {
    MubbleTheme {
        CircleItem(circle = circle, showIcon = false)
    }
}
