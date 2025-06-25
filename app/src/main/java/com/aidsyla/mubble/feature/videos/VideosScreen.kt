package com.aidsyla.mubble.feature.videos

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButtonShapes
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.aidsyla.mubble.R
import com.aidsyla.mubble.common.components.CircleImage
import com.aidsyla.mubble.ui.theme.MubbleTheme
import com.aidsyla.mubble.ui.theme.onSurfaceDark

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideosScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier.zIndex(3f),
                title = {
                    Icon(
                        painter = MubbleTheme.Icons.MubbleIcon, contentDescription = null,
                        tint = Color.White
                    )
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = MubbleTheme.Icons.Search, contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(2f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(
                            MubbleTheme.Gradients.fadingBlackGradient
                        )
                )
                Box(
                    modifier = Modifier
                        .rotate(180f)
                        .fillMaxWidth()
                        .weight(1f)
                        .background(
                            MubbleTheme.Gradients.fadingBlackGradient
                        )
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .zIndex(3f),
                horizontalAlignment = Alignment.End
            ) {
                VideoActionButtons()
                VideoProfileHeader()
                VideoControls(
                    onBackClick = onBackClick
                )
            }
            Image(
                modifier = Modifier
                    .fillMaxHeight()
                    .zIndex(1f),
                painter = painterResource(R.drawable.post_11),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
//                colorFilter = ColorFilter.tint(color = Color.White)
            )
        }
    }
}

@Composable
fun VideoActionButtons(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ActionButton(
            painter = MubbleTheme.Icons.Favorite,
            count = 12
        )
        ActionButton(
            painter = MubbleTheme.Icons.Comment,
            count = 6
        )
        ActionButton(
            painter = MubbleTheme.Icons.Send,
            count = 2
        )
        ActionButton(
            painter = MubbleTheme.Icons.Save,
            count = 7
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    count: Int,
) {
    Column(
        modifier = modifier.heightIn(min = 48.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Icon(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .offset(x = 1.dp, y = 1.dp)
                    .alpha(0.5f)
                    .blur(2.dp),
                tint = Color.Black,
            )
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painter,
                contentDescription = null,
                tint = Color.White
            )
        }
        Text(
            count.toString(),
            style = MaterialTheme.typography.labelMediumEmphasized.copy(
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            ),
            color = Color.White,
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun VideoProfileHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(start = 16.dp, end = 4.dp, top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleImage(
            painter = painterResource(R.drawable.profile_12),
            borderWidth = 0.dp
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    "johnsmith", style = MaterialTheme.typography.titleSmallEmphasized,
                    color = Color.White
                )
                Text(
                    "4h", style = MaterialTheme.typography.titleSmallEmphasized,
                    color = onSurfaceDark
                )
            }
            Text(
                "Just wrapped up an amazing weekend explore",
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium,
                color = onSurfaceDark
            )
        }
        IconButton(
            onClick = {}
        ) {
            Icon(
                painter = MubbleTheme.Icons.MoreHorizontal, contentDescription = null,
                tint = onSurfaceDark
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun VideoControls(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    var checked by remember { mutableStateOf(false) }
    var checked2 by remember { mutableStateOf(false) }
    var checked3 by remember { mutableStateOf(false) }
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedIconButton(
                onClick = onBackClick,
                modifier =
                    Modifier
                        .minimumInteractiveComponentSize()
                        .size(
                            IconButtonDefaults.extraSmallContainerSize(
                                IconButtonDefaults.IconButtonWidthOption.Wide
                            )
                        ),
                shapes = IconButtonDefaults.shapes(
                    shape = IconButtonDefaults.extraSmallRoundShape,
                    pressedShape = IconButtonDefaults.extraSmallPressedShape,
                ),
                colors = IconButtonDefaults.outlinedIconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                ),
                border = BorderStroke(1.dp, Color.White)
            ) {
                Icon(
                    modifier = Modifier.size(
                        IconButtonDefaults.extraSmallIconSize
                    ),
                    painter = MubbleTheme.Icons.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                OutlinedIconToggleButton(
                    checked = checked,
                    onCheckedChange = { checked = it },
                    modifier =
                        Modifier
                            .minimumInteractiveComponentSize()
                            .size(
                                IconButtonDefaults.extraSmallContainerSize(
                                    IconButtonDefaults.IconButtonWidthOption.Uniform
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
                            painter = if (it) MubbleTheme.Icons.Resume else MubbleTheme.Icons.Pause,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }

                OutlinedIconToggleButton(
                    checked = checked2,
                    onCheckedChange = { checked2 = it },
                    modifier =
                        Modifier
                            .minimumInteractiveComponentSize()
                            .size(
                                IconButtonDefaults.extraSmallContainerSize(
                                    IconButtonDefaults.IconButtonWidthOption.Uniform
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
                        targetState = checked2
                    ) {
                        Icon(
                            modifier = Modifier.size(
                                IconButtonDefaults.extraSmallIconSize
                            ),
                            painter = if (it) MubbleTheme.Icons.VolumeOff else MubbleTheme.Icons.VolumeUp,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }

                OutlinedIconButton(
                    onClick = {},
                    modifier =
                        Modifier
                            .minimumInteractiveComponentSize()
                            .size(
                                IconButtonDefaults.extraSmallContainerSize(
                                    IconButtonDefaults.IconButtonWidthOption.Uniform
                                )
                            ),
                    shapes = IconButtonDefaults.shapes(
                        shape = IconButtonDefaults.extraSmallRoundShape,
                        pressedShape = IconButtonDefaults.extraSmallPressedShape,
                    ),
                    colors = IconButtonDefaults.outlinedIconButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White,
                    ),
                    border = BorderStroke(1.dp, Color.White)
                ) {
                    AnimatedContent(
                        targetState = checked2
                    ) {
                        Icon(
                            modifier = Modifier.size(
                                IconButtonDefaults.extraSmallIconSize
                            ),
                            painter = if (it) MubbleTheme.Icons.Speed1x else MubbleTheme.Icons.Speed2x,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }

            OutlinedIconToggleButton(
                checked = checked3,
                onCheckedChange = { checked3 = it },
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
                    targetState = checked3
                ) {
                    Icon(
                        modifier = Modifier.size(
                            IconButtonDefaults.extraSmallIconSize
                        ),
                        painter = if (it) MubbleTheme.Icons.CollapseContent else MubbleTheme.Icons.ExpandContent,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

        }
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            progress = {
                0.3f
            },
        )
    }

}

@Preview
@Composable
private fun VideosScreenPreview() {
    MubbleTheme {
        VideosScreen(
            onBackClick = {}
        )
    }
}
