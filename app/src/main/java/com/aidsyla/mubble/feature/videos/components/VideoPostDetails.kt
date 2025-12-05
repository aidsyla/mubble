package com.aidsyla.mubble.feature.videos.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.R
import com.aidsyla.mubble.common.components.CircleImage
import com.aidsyla.mubble.ui.theme.MubbleTheme

@Composable
internal fun VideoPostDetails(
    modifier: Modifier = Modifier,
    isCaptionExpanded: Boolean,
    onCaptionExpandChange: (Boolean) -> Unit,
) {
    Row(
        modifier =
            modifier
                .animateContentSize()
                .padding(start = 16.dp, end = 0.dp, top = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = if (isCaptionExpanded) Alignment.Top else Alignment.CenterVertically,
    ) {
        CircleImage(
            modifier =
                Modifier.graphicsLayer {
                    shadowElevation = 6f
                    shape = CircleShape
                },
            size = 36.dp,
            painter = painterResource(R.drawable.profile_12),
            borderWidth = 0.2.dp,
            borderColor = Color.Black.copy(alpha = 0.25f),
        )
        CompositionLocalProvider(LocalRippleConfiguration provides null) {
            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .clickable {
                            onCaptionExpandChange(!isCaptionExpanded)
                        },
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "john_smith",
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                    )
                    Text(
                        text = "4h",
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                    )
                }
                Text(
                    text = "Just wrapped up an amazing weekend explore",
                    overflow = TextOverflow.Ellipsis,
                    maxLines = if (isCaptionExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White,
                )
            }
        }
        IconButton(
            modifier = Modifier.requiredHeight(36.dp),
            onClick = {},
        ) {
            Icon(
                painter = MubbleTheme.Icons.MoreHorizontal,
                contentDescription = null,
                tint = Color.White,
            )
        }
    }
}
