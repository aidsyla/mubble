package com.aidsyla.mubble.feature.videos.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButtonShapes
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.ui.theme.outlineVariantLight

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun VideoControlIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    OutlinedIconButton(
        onClick = onClick,
        modifier =
        modifier
            .minimumInteractiveComponentSize()
            .size(
                IconButtonDefaults.extraSmallContainerSize(
                    IconButtonDefaults.IconButtonWidthOption.Uniform
                )
            ),
        shapes =
        IconButtonDefaults.shapes(
            shape = IconButtonDefaults.extraSmallRoundShape,
            pressedShape = IconButtonDefaults.extraSmallPressedShape
        ),
        colors =
        IconButtonDefaults.outlinedIconButtonColors(
            containerColor = Color.Black.copy(alpha = 0.25f),
            contentColor = outlineVariantLight
        ),
        border = BorderStroke(1.dp, Color.Transparent)
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun VideoControlIconToggleButton(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    OutlinedIconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier =
        modifier
            .minimumInteractiveComponentSize()
            .size(
                IconButtonDefaults.extraSmallContainerSize(
                    IconButtonDefaults.IconButtonWidthOption.Uniform
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
            containerColor = Color.Black.copy(alpha = 0.25f),
            contentColor = outlineVariantLight,
            checkedContainerColor = Color.Black.copy(alpha = 0.4f),
            checkedContentColor = Color.White
        ),
        border = BorderStroke(1.dp, Color.Transparent)
    ) {
        content()
    }
}
