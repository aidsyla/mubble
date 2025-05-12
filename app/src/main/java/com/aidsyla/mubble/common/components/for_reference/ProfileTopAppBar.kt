package com.aidsyla.mubble.common.components.for_reference

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopAppBarOld(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Box(
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.extraLarge)
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
                    .padding(
                        vertical = 6.dp, horizontal = 8.dp
                    ),
            ) {
                Text(
                    text = "realdonaldtrump",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color =
                            MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        navigationIcon = {
            FilledTonalIconButton(
                onClick = {},
                Modifier.size(32.dp)
            ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
        },
        actions = {
            FilledTonalIconButton(onClick = {}, Modifier.size(32.dp)) {
                Icon(
                    Icons.Default.MoreVert,
                    null
                )
            }
        })
}

@Composable
fun ProfileTopAppBarOld_2(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.zIndex(1f)
    ) {
        FilledTonalIconButton(
            onClick = {},
            Modifier.size(32.dp)
        ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .clip(shape = MaterialTheme.shapes.extraLarge)
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                .padding(
                    vertical = 6.dp, horizontal = 8.dp
                ),
        ) {
            Text(
                text = "realdonaldtrump",
                style = MaterialTheme.typography.titleSmall.copy(
                    color =
                        MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        FilledTonalIconButton(onClick = {}, Modifier.size(32.dp)) {
            Icon(
                Icons.Default.MoreVert,
                null
            )
        }
    }
}