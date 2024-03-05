package ui.widget.refresh

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.platform.inspectable

/**
 * A modifier for translating the position and scaling the size of a pull-to-refresh indicator
 * based on the given [PullRefreshState].
 *
 * @param state The [PullRefreshState] which determines the position of the indicator.
 * @param scale A boolean controlling whether the indicator's size scales with pull progress or not.
 * @param flipped Whether the indicator is drawn emanating from the bottom instead.
 */
fun Modifier.pullRefreshIndicatorTransform(
    state: PullRefreshState,
    scale: Boolean = false,
    flipped: Boolean = false,
) = inspectable(
    inspectorInfo = debugInspectorInfo {
        name = "pullRefreshIndicatorTransform"
        properties["state"] = state
        properties["scale"] = scale
        properties["flipped"] = flipped
    }
) {
    Modifier
        // Essentially we only want to clip the at the top, so the indicator will not appear when
        // the position is 0. It is preferable to clip the indicator as opposed to the layout that
        // contains the indicator, as this would also end up clipping shadows drawn by items in a
        // list for example - so we leave the clipping to the scrolling container. We use MAX_VALUE
        // for the other dimensions to allow for more room for elevation / arbitrary indicators - we
        // only ever really want to clip at the top edge.
        .drawWithContent {
            clipRect(
                top = if (!flipped) 0f else -Float.MAX_VALUE,
                left = -Float.MAX_VALUE,
                right = Float.MAX_VALUE,
                bottom = if (!flipped) Float.MAX_VALUE else size.height,
            ) {
                this@drawWithContent.drawContent()
            }
        }
        .graphicsLayer {
            translationY = if (!flipped) {
                state.position - size.height
            } else {
                -state.position + size.height
            }

            if (scale && !state.refreshing) {
                val scaleFraction = LinearOutSlowInEasing
                    .transform(state.position / state.threshold)
                    .coerceIn(0f, 1f)
                scaleX = scaleFraction
                scaleY = scaleFraction
            }
        }
}