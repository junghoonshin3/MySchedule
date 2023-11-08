package kr.sjh.myschedule.ui.screen.bottomsheet

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.commandiron.wheel_picker_compose.WheelDateTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.core.yearMonth
import kr.sjh.myschedule.ui.screen.bottomsheet.ContinuousSelectionHelper.getSelection
import kr.sjh.myschedule.ui.theme.FontColorNomal
import kr.sjh.myschedule.ui.theme.SoftBlue
import kr.sjh.myschedule.utill.backgroundHighlight
import kr.sjh.myschedule.utill.common.MenuValue
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

data class DateSelection(val startDate: LocalDate? = null, val endDate: LocalDate? = null) {
    val daysBetween by lazy(LazyThreadSafetyMode.NONE) {
        if (startDate == null || endDate == null) null else {
            ChronoUnit.DAYS.between(startDate, endDate)
        }
    }
}

object ContinuousSelectionHelper {
    fun getSelection(
        clickedDate: LocalDate,
        dateSelection: DateSelection,
    ): DateSelection {
        val (selectionStartDate, selectionEndDate) = dateSelection
        return if (selectionStartDate != null) {
            if (clickedDate < selectionStartDate || selectionEndDate != null) {
                DateSelection(startDate = clickedDate, endDate = null)
            } else if (clickedDate != selectionStartDate) {
                DateSelection(startDate = selectionStartDate, endDate = clickedDate)
            } else {
                DateSelection(startDate = clickedDate, endDate = null)
            }
        } else {
            DateSelection(startDate = clickedDate, endDate = null)
        }
    }

    fun isInDateBetweenSelection(
        inDate: LocalDate,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Boolean {
        if (startDate.yearMonth == endDate.yearMonth) return false
        if (inDate.yearMonth == startDate.yearMonth) return true
        val firstDateInThisMonth = inDate.yearMonth.nextMonth.atStartOfMonth()
        return firstDateInThisMonth in startDate..endDate && startDate != firstDateInThisMonth
    }

    fun isOutDateBetweenSelection(
        outDate: LocalDate,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Boolean {
        if (startDate.yearMonth == endDate.yearMonth) return false
        if (outDate.yearMonth == endDate.yearMonth) return true
        val lastDateInThisMonth = outDate.yearMonth.previousMonth.atEndOfMonth()
        return lastDateInThisMonth in startDate..endDate && endDate != lastDateInThisMonth
    }
}

@Composable
fun BottomSheetContent(text: String, onTextChange: (String) -> Unit) {

    var isPriorityShow by remember {
        mutableStateOf(false)
    }

    var selection by remember {
        mutableStateOf(DateSelection())
    }

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier.weight(0.8f),
                value = text,
                onValueChange = {
                    Log.i("sjh", "${onTextChange.hashCode()}")
                    onTextChange(it)
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                label = {
                    Text(
                        text = "할일을 적어주세요", color = FontColorNomal
                    )
                },
            )
            Spacer(modifier = Modifier.width(5.dp))
            Button(elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = SoftBlue, contentColor = SoftBlue
                ),
                modifier = Modifier
                    .weight(0.2f)
                    .aspectRatio(1f)
                    .padding(5.dp),
                onClick = {

                }) {
                Image(
                    imageVector = Icons.Default.Send,
                    contentDescription = null,
                    modifier = Modifier.wrapContentSize()
                )
            }
        }
        Box(modifier = Modifier.wrapContentSize()) {
            Column(
                modifier = Modifier.wrapContentSize()
            ) {
                var selectedMenu by remember {
                    mutableStateOf(MenuValue.Calendar)
                }
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    items(MenuValue.values(), key = { it.hashCode() }) { menu ->
                        Button(onClick = {
                            selectedMenu = menu
                        }) {
                            Text(text = menu.value)
                        }
                    }
                }
                AnimatedContent(contentKey = {
                    it.hashCode()
                }, targetState = selectedMenu, label = "") {
                    when (it) {
                        MenuValue.Calendar -> {
                            HorizontalCalendar(state = rememberCalendarState(
                                startMonth = YearMonth.of(1950, 1),
                                endMonth = YearMonth.of(2050, 12),
                                firstDayOfWeek = DayOfWeek.MONDAY,
                                firstVisibleMonth = YearMonth.now()
                            ), monthHeader = { month ->
                                BottomSheetCalendarHeader(month)
                            }, dayContent = { day ->
                                BottomSheetCalendarDay(day, LocalDate.now(), selection) {
                                    if (day.position == DayPosition.MonthDate &&
                                        (day.date == LocalDate.now() || day.date.isAfter(
                                            LocalDate.now()
                                        ))
                                    ) {
                                        selection = getSelection(
                                            clickedDate = day.date,
                                            dateSelection = selection,
                                        )
                                    }
                                }
                            })
                        }

                        MenuValue.Alarm -> {
                            WheelDateTimePicker(
                                modifier = Modifier.fillMaxWidth(),
                                startDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")),
                                timeFormat = TimeFormat.AM_PM,
                                size = DpSize(300.dp, 150.dp),
                                rowCount = 3,
                                textColor = Color.Black,
                                selectorProperties = WheelPickerDefaults.selectorProperties(
                                    enabled = true,
                                    shape = RoundedCornerShape(0.dp),
                                    color = Color(0xffF7F2FA).copy(alpha = 0.2f),
                                    border = BorderStroke(1.dp, Color.White)
                                )
                            ) {

                            }
                        }

                    }
                }

            }
        }
    }
}

@Composable
private fun BottomSheetCalendarHeader(calendarMonth: CalendarMonth) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        text = "${calendarMonth.yearMonth.year}.${calendarMonth.yearMonth.monthValue}"
    )
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        calendarMonth.weekDays.first().map { calendarDay ->
            Text(
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
                color = Color.White,
                text = calendarDay.date.dayOfWeek.getDisplayName(
                    TextStyle.FULL, Locale.KOREAN
                ).first().toString()
            )
        }
    }
}

@Composable
private fun BottomSheetCalendarDay(
    day: CalendarDay, today: LocalDate,
    selection: DateSelection, onClickedDate: (LocalDate) -> Unit
) {
    var textColor = Color.Transparent
    Box(
        modifier = Modifier
            .aspectRatio(1f) // This is important for square-sizing!
            .clickable(
                enabled = day.position == DayPosition.MonthDate && day.date >= today,
                onClick = { onClickedDate(day.date) },
            )
            .backgroundHighlight(
                day = day,
                today = today,
                selection = selection,
                selectionColor = Color.Black.copy(alpha = 0.9f),
                continuousSelectionColor = Color.LightGray.copy(alpha = 0.3f),
            ) { textColor = it },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Preview
@Composable
private fun BottomSheetContentPreview() {
    BottomSheetContent("tlqkf", {})
}
