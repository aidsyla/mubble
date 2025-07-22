package com.aidsyla.mubble.feature.circle

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.ui.theme.MubbleTheme

@Composable
fun ChipsRow(modifier: Modifier = Modifier) {
    var postsSelected by remember { mutableStateOf(false) }
    var bubblesSelected by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
    ) {
        AssistChip(
            onClick = { },
            label = { Text("Sort") },
            leadingIcon = {
                Icon(
                    painter = MubbleTheme.Icons.Sort,
                    contentDescription = "Localized description",
                    Modifier.size(AssistChipDefaults.IconSize)
                )
            }
        )
        FilterChip(
            modifier = Modifier.padding(horizontal = 8.dp),
            onClick = { postsSelected = !postsSelected },
            label = {
                Text("Posts")
            },
            selected = postsSelected,
            leadingIcon = if (postsSelected) {
                {
                    Icon(
                        painter = MubbleTheme.Icons.Check,
                        contentDescription = "Done icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            } else {
                null
            },
        )
        FilterChip(
            onClick = { bubblesSelected = !bubblesSelected },
            label = {
                Text("Bubbles")
            },
            selected = bubblesSelected,
            leadingIcon = if (bubblesSelected) {
                {
                    Icon(
                        painter = MubbleTheme.Icons.Check,
                        contentDescription = "Done icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            } else {
                null
            },
        )
    }
}