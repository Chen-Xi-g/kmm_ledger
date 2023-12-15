package ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.verticalDrag
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import platform.safeArea
import ui.theme.LocalColor
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

/**
 * Toast
 *
 * @author 高国峰
 * @date 2023/12/8-22:24
 *
 * @param text 文本,当文本为空时不显示
 * @param onDismissed 消失回调,需要在这里将文本置空
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Toast(
    text: String? = "",
    onDismissed: () -> Unit
) {
    if (text.isNullOrBlank()) return
    var state by remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = state, label = "toast")
    LaunchedEffect(key1 = text) {
        state = true
        delay(if(text.length > 15) 3000 else 2000)
        state = false
    }
    LaunchedEffect(state, transition.currentState, transition.isRunning){
        if (!state && !transition.currentState &&!transition.isRunning){
            onDismissed()
        }
    }
    transition.AnimatedVisibility(
        visible = { it },
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ){
        Surface(
            modifier = Modifier
                .safeArea()
                .padding(15.dp)
                .wrapContentHeight()
                .fillMaxWidth()
                .toastGesturesDetector(
                    onDismissed = {
                        state = false
                        onDismissed()
                    }
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = LocalColor.current.toastBg,
                        shape = RoundedCornerShape(8.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(15.dp)
                )
            }
        }
    }
}

private fun Modifier.toastGesturesDetector(
    onDismissed: () -> Unit,
): Modifier = composed {
    val offsetY = remember { Animatable(0f) }
    val alpha = remember { Animatable(1f) }

    pointerInput(Unit) {
        val decay = splineBasedDecay<Float>(this)
        coroutineScope {
            while (true) {
                awaitPointerEventScope {
                    // Detect a touch down event.
                    val down = awaitFirstDown()
                    val pointerId = down.id

                    val velocityTracker = VelocityTracker()
                    // Stop any ongoing animation.
                    launch(start = CoroutineStart.UNDISPATCHED) {
                        offsetY.stop()
                        alpha.stop()
                    }

                    verticalDrag(pointerId) { change ->
                        // Update the animation value with touch events.
                        val changeY = (offsetY.value + change.positionChange().y).coerceAtMost(0f)
                        launch {
                            offsetY.snapTo(changeY)
                        }
                        if (changeY == 0f) {
                            velocityTracker.resetTracking()
                        } else {
                            velocityTracker.addPosition(
                                change.uptimeMillis,
                                change.position,
                            )
                        }
                    }

                    // No longer receiving touch events. Prepare the animation.
                    val velocity = velocityTracker.calculateVelocity().y
                    val targetOffsetY = decay.calculateTargetValue(
                        offsetY.value,
                        velocity,
                    )
                    // The animation stops when it reaches the bounds.
                    offsetY.updateBounds(
                        lowerBound = -size.height.toFloat() * 3,
                        upperBound = size.height.toFloat(),
                    )
                    launch {
                        if (velocity >= 0 || targetOffsetY.absoluteValue <= size.height) {
                            // Not enough velocity; Slide back.
                            offsetY.animateTo(
                                targetValue = 0f,
                                initialVelocity = velocity,
                            )
                        } else {
                            // The element was swiped away.
                            launch { offsetY.animateDecay(velocity, decay) }
                            launch {
                                alpha.animateTo(targetValue = 0f, animationSpec = tween(300))
                                onDismissed()
                            }
                        }
                    }
                }
            }
        }
    }
        .offset {
            IntOffset(0, offsetY.value.roundToInt())
        }
        .alpha(alpha.value)
}