package com.aidsyla.mubble.util

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.semantics.Role
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun Modifier.clickableWithScaleIndication(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    indicationType: IndicationType = IndicationType.ICONS,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    this.clickable(
        interactionSource = interactionSource,
        indication = ScaleIndicationNodeFactory(indicationType),
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        onClick = onClick
    )
}

enum class IndicationType {
    ICONS,
    CARDS
}

class ScaleIndicationNodeFactory(
    val indicationType: IndicationType
) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode = ScaleIndicationNode(interactionSource, indicationType)

    override fun hashCode(): Int = -1

    override fun equals(other: Any?) = other === this
}

private class ScaleIndicationNode(
    private val interactionSource: InteractionSource,
    private val indicationType: IndicationType
) : Modifier.Node(),
    DrawModifierNode {
    var currentPressPosition: Offset = Offset.Zero
    val animatedScalePercent = Animatable(1f)

    private suspend fun animateToPressed(pressPosition: Offset) {
        currentPressPosition = pressPosition
        val targetValue =
            when (indicationType) {
                IndicationType.ICONS -> 0.9f
                IndicationType.CARDS -> 0.98f
            }
        animatedScalePercent.animateTo(targetValue, tween(durationMillis = 300))
    }

    private suspend fun animateToResting() {
        animatedScalePercent.animateTo(1f, tween(durationMillis = 300))
    }

    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collectLatest { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> animateToPressed(interaction.pressPosition)
                    is PressInteraction.Release -> animateToResting()
                    is PressInteraction.Cancel -> animateToResting()
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        scale(
            scale = animatedScalePercent.value,
            pivot = currentPressPosition
        ) {
            this@draw.drawContent()
        }
    }
}
