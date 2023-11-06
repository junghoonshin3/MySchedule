package kr.sjh.myschedule.utill

import android.graphics.Rect
import android.util.Log
import android.view.ViewTreeObserver
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.core.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)} ${this.year}"
}

private fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.KOREAN)
}

@Composable
fun rememberFirstVisibleWeekAfterScroll(
    state: WeekCalendarState
): Week {
    val visibleWeek = remember(state) { mutableStateOf(state.firstVisibleWeek) }
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }.filter { scrolling -> !scrolling }.collect {
            visibleWeek.value = state.firstVisibleWeek
        }
    }
    return visibleWeek.value
}

@Composable
fun rememberFirstVisibleMonthAfterScroll(
    state: CalendarState
): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }.filter { scrolling -> !scrolling }.collect {
            visibleMonth.value = state.firstVisibleMonth
        }
    }
    return visibleMonth.value
}

interface MultipleEventsCutterManager {
    fun processEvent(event: () -> Unit)
}

@OptIn(FlowPreview::class)
@Composable
fun <T> multipleEventsCutter(
    content: @Composable (MultipleEventsCutterManager) -> T
): T {
    val debounceState = remember {
        MutableSharedFlow<() -> Unit>(
            replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    }

    val result = content(object : MultipleEventsCutterManager {
        override fun processEvent(event: () -> Unit) {
            debounceState.tryEmit(event)
        }
    })

    LaunchedEffect(true) {
        debounceState.debounce(300L).collect { onClick ->
            onClick.invoke()
        }
    }

    return result
}

fun Modifier.clickableSingle(
    enabled: Boolean = true, onClickLabel: String? = null, role: Role? = null, onClick: () -> Unit
) = composed(inspectorInfo = debugInspectorInfo {
    name = "clickable"
    properties["enabled"] = enabled
    properties["onClickLabel"] = onClickLabel
    properties["role"] = role
    properties["onClick"] = onClick
}) {
    multipleEventsCutter { manager ->
        Modifier
            .focusable(false)
            .clickable(enabled = enabled,
                onClickLabel = onClickLabel,
                onClick = { manager.processEvent { onClick() } },
                role = role,
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() })
    }
}

fun Modifier.addFocusCleaner(focusManager: FocusManager, doOnClear: () -> Unit = {}): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(onTap = {
            doOnClear()
            focusManager.clearFocus()
        })
    }
}
@Composable
internal fun keyboardAsState(): State<Int> {
    val keyboardState = remember { mutableIntStateOf(0) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardState.intValue = if (keypadHeight > screenHeight * 0.15) {
                keypadHeight
            } else {
                keypadHeight
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardState
}