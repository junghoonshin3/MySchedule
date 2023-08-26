package kr.sjh.myschedule.components

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Today
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.util.DebugLogger
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.WeekDayPosition
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import kotlinx.coroutines.launch
import kr.sjh.myschedule.R
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.ui.theme.Crayola
import kr.sjh.myschedule.ui.theme.FontColorNomal
import kr.sjh.myschedule.ui.theme.PaleRobinEggBlue
import kr.sjh.myschedule.ui.theme.SoftBlue
import kr.sjh.myschedule.ui.theme.VanillaIce
import kr.sjh.myschedule.utill.displayText
import kr.sjh.myschedule.utill.rememberFirstVisibleMonthAfterScroll
import kr.sjh.myschedule.utill.rememberFirstVisibleWeekAfterScroll
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MyWeekCalendar(
    modifier: Modifier,
    scheduleMap: Map<Month, List<ScheduleEntity>>,
    selectedDate: LocalDate,
    onSelectedDate: (LocalDate) -> Unit
) {

    val currentDate = remember { LocalDate.now() }

    val startDate = remember { currentDate.minusYears(2) }

    val endDate = remember { currentDate.plusYears(2) }

    var isWeekMode by rememberSaveable { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()

    val weekState = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstVisibleWeekDate = currentDate,
        firstDayOfWeek = DayOfWeek.MONDAY
    )

    val monthState = rememberCalendarState(
        startMonth = startDate.yearMonth,
        endMonth = endDate.yearMonth,
        firstVisibleMonth = currentDate.yearMonth,
        firstDayOfWeek = DayOfWeek.MONDAY,
    )



    CalendarTitle(selectedDate.yearMonth,
        modifier = modifier.fillMaxWidth(),
        onSwitchCalendarType = {
            isWeekMode = !isWeekMode
            coroutineScope.launch {
                if (isWeekMode) {
                    weekState.animateScrollToWeek(selectedDate)
                } else {
                    monthState.animateScrollToMonth(selectedDate.yearMonth)
                }
            }
        },
        isWeekMode = isWeekMode,
        onTodayCalendar = {
            coroutineScope.launch {
                if (isWeekMode) {
                    weekState.animateScrollToWeek(currentDate)
                } else {
                    monthState.animateScrollToMonth(currentDate.yearMonth)
                }
            }
        })

    CalendarHeader(modifier, daysOfWeek(DayOfWeek.MONDAY))

    CalendarContent(
        modifier = modifier,
        scheduleMap = scheduleMap,
        currentDate = currentDate,
        selectedDate = selectedDate,
        weekState = weekState,
        monthState = monthState,
        isWeekMode = isWeekMode,
        onSelectedDate = onSelectedDate
    )

}

private val dateFormatter = DateTimeFormatter.ofPattern("dd")

@Composable
private fun Day(
    modifier: Modifier,
    day: LocalDate,
    isPastDay: Boolean,
    isSelected: Boolean,
    inDate: Boolean = true,
    isSchedule: Boolean = false,
    onClick: (LocalDate) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable(enabled = inDate) {
                onClick(day)
            },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = modifier
                .width(50.dp)
                .height(50.dp)
                .background(
                    color = if (isSelected) PaleRobinEggBlue else SoftBlue, shape = CircleShape
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = dateFormatter.format(day),
                fontSize = 18.sp,
                color = if (isSelected) Crayola else if (isPastDay || !inDate) Color.LightGray else FontColorNomal,
                fontWeight = FontWeight.Bold,
            )
        }
        if (isSchedule) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(5.dp)
                    .background(
                        color = VanillaIce, shape = CircleShape
                    )
                    .align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
