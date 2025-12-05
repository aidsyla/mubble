package com.aidsyla.mubble.feature.profile.follow

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.aidsyla.mubble.common.components.AlertDialog
import com.aidsyla.mubble.common.components.CircleImage
import com.aidsyla.mubble.model.FollowType
import com.aidsyla.mubble.ui.theme.MubbleTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun UserItem(
    modifier: Modifier = Modifier,
    isCurrentUser: Boolean,
    listType: FollowType,
    userId: String,
    username: String,
    @DrawableRes profilePictureResId: Int,
    onUserClick: (String) -> Unit,
    onFollowClick: (String) -> Unit,
    onMessageClick: (String) -> Unit,
) {
    var openDialog by remember { mutableStateOf(false) }

    when (listType) {
        FollowType.FOLLOWERS -> {
            if (openDialog) {
                AlertDialog(
                    onDismissRequest = { openDialog = false },
                    onConfirmation = {},
                    dialogTitle = "Remove follower?",
                    dialogText =
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                append(username)
                            }
                            append(text = " will no longer see your posts in their feed.")
                        },
                    confirmButtonText = "Remove",
                )
            }
        }

        FollowType.FOLLOWING -> {
            if (openDialog) {
                AlertDialog(
                    onDismissRequest = { openDialog = false },
                    onConfirmation = {},
                    dialogTitle = "Stop following?",
                    dialogText =
                        buildAnnotatedString {
                            append(text = "You won’t see")
                            append(text = " ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                append(username)
                                append("'s")
                            }
                            append(text = " ")
                            append(text = "updates anymore.")
                        },
                    confirmButtonText = "Unfollow",
                )
            }
        }
    }

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable { onUserClick(userId) }
                .padding(start = 16.dp, end = 4.dp, top = 6.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircleImage(
            painter = painterResource(profilePictureResId),
            borderWidth = 0.1.dp,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = username,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
        )
        if (isCurrentUser) {
            Spacer(modifier = Modifier.weight(1f))
            FilledIconButton(
                onClick = { onMessageClick(userId) },
                modifier =
                    modifier
                        .minimumInteractiveComponentSize()
                        .size(
                            IconButtonDefaults.extraSmallContainerSize(
                                IconButtonDefaults.IconButtonWidthOption.Wide,
                            ),
                        ),
                shapes =
                    IconButtonDefaults.shapes(
                        shape = IconButtonDefaults.extraSmallRoundShape,
                        pressedShape = IconButtonDefaults.extraSmallPressedShape,
                    ),
            ) {
                Icon(
                    modifier =
                        Modifier.size(
                            IconButtonDefaults.extraSmallIconSize,
                        ),
                    painter = MubbleTheme.Icons.Message,
                    contentDescription = null,
                )
            }
            IconButton(
                onClick = { openDialog = true },
                modifier =
                    modifier
                        .minimumInteractiveComponentSize()
                        .size(
                            IconButtonDefaults.extraSmallContainerSize(
                                IconButtonDefaults.IconButtonWidthOption.Uniform,
                            ),
                        ),
            ) {
                Icon(
                    modifier =
                        Modifier.size(
                            IconButtonDefaults.extraSmallIconSize,
                        ),
                    painter = MubbleTheme.Icons.Close,
                    contentDescription = null,
                )
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))
            val size = ButtonDefaults.ExtraSmallContainerHeight
            Button(
                onClick = { onFollowClick(userId) },
                modifier = Modifier.heightIn(size),
                shapes = ButtonDefaults.shapes(),
                contentPadding = ButtonDefaults.contentPaddingFor(size),
            ) {
                Icon(
                    painter = MubbleTheme.Icons.PersonAdd,
                    contentDescription = "Localized description",
                    modifier = Modifier.size(ButtonDefaults.iconSizeFor(size)),
                )
                Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(size)))
                Text("Follow")
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}
