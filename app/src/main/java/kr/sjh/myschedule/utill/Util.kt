package kr.sjh.myschedule.utill

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.core.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kr.sjh.myschedule.ui.DateSelection
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

fun Modifier.backgroundWithSelection(
    day: CalendarDay, today: LocalDate, selection: DateSelection
): Modifier = composed {
    val (startDate, endDate) = selection
    when {
        startDate != null && endDate != null && (day.date > startDate.toLocalDate() && day.date < endDate.toLocalDate()) -> {
            background(
                Color.Red
            )
        }

        day.date == startDate?.toLocalDate() -> {
            background(
                color = Color.Red,
                shape = HalfSizeShape(clipStart = false),
            )
        }

        day.date == endDate?.toLocalDate() -> {
            background(
                color = Color.Red,
                shape = HalfSizeShape(clipStart = false),
            )
        }


        else -> {
            this
        }
    }
}

private class HalfSizeShape(private val clipStart: Boolean) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val half = size.width / 2f
        val offset = if (layoutDirection == LayoutDirection.Ltr) {
            if (clipStart) Offset(half, 0f) else Offset.Zero
        } else {
            if (clipStart) Offset.Zero else Offset(half, 0f)
        }
        return Outline.Rectangle(Rect(offset, Size(half, size.height)))
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Composable
fun rememberFirstVisibleMonthAfterScroll(state: CalendarState): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .filter { scrolling -> !scrolling }
            .collect { visibleMonth.value = state.firstVisibleMonth }
    }
    return visibleMonth.value
}