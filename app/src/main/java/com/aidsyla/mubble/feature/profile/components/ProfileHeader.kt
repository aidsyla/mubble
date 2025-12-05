package com.aidsyla.mubble.feature.profile.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.aidsyla.mubble.common.navigation.LocalNavAnimatedVisibilityScope
import com.aidsyla.mubble.common.navigation.LocalSharedTransitionScope
import com.aidsyla.mubble.data.User
import com.aidsyla.mubble.data.UserRepo
import com.aidsyla.mubble.ui.theme.MubbleTheme

@Composable
fun ProfileHeader(
    modifier: Modifier = Modifier,
    isCurrentUser: Boolean,
    user: User,
    hasAvatarOrBannerBeenClicked: Boolean,
    onHasBeenClickedChange: (Boolean) -> Unit,
    onMediaClick: (Int, FullScreenMediaType) -> Unit,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit,
) {
    ProfileHeader(
        modifier = modifier,
        isCurrentUser = isCurrentUser,
        profilePictureResId = user.profilePictureResId,
        bannerResId = user.bannerResId,
        displayName = user.displayName,
        username = user.username,
        description = user.description,
        followingCount = user.followingCount,
        followerCount = user.followerCount,
        hasAvatarOrBannerBeenClicked = hasAvatarOrBannerBeenClicked,
        onHasBeenClickedChange = onHasBeenClickedChange,
        onMediaClick = onMediaClick,
        onFollowersClick = onFollowersClick,
        onFollowingClick = onFollowingClick
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun ProfileHeader(
    modifier: Modifier = Modifier,
    isCurrentUser: Boolean,
    @DrawableRes profilePictureResId: Int,
    @DrawableRes bannerResId: Int,
    displayName: String,
    username: String,
    description: String?,
    followingCount: Int,
    followerCount: Int,
    hasAvatarOrBannerBeenClicked: Boolean,
    onHasBeenClickedChange: (Boolean) -> Unit,
    onMediaClick: (Int, FullScreenMediaType) -> Unit,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit,
) {
    val profilePictureSize = getScreenWidth().div(4)
    val offsetAmount = profilePictureSize * 0.5f

    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No SharedElementScope found")
    val animatedContentScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No AnimatedVisibility found")

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box {
            with(sharedTransitionScope) {
                with(animatedContentScope) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .zIndex(1f)
                            .fillMaxWidth()
                            .windowInsetsTopHeight(WindowInsets.statusBars.add(WindowInsets(top = TopAppBarDefaults.TopAppBarExpandedHeight)))
                            .renderInSharedTransitionScopeOverlay(
                                zIndexInOverlay = 1f
                            )
                            .animateEnterExit()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.3f), Color.Transparent
                                    ),
                                )
                            )

                    )
                }
            }
            with(sharedTransitionScope) {
                Image(
                    painter = painterResource(bannerResId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2.25f)
                        .clickable {
                            onHasBeenClickedChange(true)
                            onMediaClick(bannerResId, FullScreenMediaType.BANNER)
                        }
                        .sharedElement(
                            rememberSharedContentState(
                                key = FullScreenMediaSharedElementKey(
                                    imgId = bannerResId,
                                    fullScreenMediaType = FullScreenMediaType.BANNER
                                )
                            ), animatedVisibilityScope = animatedContentScope
                        ),
                    contentScale = ContentScale.FillWidth)
            }

            Row(
                modifier = Modifier
                    .height(profilePictureSize)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp)
                    .offset(y = offsetAmount),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                with(sharedTransitionScope) {
                    with(animatedContentScope) {
                        Image(
                            painter = painterResource(profilePictureResId),
                            contentDescription = null,
                            modifier = Modifier
                                .size(profilePictureSize)
                                .clip(CircleShape)
                                .clickable {
                                    onHasBeenClickedChange(true)
                                    onMediaClick(
                                        profilePictureResId, FullScreenMediaType.AVATAR
                                    )
                                }
                                .then(
                                    if (hasAvatarOrBannerBeenClicked) Modifier
                                        .renderInSharedTransitionScopeOverlay(
                                            zIndexInOverlay = 1f
                                        )
                                        .animateEnterExit()
                                    else Modifier
                                )
                                .sharedElement(
                                    rememberSharedContentState(
                                        key = FullScreenMediaSharedElementKey(
                                            imgId = profilePictureResId,
                                            fullScreenMediaType = FullScreenMediaType.AVATAR
                                        )
                                    ), animatedVisibilityScope = animatedContentScope
                                )
                                .clip(CircleShape)
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = CircleShape
                                )
                                .padding(2.dp),
                            contentScale = ContentScale.Crop)
                    }
                }

                FollowerCount(
                    modifier = Modifier.fillMaxHeight(0.5f),
                    followingCount = followingCount,
                    followerCount = followerCount,
                    onFollowersClick = onFollowersClick,
                    onFollowingClick = onFollowingClick
                )
            }
        }
        Spacer(modifier = Modifier.height(offsetAmount))
        ProfileDetails(
            displayName = displayName,
            username = username,
            description = description,
            isCurrentUser = isCurrentUser
        )
        Spacer(modifier = Modifier.height(2.dp))
    }
}

