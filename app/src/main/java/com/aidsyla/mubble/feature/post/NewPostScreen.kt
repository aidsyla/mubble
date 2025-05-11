package com.aidsyla.mubble.feature.post

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.R

@Preview
@Composable
fun NewPostScreen(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    colorFilter = ColorFilter.tint(Color.Blue),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Button(
                    onClick = {},
                    modifier = Modifier.height(32.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("My Profile", style = MaterialTheme.typography.labelMedium)
                }
                Card(
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("0/300", modifier = Modifier.padding(4.dp))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Obviously the Material You design doesn't improve branding, but you already know the point is for cohesion. Obviously the Material You design doesn't improve branding, but you already know the point is for cohesion. Obviously the Material You design doesn't improve branding, but you already know the ")
            }
        }
        Card(
            shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
        ) {
            IconButton(
                onClick = {}
            ) {
                Icon(imageVector = Icons.Default.Call, contentDescription = null)
            }
            IconButton(
                onClick = {}
            ) {
                Icon(imageVector = Icons.Default.MailOutline, contentDescription = null)
            }
        }
    }
}

@Preview
@Composable
fun SendScreen(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
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