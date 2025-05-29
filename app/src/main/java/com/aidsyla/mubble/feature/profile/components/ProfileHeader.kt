package com.aidsyla.mubble.feature.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.aidsyla.mubble.R
import com.aidsyla.mubble.common.components.for_reference.debugLog
import com.aidsyla.mubble.ui.theme.MubbleTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileHeader(
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    var profilePictureWidth by remember { mutableStateOf(0.dp) }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.BottomStart
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(1f)
                    .fillMaxWidth()
                    .windowInsetsTopHeight(WindowInsets.statusBars.add(WindowInsets(top = TopAppBarDefaults.TopAppBarExpandedHeight)))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.3f), Color.Transparent
                            ),
                        )
                    )
            )
            Image(
                painter = painterResource(R.drawable.post_1),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2.25f),
                contentScale = ContentScale.FillWidth
            )
            Box(
                modifier = Modifier
                    .offset(y = 53.dp)
                    .padding(start = 16.dp)
                    .clip(shape = CircleShape)
                    .background(color = MaterialTheme.colorScheme.surface)
                    .padding(3.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.profile_2),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .onGloballyPositioned {
                            profilePictureWidth = with(density) { it.size.width.toDp() }
                        }
                    ,
                    contentScale = ContentScale.Crop
                )
            }
        }
        FollowerCount(
            profilePictureWidth = profilePictureWidth
        )
        ProfileDetails()
        Spacer(modifier = Modifier.height(2.dp))
    }
}

@Composable
fun FollowerCount(
    modifier: Modifier = Modifier,
    profilePictureWidth: Dp
) {
    val startPadding = profilePictureWidth + 32.dp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = startPadding, end = 16.dp, top = 8.dp, bottom = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = modifier
                .clip(shape = MaterialTheme.shapes.medium)
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
                .heightIn(max = 36.dp)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "4523 Following",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall
            )
            VerticalDivider()
            Text(
                "53644 Followers",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProfileDetails(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "Alex Smith",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "@alex_smith",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {},
                modifier = Modifier.size(
                    IconButtonDefaults.smallContainerSize(
                        IconButtonDefaults.IconButtonWidthOption.Wide
                    )
                ),
                colors = IconButtonDefaults.filledIconButtonColors(),
                shapes = IconButtonDefaults.shapes()
            ) {
                Icon(painter = MubbleTheme.Icons.PersonAdd, contentDescription = null)
            }
            Spacer(modifier = Modifier.width(12.dp))
            IconButton(
                onClick = {},
                modifier = Modifier.size(
                    IconButtonDefaults.smallContainerSize(
                        IconButtonDefaults.IconButtonWidthOption.Wide
                    )
                ),
                colors = IconButtonDefaults.filledIconButtonColors(),
                shapes = IconButtonDefaults.shapes()
            ) {
                Icon(painter = MubbleTheme.Icons.Message, contentDescription = null)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Obviously the Material You design doesn't improve branding, but you already know the point is for cohesion.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileHeaderPreview() {
    MubbleTheme {
        ProfileHeader()
    }
}