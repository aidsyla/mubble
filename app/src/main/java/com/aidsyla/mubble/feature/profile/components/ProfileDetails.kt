package com.aidsyla.mubble.feature.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.feature.profile.ProfileScreenUiState

@Composable
fun ProfileDetails(
    modifier: Modifier = Modifier,
    displayName: String,
    followerCount: Int,
    followingCount: Int,
    description: String?,
    uiState: ProfileScreenUiState,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = displayName,
            style = MaterialTheme.typography.titleMedium
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "$followerCount Followers",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "$followingCount Following",
                style = MaterialTheme.typography.labelMedium
            )
        }
        if (description != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall
            )
        }
        when (uiState) {
            ProfileScreenUiState.Loading -> {

            }

            is ProfileScreenUiState.CurrentUser -> {

            }

            is ProfileScreenUiState.OtherUser -> {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {},
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(text = "Follow", style = MaterialTheme.typography.labelMedium)
                    }
                    Button(
                        onClick = {},
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(text = "Message", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}