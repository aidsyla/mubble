package com.aidsyla.mubble.common.components.for_reference

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.R

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Testing(modifier: Modifier = Modifier) {
    var showDetails by remember {
        mutableStateOf(false)
    }
    SharedTransitionLayout {
        AnimatedContent(
            showDetails,
            label = "basic_transition"
        ) { targetState ->
            if (!targetState) {
                MainContent(
                    onShowDetails = {
                        showDetails = true
                    },
                    animatedVisibilityScope = this@AnimatedContent,
                    sharedTransitionScope = this@SharedTransitionLayout
                )
            } else {
                DetailsContent(
                    onBack = {
                        showDetails = false
                    },
                    animatedVisibilityScope = this@AnimatedContent,
                    sharedTransitionScope = this@SharedTransitionLayout
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    onShowDetails: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val lazyListState = rememberLazyListState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mubble") },
            )
        }
    ) {
        LazyColumn(
            state = lazyListState
        ) {
            item {
                OutlinedCard(
                    onClick = onShowDetails,
                    modifier = Modifier.padding(it).offset(y = (-20).dp)
                ) {
                    Row(
                        modifier = modifier
                            .padding(
                                start = 16.dp, top = 12.dp, bottom = 12.dp, end = 4.dp
                            )
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        with(sharedTransitionScope) {
                            Image(
                                painter = painterResource(R.drawable.profile_1),
                                contentDescription = null,
                                modifier = Modifier
                                    .sharedElement(
                                        rememberSharedContentState(key = "image"),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                    )
                                    .size(140.dp)
                                    .clip(shape = CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                text = "Filan Fisteku",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                        Spacer(Modifier.weight(1f))
                        IconButton(onClick = { }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More options")
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(color = Color.Blue)
                    )
                }
            }
            item {
                Box(modifier = Modifier.fillMaxWidth().height(1300.dp).background(color = Color.Cyan))
            }
        }

    }

}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun DetailsContent(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        // Make title clickable to view profile
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        with(sharedTransitionScope) {
                            Image(
                                painter = painterResource(R.drawable.profile_1),
                                contentDescription = null,
                                modifier = Modifier
                                    .sharedElement(
                                        rememberSharedContentState(key = "image"),
                                        animatedVisibilityScope = animatedVisibilityScope
                                    )
                                    .size(48.dp)
                                    .clip(shape = CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                text = "Filan Fisteku",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }
            )
        }
    )
    {
        OutlinedCard(
            onClick = onBack,
            modifier = Modifier.padding(it)
        ) {
            Spacer(modifier = Modifier.height(400.dp))
            Row(
                modifier = modifier
                    .padding(
                        start = 16.dp, top = 12.dp, bottom = 12.dp, end = 4.dp
                    )
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More options")
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(color = Color.Blue)
            )
            Text("Description text")
            Text("date")
            Text("time")
            Text("likes")
        }
    }
}