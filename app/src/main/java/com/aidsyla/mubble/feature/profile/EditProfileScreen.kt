package com.aidsyla.mubble.feature.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.R
import com.aidsyla.mubble.ui.theme.MubbleTheme

@Composable
fun EditProfileScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd
        ) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f),
                contentScale = ContentScale.FillWidth
            )
            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                colorFilter = ColorFilter.tint(Color.Blue),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .offset(y = (50).dp)
                    .size(100.dp)
                    .clip(CircleShape)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    FilledIconButton(
                        onClick = {}
                    ) { Icon(imageVector = Icons.Default.Edit, contentDescription = null) }
                    Text("Edit Banner")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    FilledIconButton(
                        onClick = {}
                    ) { Icon(imageVector = Icons.Default.Edit, contentDescription = null) }
                    Text("Edit Profile")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    "President Donald J. Trump",
                    {},
                    label = { Text("Name") },
                    supportingText = { Text("25/30") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    "Obviously the Material You design doesn't improve branding, but you already know the point is for cohesion.",
                    {},
                    label = { Text("Bio") },
                    supportingText = { Text("107/200") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    "realdonaldtrump",
                    {},
                    label = { Text("Username") },
                    supportingText = { Text("15/20") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
private fun EditProfileScreenPreview() {
    Surface {
        EditProfileScreen()
    }
}

@Preview
@Composable
private fun EditProfileScreenPreview2() {
    MubbleTheme {
        Surface {
            EditProfileScreen()
        }
    }
}