package com.aidsyla.mubble.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TabButtons(
    modifier: Modifier = Modifier,
    spaceBetween: Dp = ButtonGroupDefaults.ConnectedSpaceBetween,
    options: List<String>,
    selectedIcons: List<Painter>,
    unselectedIcons: List<Painter>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween),
    ) {
        val modifiers = List(options.size) { Modifier.weight(1f) }
        val last = options.lastIndex
        options.forEachIndexed { index, option ->
            ToggleButton(
                checked = selectedIndex == index,
                onCheckedChange = {
                    onTabSelected(index)
                },
                modifier = modifiers[index].semantics { role = Role.Tab },
                shapes =
                    when (index) {
                        0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                        last -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                        else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                    },
                colors = ToggleButtonDefaults.toggleButtonColors(
                    checkedContainerColor = MaterialTheme.colorScheme.primary,
                    checkedContentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            ) {
                Icon(
                    painter = if (selectedIndex == index) selectedIcons[index] else unselectedIcons[index] ,
                    contentDescription = "Localized description"
                )
                Spacer(Modifier.size(ToggleButtonDefaults.IconSpacing))
                Text(option)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TabButtons(
    modifier: Modifier = Modifier,
    spaceBetween: Dp = ButtonGroupDefaults.ConnectedSpaceBetween,
    selectedIcons: List<Painter>,
    unselectedIcons: List<Painter>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween),
    ) {
        val modifiers = listOf(Modifier.weight(1f), Modifier.weight(1f))

        unselectedIcons.forEachIndexed { index, icon ->
            ToggleButton(
                checked = selectedIndex == index,
                onCheckedChange = {
                    onTabSelected(index)
                },
                modifier = modifiers[index].semantics { role = Role.Tab },
                shapes =
                    when (index) {
                        0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                        else -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                    },
                colors = ToggleButtonDefaults.toggleButtonColors(
                    checkedContainerColor = MaterialTheme.colorScheme.primary,
                    checkedContentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            ) {
                Icon(
                    painter = if (selectedIndex == index) selectedIcons[index] else icon,
                    contentDescription = "Localized description"
                )
            }
        }
    }
}