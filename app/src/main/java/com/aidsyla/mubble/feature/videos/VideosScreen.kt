package com.aidsyla.mubble.feature.videos

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import androidx.media3.common.Player.REPEAT_MODE_ONE
import androidx.media3.common.util.UnstableApi
import com.aidsyla.mubble.app.LocalDarkTheme
import com.aidsyla.mubble.common.components.SharePostBottomSheet
import com.aidsyla.mubble.feature.videos.components.AnimatingHeart
import com.aidsyla.mubble.feature.videos.components.CommentsBottomSheet
import com.aidsyla.mubble.feature.videos.components.Like
import com.aidsyla.mubble.feature.videos.components.LoadingPulse
import com.aidsyla.mubble.feature.videos.components.VideoControls
import com.aidsyla.mubble.feature.videos.components.VideoPostDetails
import com.aidsyla.mubble.feature.videos.components.VideoProgressBar
import com.aidsyla.mubble.feature.videos.components.VideoSurface
import com.aidsyla.mubble.feature.videos.components.buttons.ExpandButton
import com.aidsyla.mubble.feature.videos.components.buttons.VideoActionButtons
import com.aidsyla.mubble.feature.videos.utils.formatMsToMinutesSeconds
import com.aidsyla.mubble.feature.videos.utils.initializePlayerForVideo
import com.aidsyla.mubble.ui.theme.MubbleTheme
import com.aidsyla.mubble.ui.theme.surfaceDark
import java.util.UUID

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideosScreen(
    modifier: Modifier = Modifier,
    viewModel: VideoViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var uiVisible by remember { mutableStateOf(true) }
    val topAppBarAlpha by animateFloatAsState(
        targetValue = if (uiVisible) 1f else 0f
    )

    val view = LocalView.current
    val window = (view.context as? Activity)?.window ?: return

    val isLightMode = !LocalDarkTheme.current

    val insetsController = remember(window, view) {
        WindowCompat.getInsetsController(window, view)
    }

    DisposableEffect(insetsController) {
        insetsController.isAppearanceLightStatusBars = false

        onDispose {
            insetsController.isAppearanceLightStatusBars = isLightMode
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .zIndex(3f)
                    .alpha(topAppBarAlpha),
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
        containerColor = surfaceDark,
        contentWindowInsets = WindowInsets(0.dp)
    ) {
        AnimatedContent(
            transitionSpec = {
                fadeIn(tween(durationMillis = 500)).togetherWith(fadeOut(tween(durationMillis = 500)))
                    .apply {
                        val goingBack = targetState.isTransitioningTo(VideoScreenUiState.Loading)
                        targetContentZIndex = if (goingBack) 2f else -2f
                    }
            },
            targetState = uiState
        ) { videoUiState ->
            when (videoUiState) {
                VideoScreenUiState.Loading -> {
                    LoadingPulse()
                }

                is VideoScreenUiState.Success -> {
                    val videos = videoUiState.videoUrls
                    val pagerState = rememberPagerState { videos.size }
                    VerticalPager(
                        modifier = Modifier.fillMaxSize(),
                        state = pagerState,
                        beyondViewportPageCount = 2,
                        key = { index -> videos[index] }
                    ) { pageIndex ->
                        VideoContent(
                            videoUrl = videos[pageIndex],
                            pagerState = pagerState,
                            pageIndex = pageIndex,
                            uiVisibilityChanged = { uiVisible = it },
                            onBackClick = onBackClick
                        )
                    }
                }
            }
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun VideoContent(
    modifier: Modifier = Modifier,
    videoUrl: String,
    pageIndex: Int,
    pagerState: PagerState,
    uiVisibilityChanged: (Boolean) -> Unit,
    onBackClick: () -> Unit,
) {
    var openCommentSheet by rememberSaveable { mutableStateOf(false) }
    var openSharePostSheet by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    var player by remember { mutableStateOf<Player?>(null) }

    val isBeingDragged by pagerState.interactionSource.collectIsDraggedAsState()
    val isCurrentPage = pagerState.currentPage == pageIndex
    var isUiVisible by remember { mutableStateOf(true) }
    var isZooming by remember { mutableStateOf(false) }

    val uiVisibilityState = remember { MutableTransitionState(initialState = true) }
    val shouldUiBeVisible = isUiVisible && !isZooming

    LaunchedEffect(shouldUiBeVisible) {
        uiVisibilityState.targetState = shouldUiBeVisible
        uiVisibilityChanged(shouldUiBeVisible)
    }

    val gradientAlpha by animateFloatAsState(
        targetValue = if (shouldUiBeVisible) 1f else 0f
    )
    val draggedPagerAlpha by animateFloatAsState(
        targetValue = if (isBeingDragged) 0.25f else 1f
    )

    var isScrubbing by remember { mutableStateOf(false) }
    var currentScrubPosition by remember { mutableLongStateOf(0L) }

    var isCaptionExpanded by remember { mutableStateOf(false) }
    val captionScrimAlpha by animateFloatAsState(
        targetValue = if (isCaptionExpanded && shouldUiBeVisible) 1f else 0f
    )

    LifecycleStartEffect(Unit) {
        player = initializePlayerForVideo(context, videoUrl)
        onStopOrDispose {
            player?.apply { release() }
            player = null
        }
    }

    LaunchedEffect(isCurrentPage) {
        player?.apply {
            playWhenReady = isCurrentPage
            repeatMode = REPEAT_MODE_ONE
        }
    }

    LaunchedEffect(pagerState.settledPage) {
        player?.apply {
            if (!isPlaying)
                seekToDefaultPosition()
        }
    }

    var isLiked by remember { mutableStateOf(false) }
    var showLike by remember { mutableStateOf(false) }
    val sizeAnim = remember { Animatable(0f) }
    val likes = remember { mutableStateListOf<Like>() }

    LaunchedEffect(showLike) {
        if (showLike) {
            sizeAnim.snapTo(0f)
            sizeAnim.animateTo(
                targetValue = 5f,
                animationSpec = tween(durationMillis = 300, easing = EaseOutQuad)
            )
            sizeAnim.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 300, easing = EaseInOutCubic)
            )
            showLike = false
        } else {
            sizeAnim.animateTo(0f, animationSpec = tween(200))
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                this.detectTapGestures(
                    onDoubleTap = {
                        isLiked = true
                        likes.add(Like(id = UUID.randomUUID(), offset = it))
                    }
                )
            }
    ) {
        Box(
            modifier = Modifier
                .zIndex(2f)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(gradientAlpha)
                    .background(
                        MubbleTheme.Gradients.fadingBlackGradient
                    )
            )
            Box(
                modifier = Modifier
                    .rotate(180f)
                    .fillMaxSize()
                    .alpha(gradientAlpha)
                    .background(
                        MubbleTheme.Gradients.fadingBlackGradient
                    )
            )
            Box(
                modifier = Modifier
                    .rotate(180f)
                    .fillMaxSize()
                    .alpha(captionScrimAlpha)
                    .background(
                        MubbleTheme.Gradients.captionScrimGradient
                    )
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(bottom = 8.dp)
                .zIndex(4f)
                .graphicsLayer {
                    this.alpha = draggedPagerAlpha
                },
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .alpha(gradientAlpha)
                    .then(
                        if (!shouldUiBeVisible) Modifier
                            .pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent(PointerEventPass.Initial)
                                        event.changes.forEach { it.consume() }
                                    }
                                }
                            } else Modifier),
                horizontalAlignment = Alignment.End
            ) {
                VideoActionButtons(
                    isPostLiked = isLiked,
                    onLikeChange = { isLiked = it },
                    onLikeClick = { isLiked = !isLiked },
                    onCommentClick = { openCommentSheet = !openCommentSheet },
                    onSendClick = { openSharePostSheet = !openSharePostSheet },
                    onSaveClick = {}
                )
                VideoPostDetails(
                    isCaptionExpanded = isCaptionExpanded,
                    onCaptionExpandChange = { isCaptionExpanded = it }
                )
                VideoControls(
                    onBackClick = onBackClick,
                    player = player
                )
            }
            AnimatedVisibility(
                modifier = Modifier
                    .matchParentSize()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 0.dp),
                visible = !isZooming,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.BottomEnd
                ) {
                    ExpandButton(
                        checked = isUiVisible,
                        onCheckedChange = { isUiVisible = it },
                    ) {
                        AnimatedContent(
                            targetState = isUiVisible
                        ) {
                            Icon(
                                modifier = Modifier.size(
                                    IconButtonDefaults.extraSmallIconSize
                                ),
                                painter = if (it) MubbleTheme.Icons.ExpandContent else MubbleTheme.Icons.CollapseContent,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }
            }

        }

        VideoSurface(
            player = player,
            isZooming = { isZooming = it }
        )

        player?.let {
            Box(
                modifier = Modifier
                    .zIndex(3f)
                    .navigationBarsPadding()
                    .padding(bottom = 4.dp)
                    .alpha(gradientAlpha)
                    .align(Alignment.BottomCenter)
            ) {
                VideoProgressBar(
                    player = it,
                    onScrubbingInfoChange = { p1, p2 ->
                        isScrubbing = p1
                        currentScrubPosition = p2
                    },
                )
            }
        }

        player?.let {
            AnimatedVisibility(
                visible = isScrubbing,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(5f)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val formattedCurrentTime =
                        formatMsToMinutesSeconds(currentScrubPosition)
                    val formattedTotalDuration = formatMsToMinutesSeconds(it.duration)
                    Text(
                        "$formattedCurrentTime / $formattedTotalDuration",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Medium,
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.4f),
                                blurRadius = 12f
                            )
                        ),
                        color = Color.White
                    )
                }
            }
        }

        likes.forEach { like ->
            key(like.id) {
                AnimatingHeart(
                    offset = like.offset,
                    onAnimationFinished = {
                        likes.remove(like)
                    }
                )
            }
        }

        CommentsBottomSheet(
            openBottomSheet = openCommentSheet,
            onOpenChange = { openCommentSheet = it }
        )

        SharePostBottomSheet(
            openSheet = openSharePostSheet,
            onOpenChange = { openSharePostSheet = it }
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
