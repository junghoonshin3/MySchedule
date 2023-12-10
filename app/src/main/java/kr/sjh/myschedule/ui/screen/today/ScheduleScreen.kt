package kr.sjh.myschedule.ui.screen.today

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue.*
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColor
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import kotlinx.collections.immutable.ImmutableMap
import kr.sjh.myschedule.data.local.entity.ScheduleWithTask
import kr.sjh.myschedule.ui.component.ModalBottomSheetDialog
import kr.sjh.myschedule.ui.screen.today.bottomsheet.BottomSheetContent
import kr.sjh.myschedule.ui.theme.PaleRobinEggBlue
import kr.sjh.myschedule.ui.theme.SoftBlue
import kr.sjh.myschedule.ui.theme.VanillaIce
import kr.sjh.myschedule.utill.Common.scheduleMaxCount
import kr.sjh.myschedule.utill.addFocusCleaner
import kr.sjh.myschedule.utill.rememberFirstVisibleMonthAfterScroll
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlin.random.Random.*


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = hiltViewModel(),
    onKeepOnScreenCondition: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()

    val currentMonth = remember { YearMonth.now() }

    val startMonth = remember { currentMonth.minusMonths(500) }

    val endMonth = remember { currentMonth.plusMonths(500) }

    val daysOfWeek = remember { daysOfWeek() }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first(),
        outDateStyle = OutDateStyle.EndOfRow,
    )

    val visibleMonth = rememberFirstVisibleMonthAfterScroll(state)

    val focusManager = LocalFocusManager.current

    val sheetState = rememberModalBottomSheetState(initialValue = Hidden, confirmValueChange = {
        when (it) {
            Expanded -> {
                true
            }

            Hidden -> {
                viewModel.bottomSheetDialog = false
                true
            }

            HalfExpanded -> {
                false
            }
        }
    }, skipHalfExpanded = true)

    LaunchedEffect(key1 = viewModel.bottomSheetDialog, block = {
        if (viewModel.bottomSheetDialog) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    })

    //연도가 바뀔시 데이터 조회
    LaunchedEffect(key1 = visibleMonth.yearMonth.year, block = {
        Log.i("sjh", "${visibleMonth.yearMonth.year}")
        val startDate = LocalDate.of(visibleMonth.yearMonth.year.minus(1), 1, 1)
        val endDate = LocalDate.of(visibleMonth.yearMonth.year.plus(1), 12, 31)
        viewModel.getScheduleWithTasks(startDate, endDate)
    })

    ModalBottomSheetDialog(modifier = Modifier
        .fillMaxSize()
        .imePadding()
        .navigationBarsPadding()
        .addFocusCleaner(focusManager), sheetState = sheetState, sheetContent = {
        BottomSheetContent(title = uiState.bottomSheetUiState.title,
            startDateTime = LocalDateTime.of(
                uiState.bottomSheetUiState.startDate, uiState.bottomSheetUiState.startTime
            ),
            endDateTime = LocalDateTime.of(
                uiState.bottomSheetUiState.endDate, uiState.bottomSheetUiState.endTime
            ),
            onTitleChange = viewModel::onTitleChange,
            onSave = viewModel::onSave,
            onDateRange = viewModel::onDateRange,
            onAlarmTime = { },
            onAlarm = { },
            onCancel = { /*TODO*/ },
            onDone = {})
    }, content = {
        ScheduleScreen(
            modifier = Modifier.fillMaxSize(),
            yearScheduleMap = uiState.yearScheduleMap,
            calendarState = state,
            visibleMonth = visibleMonth,
            daysOfWeek = daysOfWeek,
            selectedDate = uiState.selectedDate,
            onKeepOnScreenCondition = onKeepOnScreenCondition,
            onAddSchedule = viewModel::onAddSchedule,
            onScheduleClick = viewModel::setSelectedSchedule,
            onSelectedDate = viewModel::setSelectedDate
        )
    })


}

@Composable
private fun ScheduleScreen(
    modifier: Modifier,
    yearScheduleMap: ImmutableMap<LocalDate, List<ScheduleWithTask>>,
    calendarState: CalendarState,
    visibleMonth: CalendarMonth,
    daysOfWeek: List<DayOfWeek>,
    selectedDate: LocalDate,
    onKeepOnScreenCondition: () -> Unit,
    onAddSchedule: () -> Unit,
    onScheduleClick: (ScheduleWithTask) -> Unit,
    onSelectedDate: (LocalDate) -> Unit
) {

    LaunchedEffect(key1 = Unit, block = {
        onKeepOnScreenCondition()
    })

    LazyColumn(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        item {
            TodayTopBar(visibleMonth.yearMonth.year, visibleMonth.yearMonth.monthValue)
            HorizontalCalendar(modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 10.dp),
                state = calendarState,
                dayContent = { calendarDay ->
                    Log.i("sjh", "${yearScheduleMap[calendarDay.date]?.size}")
                    Day(
                        calendarDay,
                        selectedDate == calendarDay.date,
                        list = yearScheduleMap[calendarDay.date].orEmpty(),
                        today = LocalDate.now()
                    ) { day ->
                        onSelectedDate(day.date)
                    }
                },
                monthHeader = {
                    MonthHeader(
                        modifier = Modifier.padding(vertical = 8.dp),
                        daysOfWeek = daysOfWeek,
                    )
                })
        }

        item {
            SelectedDateTitle(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(70.dp),
                backgroundColor = PaleRobinEggBlue.copy(0.5f),
                selectedDate
            )
        }

        items(yearScheduleMap[selectedDate].orEmpty()) {
            ScheduleItem(
                modifier = Modifier
                    .defaultMinSize(minHeight = 50.dp)
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                onClick = { onScheduleClick(it) },
                content = it.schedule.title,
                backgroundColor = SoftBlue.copy(0.5f),
                color = Color(it.schedule.color)
            )
        }
        item {
            ScheduleAddButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp), onAddSchedule = onAddSchedule
            )
        }
    }
}