@Composable
fun FollowerCount(
    modifier: Modifier = Modifier,
    followerCount: Int,
    followingCount: Int,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit,
) {
    Box(
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .clip(shape = MaterialTheme.shapes.medium)
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
                .heightIn(min = 48.dp, max = 60.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onFollowersClick() }
                    .weight(1f),
                contentAlignment = Alignment.Center) {
                Text(
                    "$followerCount Followers",
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            VerticalDivider(
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onFollowingClick() }
                    .weight(1f),
                contentAlignment = Alignment.Center) {
                Text(
                    "$followingCount Following",
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProfileDetails(
    modifier: Modifier = Modifier,
    displayName: String,
    username: String,
    description: String?,
    isCurrentUser: Boolean,
) {
    var isDescriptionExpanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .animateContentSize()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "@$username",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
//            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(4.dp))
        description?.let {
            CompositionLocalProvider(LocalRippleConfiguration provides null) {
                Text(
                    modifier = Modifier.clickable {
                        isDescriptionExpanded = !isDescriptionExpanded
                    },
                    text = it,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = if (isDescriptionExpanded) Int.MAX_VALUE else 2,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        if (!isCurrentUser) {
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .weight(1f),
                    shapes = ButtonDefaults.shapes(),
                ) {
                    Icon(
                        painter = MubbleTheme.Icons.PersonAdd,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                    Text(text = "Add")
                }
                Spacer(modifier = Modifier.width(12.dp))
                FilledTonalButton(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    shapes = ButtonDefaults.shapes(),
                ) {
                    Icon(painter = MubbleTheme.Icons.Message, contentDescription = null)
                    Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                    Text(text = "Message")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExtraSmallButton(modifier: Modifier = Modifier) {
    Button(
        onClick = {},
        modifier = Modifier
            .height(ButtonDefaults.ExtraSmallContainerHeight),
        shapes = ButtonDefaults.shapes(),
        contentPadding = ButtonDefaults.ExtraSmallContentPadding
    ) {
        Icon(
            painter = MubbleTheme.Icons.PersonAdd,
            contentDescription = null,
            modifier = Modifier.size(
                ButtonDefaults.ExtraSmallIconSize
            )
        )
        Spacer(modifier = Modifier.width(ButtonDefaults.ExtraSmallIconSpacing))
        Text(text = "Add", style = ButtonDefaults.textStyleFor(ButtonDefaults.ExtraSmallContainerHeight))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileHeaderPreview() {
    MubbleTheme {
        ProfileHeader(
            user = UserRepo.dummyUsers.component1(),
            isCurrentUser = true,
            hasAvatarOrBannerBeenClicked = false,
            onHasBeenClickedChange = {},
            onMediaClick = { _, _ -> },
            onFollowersClick = { },
            onFollowingClick = { })
    }
}