package com.aidsyla.mubble.common.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.R
import com.aidsyla.mubble.common.components.layout.rememberIsAtTop
import com.aidsyla.mubble.data.UserRepo
import com.aidsyla.mubble.feature.profile.follow.SearchResults
import com.aidsyla.mubble.ui.theme.MubbleTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharePostBottomSheet(
    openSheet: Boolean,
    onOpenChange: (Boolean) -> Unit,
) {
    val textFieldState = rememberTextFieldState()
    val searchBarState = rememberSearchBarState()
    val coroutineScope = rememberCoroutineScope()

    val inputField = @Composable {
        SearchBarDefaults.InputField(
            modifier = Modifier,
            searchBarState = searchBarState,
            textFieldState = textFieldState,
            onSearch = { coroutineScope.launch { searchBarState.animateToCollapsed() } },
            placeholder = { Text("Search") },
            leadingIcon = {
                AnimatedContent(
                    targetState = searchBarState.currentValue,
                    contentAlignment = Alignment.Center,
                    transitionSpec = {
                        fadeIn() + expandIn(
                            expandFrom = Alignment.Center, clip = false
                        ) togetherWith fadeOut() + shrinkOut(
                            shrinkTowards = Alignment.Center, clip = false
                        )
                    }) { targetSearchBarValue ->
                    when (targetSearchBarValue) {
                        SearchBarValue.Expanded -> {
                            IconButton(
                                onClick = { coroutineScope.launch { searchBarState.animateToCollapsed() } }) {
                                Icon(
                                    painter = MubbleTheme.Icons.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }

                        SearchBarValue.Collapsed -> {
                            Icon(
                                painter = MubbleTheme.Icons.Search, contentDescription = "Search"
                            )
                        }
                    }
                }
            },
        )
    }


    val bottomSheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val users = UserRepo.dummyUsers

    val listState = rememberLazyListState()
    val isAtTop = listState.rememberIsAtTop()

    ExpandedFullScreenSearchBar(
        state = searchBarState,
        inputField = inputField,
        windowInsets = { WindowInsets.statusBars }) {
        SearchResults(
            onResultClick = { result ->
                textFieldState.setTextAndPlaceCursorAtEnd(result)
                coroutineScope.launch { searchBarState.animateToCollapsed() }
            })
    }

    if (openSheet) {
        ModalBottomSheet(
            dragHandle = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                        ) {
                            Surface(
                                modifier =
                                    Modifier
                                        .align(Alignment.Center),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                shape = MaterialTheme.shapes.extraLarge,
                            ) {
                                Box(Modifier.size(width = 32.dp, height = 4.dp))
                            }
                            Icon(
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .size(20.dp)
                                    .align(Alignment.CenterEnd),
                                painter = MubbleTheme.Icons.Close,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        SearchBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 8.dp),
                            state = searchBarState,
                            inputField = inputField,
                        )
                    }
                    AnimatedVisibility(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                        exit = fadeOut(animationSpec = tween(durationMillis = 300)),
                        visible = !isAtTop
                    ) {
                        SubtleHorizontalDivider()
                    }
                }
            },
            contentWindowInsets = { WindowInsets.safeDrawing.only(WindowInsetsSides.Top) },
            sheetGesturesEnabled = false,
            onDismissRequest = { onOpenChange(false) },
            sheetState = bottomSheetState,
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight(0.75f),
                state = listState,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 4.dp,
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(users) {
                    SharePostRow(
                        profilePictureResId = it.profilePictureResId,
                        displayName = it.displayName,
                        username = it.username
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SharePostRow(
    modifier: Modifier = Modifier,
    @DrawableRes profilePictureResId: Int,
    displayName: String,
    username: String,
) {
    val size = ButtonDefaults.ExtraSmallContainerHeight
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircleImage(
            painter = painterResource(profilePictureResId),
            borderWidth = 0.1.dp,
            size = 48.dp
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = displayName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
            Text(
                text = "@$username",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {},
            modifier = Modifier.heightIn(max = 32.dp),
            contentPadding = ButtonDefaults.contentPaddingFor(size),
        ) {
            Icon(
                painter = MubbleTheme.Icons.SendNew, contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.iconSizeFor(size)),
            )
            Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(size)))
            Text("Send", style = MaterialTheme.typography.labelSmall)
        }
    }
}
