package com.aidsyla.mubble.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircleImage(
    modifier: Modifier = Modifier,
    painter: Painter,
    size: Dp = 48.dp,
    borderWidth: Dp = 1.dp,
    borderColor: Color = MaterialTheme.colorScheme.outlineVariant,
    contentDescription: String? = null,
) {
    val imageModifier = modifier
        .size(size)
        .clip(CircleShape)
        .then(
            if (borderWidth > 0.dp) {
                Modifier.border(borderWidth, borderColor, CircleShape)
            } else {
                Modifier
            }
        )
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = imageModifier,
        contentScale = ContentScale.Crop
    )
}