fun CalendarContent(
    modifier: Modifier,
    scheduleMap: Map<Month, List<ScheduleEntity>>,
    currentDate: LocalDate,
    selectedDate: LocalDate,
    weekState: WeekCalendarState,
    monthState: CalendarState,
    isWeekMode: Boolean,
    onSelectedDate: (LocalDate) -> Unit
) {
    val week = rememberFirstVisibleWeekAfterScroll(weekState)
    val month = rememberFirstVisibleMonthAfterScroll(monthState)

    LaunchedEffect(key1 = week) {
        if (isWeekMode) {
            val date = if (week.days.contains(WeekDay(currentDate, WeekDayPosition.RangeDate))) {
                if (currentDate < week.days[selectedDate.dayOfWeek.value - 1].date) {
                    week.days[selectedDate.dayOfWeek.value - 1].date
                } else {
                    currentDate
                }
            } else {
                week.days[selectedDate.dayOfWeek.value - 1].date
            }
            onSelectedDate(date)
        }

    }

    LaunchedEffect(key1 = month) {
        if (!isWeekMode) {
            val date = if (month.yearMonth.isValidDay(selectedDate.dayOfMonth)) {
                when {
                    month.yearMonth == currentDate.yearMonth && month.yearMonth.month == currentDate.month && month.yearMonth.atDay(
                        selectedDate.dayOfMonth
                    ) <= currentDate -> currentDate

                    month.yearMonth.month != selectedDate.month -> month.yearMonth.atDay(
                        selectedDate.dayOfMonth
                    )

                    else -> selectedDate
                }
            } else {
                monthState.firstVisibleMonth.yearMonth.atStartOfMonth()
            }
            Log.i("sjh", "month :${date}")
            onSelectedDate(date)
        }
    }

    Card(shape = RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp)) {
        AnimatedContent(modifier = modifier, targetState = isWeekMode, transitionSpec = {
            slideInVertically(
                initialOffsetY = { 0 },
                animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
            ).togetherWith(
                slideOutVertically(
                    targetOffsetY = { 0 },
                    animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)
                )
            )
        }, label = "") { weekMode ->
            if (weekMode) {
                WeekCalendar(state = weekState, userScrollEnabled = true, dayContent = { day ->
                    var monthSchedule =
                        scheduleMap[day.date.month]?.groupBy { it.regDt } ?: emptyMap()
                    Day(
                        modifier = Modifier.background(SoftBlue),
                        day = day.date,
                        isPastDay = currentDate > day.date,
                        isSelected = selectedDate == day.date,
                        isSchedule = monthSchedule[day.date]?.isNotEmpty() ?: false
                    ) { clicked ->
                        onSelectedDate(clicked)
                    }
                })
            } else {
                HorizontalCalendar(state = monthState, dayContent = { day ->
                    var monthSchedule =
                        scheduleMap[day.date.month]?.groupBy { it.regDt } ?: emptyMap()
                    Day(
                        modifier = Modifier.background(SoftBlue),
                        day = day.date,
                        isPastDay = currentDate > day.date,
                        isSelected = selectedDate == day.date,
                        inDate = day.position == DayPosition.MonthDate,
                        isSchedule = monthSchedule[day.date]?.isNotEmpty() ?: false
                    ) { clicked ->
                        if (selectedDate != clicked) {
                            onSelectedDate(day.date)
                        }
                    }
                })
            }
        }
    }

}

@Composable
fun CalendarTitle(
    currentMonth: YearMonth,
    isWeekMode: Boolean,
    modifier: Modifier,
    onTodayCalendar: () -> Unit,
    onSwitchCalendarType: () -> Unit
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context).logger(DebugLogger(Log.DEBUG)).components {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            add(ImageDecoderDecoder.Factory())
        } else {
            add(GifDecoder.Factory())
        }
    }.build()
    Row(
        modifier = modifier
            .height(50.dp)
            .padding(start = 10.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        AsyncImage(
            modifier = Modifier
                .height(35.dp)
                .width(35.dp)
                .background(SoftBlue),
            model = getImageRequest(context, getMonthId(currentMonth.month)),
            contentDescription = "monthIcon",
            imageLoader = imageLoader,
            colorFilter = ColorFilter.tint(Color.White)
        )

        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp),
            text = currentMonth.displayText(),
            fontSize = 22.sp,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Medium,
            color = FontColorNomal
        )
        Spacer(modifier = Modifier.weight(1f))

        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()
        val color = if (isPressed) Crayola else Color.White

        Icon(
            imageVector = Icons.Default.Today,
            contentDescription = "calendarToday",
            tint = color,
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
                .padding(end = 10.dp)
                .clickable(interactionSource = interactionSource, indication = null, onClick = {
                    onTodayCalendar()
                })
        )

        Icon(imageVector = Icons.Default.CalendarMonth,
            contentDescription = "calendarMonth",
            tint = if (isWeekMode) Color.White else Crayola,
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
                .padding(end = 10.dp)
                .clickable { onSwitchCalendarType() })
    }
}

@Composable
fun CalendarHeader(modifier: Modifier, daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                color = FontColorNomal,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN),
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

fun getMonthId(month: Month): Int = when (month) {
    Month.JANUARY -> {
        R.drawable.calendar_1
    }

    Month.APRIL -> {
        R.drawable.calendar_4
    }

    Month.AUGUST -> {
        R.drawable.calendar_8
    }

    Month.DECEMBER -> {
        R.drawable.calendar_12
    }

    Month.FEBRUARY -> {
        R.drawable.calendar_2
    }

    Month.JULY -> {
        R.drawable.calendar_7
    }

    Month.MARCH -> {
        R.drawable.calendar_3
    }

    Month.MAY -> {
        R.drawable.calendar_5
    }

    Month.JUNE -> {
        R.drawable.calendar_6
    }

    Month.NOVEMBER -> {
        R.drawable.calendar_11
    }

    Month.OCTOBER -> {
        R.drawable.calendar_10
    }

    Month.SEPTEMBER -> {
        R.drawable.calendar_9
    }
}

fun getImageRequest(context: Context, resourceId: Int) =
    ImageRequest.Builder(context).data(resourceId).crossfade(true).placeholder(resourceId).build()
