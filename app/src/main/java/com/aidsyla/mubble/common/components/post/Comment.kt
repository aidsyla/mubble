package com.aidsyla.mubble.common.components.post

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.R

@Composable
fun CommentList(modifier: Modifier = Modifier) {
    Column {
        CommentItem()
        Replies()
        CommentItem(startPadding = 44.dp)
    }
}

@Composable
fun Replies(modifier: Modifier = Modifier) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp), contentAlignment = Alignment.Center) {
        Text(text = "3 Replies")
    }
}

@Composable
fun CommentItem(modifier: Modifier = Modifier, startPadding: Dp = 8.dp) {
    Row(
        modifier = modifier
            .padding(start = startPadding, end = 8.dp, top = 4.dp, bottom = 4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clip(shape = CircleShape)
            )
            Column {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "filanfisteku")
                        Text(text = "1h")
                    }
                    Text(text = "Obviously the Material")
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { }
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    Text(text = "Reply")
                }
            }
        }
        Row(
            modifier = Modifier
                .size(48.dp)
                .padding()
                .clickable { },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null
            )
            Text(text = "3")
        }
    }
}

@Preview
@Composable
private fun CommentItemPreview() {
    CommentList()
}