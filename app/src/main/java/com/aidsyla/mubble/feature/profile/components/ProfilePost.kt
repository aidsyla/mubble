package com.aidsyla.mubble.feature.profile.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun ProfilePost(
    modifier: Modifier = Modifier,
    @DrawableRes imageResId: Int
) {
    Image(
        painter = painterResource(imageResId), contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.medium)
            .aspectRatio(1f),
        contentScale = ContentScale.Crop
    )
}