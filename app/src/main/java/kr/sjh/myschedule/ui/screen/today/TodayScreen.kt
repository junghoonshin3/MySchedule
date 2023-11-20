package kr.sjh.myschedule.ui.screen.today

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import kr.sjh.myschedule.ui.theme.SoftBlue
import kr.sjh.myschedule.ui.theme.VanillaIce
import kr.sjh.myschedule.utill.rememberFirstVisibleMonthAfterScroll
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun TodayScreen(
    modifier: Modifier,
    onKeepOnScreenCondition: () -> Unit,
    onSelectedDate: (LocalDate) -> Unit,
    selectedDate: LocalDate,
    onAddSchedule: () -> Unit
) {

    LaunchedEffect(key1 = Unit, block = {
        onKeepOnScreenCondition()
    })

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

    LazyColumn(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        item {
            TodayTopBar(visibleMonth.yearMonth.year, visibleMonth.yearMonth.monthValue)
            HorizontalCalendar(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, end = 10.dp),
                state = state,
                dayContent = {
                    Day(it, selectedDate == it.date, today = LocalDate.now()) { day ->
                        onSelectedDate(day.date)
                    }
                },
                monthHeader = {
                    MonthHeader(
                        modifier = Modifier.padding(vertical = 8.dp),
                        daysOfWeek = daysOfWeek,
                    )
                },
            )
        }

        item {
            SelectedDateTitle(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(50.dp),
                backgroundColor = VanillaIce,
                selectedDate
            )
        }


        items(10) {
            ScheduleItem(
                modifier = Modifier
                    .defaultMinSize(minHeight = 50.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(), content = "ddd", color = SoftBlue.copy(0.5f)
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
    day: CalendarDay, isSelected: Boolean, today: LocalDate, onClick: (CalendarDay) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .aspectRatio(0.7f)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
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
            // Disable clicks on inDates/outDates
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) },
            ),
    ) {
        val textColor = when {
            day.date.dayOfWeek == DayOfWeek.SATURDAY && day.position != DayPosition.InDate && day.position != DayPosition.OutDate -> Color.Blue.copy(
                0.5f
            )

            day.date.dayOfWeek == DayOfWeek.SUNDAY && day.position != DayPosition.InDate && day.position != DayPosition.OutDate -> Color.Red.copy(
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
        Text(
            textAlign = TextAlign.Center, text = selectedDate.format(
                dateTimeFormatter
            ), fontSize = 20.sp
        )
    }
}

@Composable
fun ScheduleItem(modifier: Modifier, content: String, color: Color = Color.Transparent) {
    Card(modifier = modifier, backgroundColor = color) {
        Row(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp)
                    .background(Color.Black)
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


