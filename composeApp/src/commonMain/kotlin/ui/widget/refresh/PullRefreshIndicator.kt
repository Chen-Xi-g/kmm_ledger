package ui.widget.refresh

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import ui.widget.refresh.ArcRadius
import ui.widget.refresh.ArrowHeight
import ui.widget.refresh.ArrowWidth
import ui.widget.refresh.CircularLoadingIndicator
import ui.widget.refresh.CrossfadeDurationMs
import ui.widget.refresh.Elevation
import ui.widget.refresh.IndicatorSize
import ui.widget.refresh.MaxProgressArc
import ui.widget.refresh.SpinnerShape
import ui.widget.refresh.StrokeWidth
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/**
 * The default indicator for Compose pull-to-refresh, based on Android's SwipeRefreshLayout.
 *
 * @param refreshing A boolean representing whether a refresh is occurring.
 * @param state The [PullRefreshState] which controls where and how the indicator will be drawn.
 * @param modifier Modifiers for the indicator.
 * @param backgroundColor The color of the indicator's background.
 * @param contentColor The color of the indicator's arc and arrow.
 * @param scale A boolean controlling whether the indicator's size scales with pull progress or not.
 * @param flipped Whether the indicator is drawn emanating from the bottom instead.
 *                This should be used with the `inverse` param of `pullRefresh`.
 */
@Deprecated(
    message = "Use the overload without `refreshing` instead",
    replaceWith = ReplaceWith(
        "PullRefreshIndicator(" +
            "state = state,\n" +
            "modifier = modifier,\n" +
            "backgroundColor = backgroundColor,\n" +
            "contentColor = contentColor,\n" +
            "scale = scale,\n" +
            "flipped = flipped," +
        ")"
    )
)
@Composable
fun PullRefreshIndicator(
    refreshing: Boolean,
    state: PullRefreshState,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    contentColor: Color = Color.Blue,
    scale: Boolean = false,
    flipped: Boolean = false,
) = PullRefreshIndicator(
    state = state,
    modifier = modifier,
    backgroundColor = backgroundColor,
    contentColor = contentColor,
    scale = scale,
    flipped = flipped
)

@Composable
fun PullRefreshIndicator(
    state: PullRefreshState,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    contentColor: Color = Color.Blue,
    scale: Boolean = false,
    flipped: Boolean = false,
) {
    val showElevation by remember(state) {
        derivedStateOf { state.refreshing || state.position > 0.5f }
    }

    Box(
        modifier = modifier
            .size(IndicatorSize)
            .pullRefreshIndicatorTransform(state, scale, flipped)
            .shadow(if (showElevation) Elevation else 0.dp, SpinnerShape, clip = true)
            .background(color = backgroundColor, shape = SpinnerShape)
    ) {
        Crossfade(
            targetState = state.refreshing,
            animationSpec = tween(durationMillis = CrossfadeDurationMs),
            label = "Refresh"
        ) { refreshing ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val spinnerSize = (ArcRadius + StrokeWidth).times(2)

                if (refreshing) {
                    CircularLoadingIndicator(
                        color = contentColor,
                        strokeWidth = StrokeWidth,
                        modifier = Modifier.size(spinnerSize),
                    )
                } else {
                    CircularArrowIndicator(state, contentColor, Modifier.size(spinnerSize))
                }
            }
        }
    }
}

/**
 * Modifier.size MUST be specified.
 */
@Composable
private fun CircularArrowIndicator(
    state: PullRefreshState,
    color: Color,
    modifier: Modifier,
) {
    val path = remember { Path().apply { fillType = PathFillType.EvenOdd } }

    val targetAlpha by remember(state) {
        derivedStateOf {
            if (state.progress >= 1f) MaxAlpha else MinAlpha
        }
    }

    val alphaState = animateFloatAsState(targetValue = targetAlpha, animationSpec = AlphaTween)

    // Empty semantics for tests
    Canvas(modifier.semantics {}) {
        val values = ArrowValues(state.progress)
        val alpha = alphaState.value

        rotate(degrees = values.rotation) {
            val arcRadius = ArcRadius.toPx() + StrokeWidth.toPx() / 2f
            val arcBounds = Rect(
                size.center.x - arcRadius,
                size.center.y - arcRadius,
                size.center.x + arcRadius,
                size.center.y + arcRadius
            )
            drawArc(
                color = color,
                alpha = alpha,
                startAngle = values.startAngle,
                sweepAngle = values.endAngle - values.startAngle,
                useCenter = false,
                topLeft = arcBounds.topLeft,
                size = arcBounds.size,
                style = Stroke(
                    width = StrokeWidth.toPx(),
                    cap = StrokeCap.Square
                )
            )
            drawArrow(path, arcBounds, color, alpha, values)
        }
    }
}

@Immutable
private class ArrowValues(
    val rotation: Float,
    val startAngle: Float,
    val endAngle: Float,
    val scale: Float
)

private fun ArrowValues(progress: Float): ArrowValues {
    // Discard first 40% of progress. Scale remaining progress to full range between 0 and 100%.
    val adjustedPercent = max(min(1f, progress) - 0.4f, 0f) * 5 / 3
    // How far beyond the threshold pull has gone, as a percentage of the threshold.
    val overshootPercent = abs(progress) - 1.0f
    // Limit the overshoot to 200%. Linear between 0 and 200.
    val linearTension = overshootPercent.coerceIn(0f, 2f)
    // Non-linear tension. Increases with linearTension, but at a decreasing rate.
    val tensionPercent = linearTension - linearTension.pow(2) / 4

    // Calculations based on SwipeRefreshLayout specification.
    val endTrim = adjustedPercent * MaxProgressArc
    val rotation = (-0.25f + 0.4f * adjustedPercent + tensionPercent) * 0.5f
    val startAngle = rotation * 360
    val endAngle = (rotation + endTrim) * 360
    val scale = min(1f, adjustedPercent)

    return ArrowValues(rotation, startAngle, endAngle, scale)
}

private fun DrawScope.drawArrow(
    arrow: Path,
    bounds: Rect,
    color: Color,
    alpha: Float,
    values: ArrowValues
) {
    arrow.reset()
    arrow.moveTo(0f, 0f) // Move to left corner
    arrow.lineTo(x = ArrowWidth.toPx() * values.scale, y = 0f) // Line to right corner

    // Line to tip of arrow
    arrow.lineTo(
        x = ArrowWidth.toPx() * values.scale / 2,
        y = ArrowHeight.toPx() * values.scale
    )

    val radius = min(bounds.width, bounds.height) / 2f
    val inset = ArrowWidth.toPx() * values.scale / 2f
    arrow.translate(
        Offset(
            x = radius + bounds.center.x - inset,
            y = bounds.center.y + StrokeWidth.toPx() / 2f
        )
    )
    arrow.close()
    rotate(degrees = values.endAngle) {
        drawPath(path = arrow, color = color, alpha = alpha)
    }
}

// Values taken from SwipeRefreshLayout
private const val MinAlpha = 0.3f
private const val MaxAlpha = 1f
private val AlphaTween = tween<Float>(300, easing = LinearEasing)