@Composable
fun TodayTopBar(year: Int, month: Int) {
    TopAppBar(
        backgroundColor = SoftBlue,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentPadding = PaddingValues(10.dp)
    ) {
        Text(
            text = year.toString(),
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = month.toString(),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun Day(
    day: CalendarDay,
    isSelected: Boolean,
    today: LocalDate,
    list: List<ScheduleWithTask>,
    onClick: (CalendarDay) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .aspectRatio(0.7f)
            .border(
                width = if (isSelected) 3.dp else 0.dp,
                color = if (isSelected) VanillaIce.copy(0.5f) else Color.Transparent,
            )
            .padding(1.dp)
            .background(
                color = if (day.date == today) {
                    Color.Yellow.copy(0.2f)
                } else {
                    SoftBlue
                }
            )
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) },
            ),
    ) {

        val textColor = when {
            day.date.dayOfWeek == DayOfWeek.SATURDAY && day.position == DayPosition.MonthDate -> Color.Blue.copy(
                0.5f
            )

            day.date.dayOfWeek == DayOfWeek.SUNDAY && day.position == DayPosition.MonthDate -> Color.Red.copy(
                0.5f
            )

            day.position == DayPosition.InDate || day.position == DayPosition.OutDate -> Color.LightGray

            else -> {
                Color.Black.copy(0.5f)
            }
        }
        Text(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 3.dp, end = 4.dp),
            text = day.date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 12.sp,
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .alpha(
                    if (day.position != DayPosition.MonthDate) {
                        0.3f
                    } else {
                        1f
                    }
                )
                .fillMaxWidth()
                .padding(1.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            for (item in list.take(scheduleMaxCount)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(
                            color = Color(item.schedule.color)
                        ),
                ) {
                    Text(
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontSize = 8.sp,
                        text = item.schedule.title,
                        color = Color.Black
                    )
                }
            }
            if (list.size > scheduleMaxCount) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(end = 1.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Text(
                        color = Color.White,
                        fontSize = 10.sp,
                        text = "+${list.size - scheduleMaxCount}"
                    )
                }

            }
        }
    }
}

@Composable
private fun MonthHeader(
    modifier: Modifier = Modifier,
    daysOfWeek: List<DayOfWeek> = emptyList(),
) {
    Row(modifier.fillMaxWidth()) {

        for (dayOfWeek in daysOfWeek) {
            val textColor = when (dayOfWeek) {
                DayOfWeek.SUNDAY -> Color.Red.copy(0.5f)
                DayOfWeek.SATURDAY -> Color.Blue.copy(0.5f)
                else -> {
                    Color.Black.copy(0.5f)
                }
            }
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = textColor,
                text = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN),
                fontWeight = FontWeight.Light,
            )
        }
    }
}

@Composable
fun SelectedDateTitle(modifier: Modifier, backgroundColor: Color, selectedDate: LocalDate) {
    val dateTimeFormatter = DateTimeFormatter.ofPattern(
        "yyyy년 M월 dd일 (E)", Locale.KOREAN
    )
    Card(
        modifier = modifier, backgroundColor = backgroundColor
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                color = Color.Black.copy(0.5f), text = selectedDate.format(
                    dateTimeFormatter
                ), fontSize = 20.sp
            )
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScheduleItem(
    modifier: Modifier,
    onClick: () -> Unit,
    content: String,
    color: Color,
    backgroundColor: Color = Color.Transparent
) {
    Card(modifier = modifier, backgroundColor = backgroundColor, onClick = onClick) {
        Row(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(5.dp)
                    .background(color)
            )
            Text(
                color = Color.White,
                text = content,
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .fillMaxHeight()
                    .padding(start = 10.dp)
            )
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    imageVector = Icons.Default.Alarm,
                    contentDescription = "",
                )
                Image(
                    imageVector = Icons.Default.CheckBoxOutlineBlank,
                    contentDescription = "",
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScheduleAddButton(modifier: Modifier, onAddSchedule: () -> Unit) {
    Card(modifier = modifier, onClick = {
        onAddSchedule()
    }) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                alignment = Alignment.Center,
                imageVector = Icons.Default.Add,
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "일정 추가", fontSize = 20.sp)
        }
    }
}

fun generateRandomColor(): Color {
    val random = Default
    val red = random.nextInt(256)
    val green = random.nextInt(256)
    val blue = random.nextInt(256)

    return Color(red = red, green = green, blue = blue)
}

