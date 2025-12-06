package com.aidsyla.mubble.feature.profile.follow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.common.components.CircleImage
import com.aidsyla.mubble.data.UserRepo

@Composable
fun SearchResults(
    onResultClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(UserRepo.dummyUsers) {
            val resultText = it.username
            ListItem(
                headlineContent = { Text(it.displayName) },
                supportingContent = { Text(it.username) },
                leadingContent = {
                    CircleImage(
                        painter = painterResource(it.profilePictureResId),
                        borderWidth = 0.1.dp
                    )
                },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                modifier =
                Modifier
                    .clickable { onResultClick(resultText) }
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
    }
}
