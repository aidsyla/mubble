package com.aidsyla.mubble.feature.post

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuGroup
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenuPopup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.aidsyla.mubble.R
import com.aidsyla.mubble.common.components.CircleImage
import com.aidsyla.mubble.common.navigation.LocalNavAnimatedVisibilityScope
import com.aidsyla.mubble.common.navigation.LocalSharedTransitionScope
import com.aidsyla.mubble.common.navigation.sharedelements.NewPostSharedElementKey
import com.aidsyla.mubble.ui.theme.MubbleDesignSystem

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NewPostScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    val sharedTransitionScope =
        LocalSharedTransitionScope.current
            ?: throw IllegalStateException("No SharedElementScope found")
    val animatedContentScope =
        LocalNavAnimatedVisibilityScope.current
            ?: throw IllegalStateException("No AnimatedVisibility found")

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val roundedCornerAnimation by animatedContentScope.transition.animateDp {
        when (it) {
            EnterExitState.PreEnter -> 0.dp
            EnterExitState.Visible -> 12.dp
            EnterExitState.PostExit -> 12.dp
        }
    }

    var description by remember { mutableStateOf("") }
    val isAtLimit = description.length == 300

    val descriptionLimitColor by animateColorAsState(
        targetValue = if (isAtLimit) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            MaterialTheme.colorScheme.primaryContainer
        },
        animationSpec = tween(150)
    )

    var expanded by remember { mutableStateOf(false) }

    with(sharedTransitionScope) {
        Scaffold(
            modifier = modifier
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(
                        key = NewPostSharedElementKey
                    ),
                    animatedVisibilityScope = animatedContentScope,
                    resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(contentScale = ContentScale.Crop),
                    clipInOverlayDuringTransition = OverlayClip(
                        RoundedCornerShape(
                            roundedCornerAnimation
                        )
                    )
                ),
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("New Post") },
                    navigationIcon = {
                        IconButton(onClick = {
                            focusManager.clearFocus()
                            onBackClick()
                        }) {
                            Icon(
                                painter = MubbleDesignSystem.Icons.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    actions = {
                        ContinueButton(
                            enabled = !isAtLimit,
                            onClick = {}
                        )
                    }
                )
            }
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)
                    .padding(top = 8.dp, start = 16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircleImage(
                            painter = painterResource(R.drawable.profile_12),
                            size = 40.dp
                        )
                        PostDestinationSelect()
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    color = descriptionLimitColor
                                )
                        ) {
                            Text(
                                text = "${description.length}/300",
                                modifier = Modifier.padding(4.dp),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures { focusManager.clearFocus() }
                            }
                    ) {
                        BasicTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.5f)
                                .focusRequester(focusRequester),
                            value = description,
                            onValueChange = { dsc -> if (dsc.length <= 300) description = dsc },
                            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            decorationBox = { innerTextField ->
                                Box {
                                    if (description.isEmpty()) {
                                        Text(
                                            "Start typing...",
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )
                    }
                }
                Surface(
                    modifier = Modifier.padding(top = 12.dp),
                    shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh
                ) {
                    Column {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                painter = MubbleDesignSystem.Icons.Camera,
                                contentDescription = null
                            )
                        }
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                painter = MubbleDesignSystem.Icons.Gallery,
                                contentDescription = null
                            )
                        }
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(painter = MubbleDesignSystem.Icons.Gif, contentDescription = null)
                        }
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}

@Composable
private fun ContinueButton(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val disabledContainerColor = MaterialTheme.colorScheme.onSurface
        .copy(alpha = 0.1f)
        .compositeOver(MaterialTheme.colorScheme.surface)
    val containerColor by animateColorAsState(
        targetValue = if (enabled) MaterialTheme.colorScheme.surfaceContainerLow else disabledContainerColor,
        animationSpec = tween(150),
        label = "containerColor"
    )

    val disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        .copy(alpha = 0.38f)
        .compositeOver(MaterialTheme.colorScheme.surface)
    val textColor by animateColorAsState(
        targetValue = if (enabled) MaterialTheme.colorScheme.primary else disabledTextColor,
        animationSpec = tween(150),
        label = "textColor"
    )

    TextButton(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = containerColor,
            disabledContainerColor = containerColor,
            contentColor = textColor,
            disabledContentColor = textColor
        )
    ) {
        Text("Continue")
    }
}

@Preview
@Composable
fun SendScreen(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(4) {
            SendPostToPeople(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview
@Composable
fun SendPostToPeople(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            colorFilter = ColorFilter.tint(Color.Blue),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
        )
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Filan Fisteku", style = MaterialTheme.typography.bodyMedium)
            Button(
                onClick = {},
                modifier = Modifier.height(32.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Text("Send", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Preview
@Composable
fun PostDestinationSelect() {
    val groupInteractionSource = remember { MutableInteractionSource() }

    val headerLabel = "Where to post?"
    val groupItemLabels = listOf("My Profile", "Cars", "Nature")
    val icons = listOf(
        R.drawable.profile_12,
        R.drawable.circle_5,
        R.drawable.circle_1
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }

    Box {
        TooltipBox(
            positionProvider =
                TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
            tooltip = { PlainTooltip { Text("Localized description") } },
            state = rememberTooltipState(),
        ) {
            Button(
                onClick = { expanded = !expanded },
                modifier = Modifier.height(32.dp),
                contentPadding = PaddingValues(8.dp)
//                colors = ButtonDefaults.filledTonalButtonColors()
            ) {
                if (selectedIndex == 0) {
                    Icon(
                        painter = painterResource(MubbleDesignSystem.TopLevelDestinationIcons.ProfileSelected),
                        modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                        contentDescription = null
                    )
                } else {
                    CircleImage(
                        painter = painterResource(icons[selectedIndex]),
                        modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = groupItemLabels[selectedIndex],
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.animateContentSize()
                )
            }
        }
        DropdownMenuPopup(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuGroup(
                shapes = MenuDefaults.groupShape(1, 1),
                interactionSource = groupInteractionSource,
            ) {
                MenuDefaults.Label { Text(headerLabel) }
                HorizontalDivider(
                    modifier = Modifier.padding(MenuDefaults.HorizontalDividerPadding)
                )
                val count = groupItemLabels.size
                groupItemLabels.fastForEachIndexed { i, string ->
                    DropdownMenuItem(
                        text = { Text(string) },
                        shapes = MenuDefaults.itemShape(i, count),
                        leadingIcon = {
                            if (i == 0)
                                Icon(
                                    painter = painterResource(MubbleDesignSystem.TopLevelDestinationIcons.Profile),
                                    modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                                    contentDescription = null
                                )
                            else
                                CircleImage(
                                    painter = painterResource(icons[i]),
                                    modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                                )
                        },
                        checkedLeadingIcon = {
                            if (i == 0)
                                Icon(
                                    painter = painterResource(MubbleDesignSystem.TopLevelDestinationIcons.ProfileSelected),
                                    modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                                    contentDescription = null
                                )
                            else
                                CircleImage(
                                    painter = painterResource(icons[i]),
                                    modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                                )
                        },
                        checked = selectedIndex == i,
                        onCheckedChange = {
                            if (it) selectedIndex = i
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}
