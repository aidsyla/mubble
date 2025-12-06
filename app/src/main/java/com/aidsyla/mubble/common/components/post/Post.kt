package com.aidsyla.mubble.common.components.post

import android.view.WindowManager
import androidx.annotation.DrawableRes
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.lerp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.aidsyla.mubble.R
import com.aidsyla.mubble.common.components.CircleImage
import com.aidsyla.mubble.common.components.SharePostBottomSheet
import com.aidsyla.mubble.common.navigation.LocalNavAnimatedVisibilityScope
import com.aidsyla.mubble.common.navigation.LocalSharedTransitionScope
import com.aidsyla.mubble.common.navigation.sharedelements.PostOrigin
import com.aidsyla.mubble.common.navigation.sharedelements.PostSharedElementKey
import com.aidsyla.mubble.common.navigation.sharedelements.PostSharedElementType
import com.aidsyla.mubble.data.BubbleFeedItem
import com.aidsyla.mubble.data.DummyPostRepository
import com.aidsyla.mubble.data.FeedItem
import com.aidsyla.mubble.data.ImagePostFeedItem
import com.aidsyla.mubble.ui.theme.MubbleDesignSystem
import com.aidsyla.mubble.ui.theme.MubbleTheme
import com.aidsyla.mubble.util.IndicationType
import com.aidsyla.mubble.util.ScaleIndicationNodeFactory
import com.aidsyla.mubble.util.clickableWithScaleIndication
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BasePostLayout(
    modifier: Modifier = Modifier,
    item: FeedItem,
    origin: PostOrigin = PostOrigin.None,
    useCard: Boolean = true,
    isInPostDetails: Boolean = false,
    onUserClick: (String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    onPostClick: (postId: String) -> Unit
) {
    val sharedTransitionScope =
        LocalSharedTransitionScope.current
            ?: throw IllegalStateException("No SharedElementScope found")
    val animatedContentScope =
        LocalNavAnimatedVisibilityScope.current
            ?: throw IllegalStateException("No AnimatedVisibility found")

    var openSheet by rememberSaveable { mutableStateOf(false) }

    var isLiked by remember { mutableStateOf(false) }
    val content: @Composable () -> Unit = {
        if (useCard) {
            with(sharedTransitionScope) {
                PostHeader(
                    sharedElementModifier =
                    Modifier.sharedElement(
                        rememberSharedContentState(
                            key =
                            PostSharedElementKey(
                                postId = item.id,
                                origin = origin,
                                type = PostSharedElementType.ProfileAvatar
                            )
                        ),
                        animatedVisibilityScope = animatedContentScope
                    ),
                    name = item.displayName,
                    avatarResId = item.userAvatarResId,
                    circleName = item.circleName,
                    datePosted = item.datePosted,
                    onUserClick = { onUserClick(item.id) },
                    onMoreClick = { onMoreClick(item.id) }
                )
            }
        }

        when (item) {
            is ImagePostFeedItem -> {
                item.postDescription?.let {
                    PostDescription(
                        modifier =
                        Modifier
                            .padding(bottom = 8.dp),
                        description = it,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                with(sharedTransitionScope) {
                    ZoomablePostMedia(
                        modifier =
                        Modifier
                            .sharedElement(
                                rememberSharedContentState(
                                    key =
                                    PostSharedElementKey(
                                        postId = item.id,
                                        origin = origin,
                                        type = PostSharedElementType.Image
                                    )
                                ),
                                animatedVisibilityScope = animatedContentScope
                            ),
                        imageRes = item.postImageResId,
                        isInPostDetails = isInPostDetails
                    )
                }
                PostActions(
                    likeCount = item.likeCount,
                    commentCount = item.commentCount,
                    shareCount = item.shareCount,
                    isLiked = isLiked,
                    onFilledChange = { isLiked = it },
                    onShareClick = {
                        openSheet = true
                    }
                )
            }

            is BubbleFeedItem -> {
                with(sharedTransitionScope) {
                    PostDescription(
                        modifier =
                        Modifier.sharedBounds(
                            rememberSharedContentState(
                                key =
                                PostSharedElementKey(
                                    postId = item.id,
                                    origin = origin,
                                    type = PostSharedElementType.Bubble
                                )
                            ),
                            animatedVisibilityScope = animatedContentScope
                        ),
                        description = item.postDescription,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                PostActions(
                    likeCount = item.likeCount,
                    commentCount = item.commentCount,
                    shareCount = item.shareCount,
                    isLiked = isLiked,
                    onFilledChange = { isLiked = it },
                    onShareClick = {}
                )
            }
        }
    }

    val mutableInteractionSource = remember { MutableInteractionSource() }
    if (useCard) {
        Card(
            modifier =
            modifier
                .combinedClickable(
                    interactionSource = mutableInteractionSource,
                    indication = ScaleIndicationNodeFactory(IndicationType.CARDS),
                    onClick = { onPostClick(item.id) },
                    onDoubleClick = { isLiked = !isLiked }
                ).border(
                    width = 0.1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = MaterialTheme.shapes.large
                ),
            shape = MaterialTheme.shapes.large,
            colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            content()
        }
    } else {
        Column(modifier = modifier) {
            content()
        }
    }

    SharePostBottomSheet(
        openSheet = openSheet,
        onOpenChange = { openSheet = it }
    )
}

@Composable
fun PostHeader(
    modifier: Modifier = Modifier,
    sharedElementModifier: Modifier = Modifier,
    name: String,
    @DrawableRes avatarResId: Int,
    datePosted: String,
    circleName: String?,
    onUserClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    Row(
        modifier =
        modifier
            .padding(start = 16.dp, end = 4.dp)
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier =
            Modifier
                .clip(shape = MaterialTheme.shapes.extraLarge)
                .clickable { onUserClick() }
                .padding(end = 8.dp)
        ) {
            CircleImage(
                modifier = sharedElementModifier,
                painter = painterResource(avatarResId),
                contentDescription = "$name's avatar",
                borderWidth = 0.2.dp
            )
            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        modifier =
                        Modifier
                            .alignByBaseline(),
                        text = name,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        modifier = Modifier.alignByBaseline(),
                        text = datePosted,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                circleName?.let {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = MubbleDesignSystem.Icons.InCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        CircleImage(
                            painter = painterResource(R.drawable.post_3),
                            size = 24.dp,
                            borderWidth = 0.1.dp
                        )
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
        Spacer(Modifier.weight(1f))
        IconButton(onClick = onMoreClick) {
            Icon(painter = MubbleDesignSystem.Icons.MoreHorizontal, contentDescription = "More options")
        }
    }
}

@Composable
fun PostDescription(
    modifier: Modifier = Modifier,
    style: TextStyle,
    description: String
) {
    Box(modifier = modifier) {
        Text(
            modifier =
            Modifier
                .padding(horizontal = 16.dp),
            text = description,
            style = style,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

suspend fun PointerInputScope.detectTransformGesturesCustom(
    panZoomLock: Boolean = false,
    onGestureEnd: () -> Unit = {},
    onGesture: (centroid: Offset, pan: Offset, zoom: Float) -> Unit
) {
    awaitEachGesture {
        var totalZoom = 1f
        var totalPan = Offset.Zero
        var pastTouchSlop = false
        val touchSlop = viewConfiguration.touchSlop
        var lockedToPanZoom = false
        awaitFirstDown(requireUnconsumed = false)
        do {
            val event = awaitPointerEvent()
            val canceled = event.changes.fastAny { it.isConsumed }
            val pointerCount = event.changes.count { it.pressed }

            if (!canceled) {
                val rawZoom = event.calculateZoom()
                val rawPan = event.calculatePan()
                val centroid = event.calculateCentroid(useCurrent = false)

                if (!pastTouchSlop && pointerCount >= 2) {
                    totalZoom *= rawZoom
                    totalPan += rawPan

                    val centroidSize = event.calculateCentroidSize(useCurrent = false)
                    val zoomMotion = abs(1 - totalZoom) * centroidSize
                    val panMotion = totalPan.getDistance()

                    if (zoomMotion > touchSlop || panMotion > touchSlop) {
                        pastTouchSlop = true
                        lockedToPanZoom = panZoomLock
                    }
                }

                if (pastTouchSlop) {
                    when {
                        pointerCount >= 2 -> {
                            val effectivePan = if (lockedToPanZoom) Offset.Zero else rawPan
                            if (rawZoom != 1f || effectivePan != Offset.Zero) {
                                onGesture(centroid, effectivePan, rawZoom)
                            }
                            event.changes.fastForEach {
                                if (it.positionChanged()) it.consume()
                            }
                        }

                        pointerCount == 1 -> {
                            val singlePan = event.calculatePan()
                            if (singlePan != Offset.Zero) {
                                onGesture(centroid, singlePan, 1f)
                                event.changes.fastForEach {
                                    if (it.positionChanged()) it.consume()
                                }
                            }
                        }

                        pointerCount == 0 -> {
                            break
                        }
                    }
                }
            }
        } while (!canceled && event.changes.fastAny { it.pressed })
        onGestureEnd()
    }
}

@Composable
fun ZoomablePostMedia(
    modifier: Modifier = Modifier,
    @DrawableRes imageRes: Int,
    isInPostDetails: Boolean
) {
    val coroutineScope = rememberCoroutineScope()
    val imagePadding = if (isInPostDetails) 0.dp else 16.dp
    var hasLaunched by remember { mutableStateOf(false) }
    var isAnimatingBack by remember { mutableStateOf(false) }

    var layoutCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

    var initialSize by remember { mutableStateOf(IntSize.Zero) }
    var initialOffset by remember { mutableStateOf(Offset.Zero) }

    var zoom by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var centerOffset by remember { mutableStateOf(Offset.Zero) }

    val zoomAnimatable = remember { Animatable(1f) }
    val offsetAnimatable = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val centerOffsetAnimatable = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val backgroundAlphaAnim = remember { Animatable(0f) }

    centerOffset =
        if (initialSize == IntSize.Zero) {
            Offset.Zero
        } else {
            Offset(
                x = (initialSize.width / 2f) * (zoom - 1) - offset.x * zoom,
                y = (initialSize.height / 2f) * (zoom - 1) - offset.y * zoom
            )
        }

    LaunchedEffect(zoom) {
        if (!zoomAnimatable.isRunning) {
            zoomAnimatable.snapTo(zoom)
        }
    }
    LaunchedEffect(offset) {
        if (!offsetAnimatable.isRunning) {
            offsetAnimatable.snapTo(offset)
        }
    }
    LaunchedEffect(centerOffset) {
        if (!centerOffsetAnimatable.isRunning) {
            centerOffsetAnimatable.snapTo(centerOffset)
        }
    }

    fun resetToCenter() {
        coroutineScope.launch {
            isAnimatingBack = true
            val zoomJob =
                launch {
                    zoomAnimatable.animateTo(
                        targetValue = 1f,
                        animationSpec =
                        tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) {
                        zoom = value
                    }
                }

            val offsetJob =
                launch {
                    centerOffsetAnimatable.animateTo(
                        targetValue = Offset.Zero,
                        animationSpec =
                        tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) {
                        centerOffset = value
                    }
                }

            val bgJob =
                launch {
                    backgroundAlphaAnim.animateTo(
                        targetValue = 0f,
                        animationSpec =
                        tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            joinAll(zoomJob, offsetJob, bgJob)
            offset = Offset.Zero
            isAnimatingBack = false
            hasLaunched = false
        }
    }

    val density = LocalDensity.current
    var imageSize by remember { mutableFloatStateOf(0f) }
    val imageHeight = with(density) { imageSize.toDp() }

    val topPx = initialOffset.y
    val centerOfImagePx = topPx + imageSize / 2

    val screenHeightPx =
        with(density) {
            LocalConfiguration.current.screenHeightDp.dp
                .toPx()
        }
    val screenCenterPx = screenHeightPx / 2

    val contentAlignment =
        if (centerOfImagePx > screenCenterPx) Alignment.TopCenter else Alignment.BottomCenter

    if (hasLaunched) {
        Dialog(
            onDismissRequest = {},
            properties =
            DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            val view = LocalView.current
            val dialogWindowProvider = view.parent as DialogWindowProvider
            dialogWindowProvider.window.setDimAmount(0f)
            val window = dialogWindowProvider.window
            LaunchedEffect(view) {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                )
            }
            Box(
                modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = Color.Black.copy(alpha = backgroundAlphaAnim.value))
                    .graphicsLayer {
                        translationX = initialOffset.x
                        translationY = initialOffset.y
                    }.padding(end = imagePadding)
            ) {
                Box(modifier = Modifier.wrapContentSize()) {
                    Box(
                        modifier =
                        Modifier
                            .matchParentSize(),
                        contentAlignment = contentAlignment
                    ) {
                        Box(
                            modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(imageHeight)
                                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                        )
                    }
                    Image(
                        painter = painterResource(imageRes),
                        contentDescription = null,
                        modifier =
                        Modifier
                            .graphicsLayer(
                                scaleX = zoomAnimatable.value,
                                scaleY = zoomAnimatable.value,
                                translationX = centerOffsetAnimatable.value.x,
                                translationY = centerOffsetAnimatable.value.y
                            ).fillMaxWidth(),
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier.wrapContentSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            modifier =
            modifier
                .onGloballyPositioned { coordinates ->
                    layoutCoordinates = coordinates
                    initialSize = coordinates.size
                }.then(
                    if (isAnimatingBack) {
                        Modifier
                    } else {
                        Modifier.pointerInput(Unit) {
                            detectTransformGesturesCustom(
                                onGestureEnd = {
                                    resetToCenter()
                                }
                            ) { centroid, pan, gestureZoom ->
                                layoutCoordinates?.let { c ->
                                    initialOffset = c.positionInWindow()

                                    val boxTop = c.boundsInWindow().top
                                    val boxBottom = c.boundsInWindow().bottom

                                    val visibleTop = boxTop.coerceIn(0f, screenHeightPx)
                                    val visibleBottom = boxBottom.coerceIn(0f, screenHeightPx)

                                    val visibleHeight = visibleBottom - visibleTop

                                    imageSize = visibleHeight
                                }
                                hasLaunched = true
                                val oldZoom = zoom
                                val newZoom = max(zoom * gestureZoom, 0.7f)

                                offset = (offset + centroid / oldZoom) -
                                    (centroid / newZoom + pan / oldZoom)
                                zoom = newZoom

                                val maxScale = 1.6f
                                val t = ((newZoom - 1f) / (maxScale - 1f)).coerceIn(0f, 1f)

                                coroutineScope.launch {
                                    if (backgroundAlphaAnim.value == 0f) {
                                        backgroundAlphaAnim.animateTo(
                                            targetValue = 0.2f,
                                            animationSpec =
                                            tween(
                                                durationMillis = 100,
                                                easing = FastOutSlowInEasing
                                            )
                                        )
                                    } else if (!backgroundAlphaAnim.isRunning) {
                                        backgroundAlphaAnim.snapTo(lerp(0.2f, 0.7f, t))
                                    }
                                }
                            }
                        }
                    }
                ).fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BouncingHeartIcon(
    isPostLiked: Boolean,
    onLikeChange: (Boolean) -> Unit,
    count: Int,
    onClick: () -> Unit
) {
    val sizeAnim = remember { Animatable(0f) }
    val offsetAnim = remember { Animatable(Offset.Zero, Offset.VectorConverter) }

    val alpha by animateFloatAsState(
        targetValue = if (sizeAnim.value != 0f) 0f else 1f
    )

    LaunchedEffect(isPostLiked) {
        if (isPostLiked) {
            sizeAnim.snapTo(0f)
            coroutineScope {
                launch {
                    sizeAnim.animateTo(
                        targetValue = 2.1f,
                        animationSpec = tween(durationMillis = 250, easing = EaseOutQuad)
                    )
                }
                launch {
                    offsetAnim.animateTo(
                        targetValue = Offset(x = 0f, y = -100f),
                        animationSpec = tween(250)
                    )
                }
            }

            coroutineScope {
                launch {
                    sizeAnim.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 200, easing = EaseInOutCubic)
                    )
                }
                launch {
                    offsetAnim.animateTo(
                        targetValue = Offset.Zero,
                        animationSpec = tween(250)
                    )
                }
            }
        } else {
            coroutineScope {
                launch {
                    sizeAnim.animateTo(0f, animationSpec = tween(250))
                }
                launch {
                    offsetAnim.animateTo(
                        targetValue = Offset.Zero,
                        animationSpec = tween(250)
                    )
                }
            }
        }
    }
    Row(
        modifier =
        Modifier
            .height(48.dp)
            .clickableWithScaleIndication {
                onClick()
                onLikeChange(!isPostLiked)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Box {
            Icon(
                modifier =
                Modifier
                    .size(26.dp)
                    .alpha(alpha),
                painter = MubbleDesignSystem.Icons.Favorite,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
            if (sizeAnim.value > 0f) {
                Icon(
                    modifier =
                    Modifier
                        .size(26.dp)
                        .graphicsLayer {
                            translationX = offsetAnim.value.x
                            translationY = offsetAnim.value.y
                            scaleX = sizeAnim.value
                            scaleY = sizeAnim.value
                        },
                    painter = MubbleDesignSystem.Icons.Heart,
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        }
        Text(
            text = count.toString(),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style =
            MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@Composable
fun PostActions(
    modifier: Modifier = Modifier,
    isLiked: Boolean,
    onFilledChange: (Boolean) -> Unit,
    likeCount: Int,
    commentCount: Int,
    shareCount: Int,
    onShareClick: () -> Unit
) {
    Row(
        modifier =
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BouncingHeartIcon(
            count = likeCount,
            isPostLiked = isLiked,
            onLikeChange = onFilledChange,
            onClick = {}
        )
        ActionItem(
            painter = MubbleDesignSystem.Icons.Comment,
            count = commentCount,
            onClick = {}
        )
        Spacer(modifier = Modifier.weight(1f))
        ActionItem(
            painter = MubbleDesignSystem.Icons.SendNew,
            count = shareCount,
            onClick = onShareClick
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ActionItem(
    modifier: Modifier = Modifier,
    painter: Painter,
    count: Int,
    onClick: () -> Unit
) {
    Row(
        modifier =
        modifier
            .height(48.dp)
            .wrapContentWidth()
            .clickableWithScaleIndication { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Icon(
            modifier = Modifier.size(26.dp),
            painter = painter,
            contentDescription = null
        )
        if (count > 0) {
            Text(
                count.toString(),
                style = MaterialTheme.typography.labelMediumEmphasized
            )
        }
    }
}

val post =
    DummyPostRepository.dummyFeedItems
        .filterIsInstance<ImagePostFeedItem>()
        .first()

val bubble =
    DummyPostRepository.dummyFeedItems
        .filterIsInstance<BubbleFeedItem>()
        .first()

@Preview(showBackground = true, name = "Post Card Preview")
@Composable
private fun PostCardPreview() {
    MubbleTheme {
        BasePostLayout(
            modifier = Modifier.padding(8.dp),
            item = post,
            useCard = true,
            onUserClick = {},
            onMoreClick = {},
            onPostClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Post Details Preview")
@Composable
private fun PostDetailsPreview() {
    MubbleTheme {
        Surface {
            BasePostLayout(
                item = post,
                useCard = false,
                onUserClick = {},
                onMoreClick = {},
                onPostClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Bubble Card Preview")
@Composable
private fun BubbleCardPreview() {
    MubbleTheme {
        BasePostLayout(
            modifier = Modifier.padding(8.dp),
            item = bubble,
            useCard = true,
            onUserClick = {},
            onMoreClick = {},
            onPostClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Bubble Details Preview")
@Composable
private fun BubbleDetailsPreview() {
    MubbleTheme {
        Surface {
            BasePostLayout(
                item = bubble,
                useCard = false,
                onUserClick = {},
                onMoreClick = {},
                onPostClick = {}
            )
        }
    }
}
