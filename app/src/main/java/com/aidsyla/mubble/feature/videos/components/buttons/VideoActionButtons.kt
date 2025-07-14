package com.aidsyla.mubble.feature.videos.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.ui.theme.MubbleTheme

@Composable
internal fun VideoActionButtons(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(end = 0.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ActionButton(
            painter = MubbleTheme.Icons.Favorite,
            count = "5 312"
        )
        ActionButton(
            painter = MubbleTheme.Icons.Comment,
            count = "588"
        )
        ActionButton(
            painter = MubbleTheme.Icons.Send,
            count = "42"
        )
        ActionButton(
            painter = MubbleTheme.Icons.Save,
            count = "54"
        )
    }
}

@Composable
private fun ActionButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    count: String,
) {
    Column(
        modifier = modifier
            .requiredSize(48.dp)
            .background(color = Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier,
        ) {
            Icon(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
                    .offset(x = 0.dp, y = 0.dp)
                    .alpha(0.3f)
                    .blur(2.5.dp),
                tint = Color.Black,
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
            style = MaterialTheme.typography.labelSmall.copy(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.4f),
                    blurRadius = 6f
                )
            ),
            color = Color.White,
        )
    }
}