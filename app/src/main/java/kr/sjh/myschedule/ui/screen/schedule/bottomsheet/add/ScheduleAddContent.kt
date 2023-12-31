package kr.sjh.myschedule.ui.screen.schedule.bottomsheet.add

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.commandiron.wheel_picker_compose.WheelDateTimePicker
import com.commandiron.wheel_picker_compose.WheelTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import kr.sjh.myschedule.ui.screen.schedule.ScheduleEvent
import kr.sjh.myschedule.ui.theme.PaleRobinEggBlue
import kr.sjh.myschedule.ui.theme.VanillaIce
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle.*
import java.util.Locale

@Composable
fun ScheduleAddContent(
    modifier: Modifier,
    bottomSheetVisible: Boolean,
    onEvent: (ScheduleEvent) -> Unit,
    startDate: LocalDateTime,
    endDate: LocalDateTime,
    buttonText: String,
) {

    LazyColumn(
        modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
    ) {

        item {
            DateTimeContent(bottomSheetVisible, startDate, endDate, onEvent)
        }

        item {
            ScheduleAddButton(
                buttonText = buttonText, onEvent = onEvent
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DateTimeContent(
    bottomSheetVisible: Boolean,
    startDate: LocalDateTime,
    endDate: LocalDateTime,
    onEvent: (ScheduleEvent) -> Unit
) {
    var isLeftSpinnerShow by remember {
        mutableStateOf(false)
    }
    var isRightSpinnerShow by remember {
        mutableStateOf(false)
    }

    var leftDateTime by remember(startDate) {
        mutableStateOf(
            startDate
        )
    }
    var rightDateTime by remember(endDate) {
        mutableStateOf(
            endDate
        )
    }

    val dateFormat = DateTimeFormatter.ofPattern("MM월 dd일 (E)").withLocale(Locale.KOREAN)

    val timeFormat = DateTimeFormatter.ofPattern("HH:mm a")

    LaunchedEffect(key1 = leftDateTime, key2 = rightDateTime, block = {
        onEvent(ScheduleEvent.SetStartEndDateTime(leftDateTime, rightDateTime))
    })

    LaunchedEffect(key1 = bottomSheetVisible, block = {
        if (!bottomSheetVisible) {
            isLeftSpinnerShow = false
            isRightSpinnerShow = false
        }
    })

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp, start = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier
                    .width(25.dp)
                    .height(25.dp)
                    .padding(end = 5.dp),
                imageVector = Icons.Default.WatchLater,
                contentDescription = ""
            )
            Card(modifier = Modifier.weight(1f), onClick = {
                isLeftSpinnerShow = !isLeftSpinnerShow
                if (isRightSpinnerShow) {
                    isRightSpinnerShow = false
                }

            }) {
                Column(Modifier.padding(5.dp)) {
                    Text(
                        text = leftDateTime.format(dateFormat)
                    )
                    Text(text = leftDateTime.format(timeFormat))
                }
            }
            Image(imageVector = Icons.Default.ArrowForward, contentDescription = "")

            Card(modifier = Modifier.weight(1f), onClick = {
                isRightSpinnerShow = !isRightSpinnerShow
                if (isLeftSpinnerShow) {
                    isLeftSpinnerShow = false
                }

            }) {
                Column(Modifier.padding(5.dp)) {
                    Text(
                        text = rightDateTime.format(dateFormat)
                    )
                    Text(text = rightDateTime.format(timeFormat))
                }
            }

        }
        AnimatedContent(targetState = isLeftSpinnerShow, label = "") {
            if (it) {
                WheelDateTimePicker(
                    modifier = Modifier.fillMaxSize(),
                    startDateTime = leftDateTime,
                    timeFormat = TimeFormat.AM_PM,
                    rowCount = 5,
                    size = DpSize(200.dp, 150.dp),
                    textStyle = TextStyle(fontSize = 15.sp),
                    textColor = Color.Black,
                    selectorProperties = WheelPickerDefaults.selectorProperties(
                        enabled = true,
                        shape = RoundedCornerShape(5.dp),
                        color = Color(0xffF7F2FA).copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, Color.White)
                    )
                ) {
                    leftDateTime = it
                }
            }

        }
        AnimatedContent(targetState = isRightSpinnerShow, label = "") {
            if (it) {
                WheelDateTimePicker(
                    modifier = Modifier.fillMaxSize(),
                    startDateTime = rightDateTime,
                    timeFormat = TimeFormat.AM_PM,
                    rowCount = 5,
                    size = DpSize(200.dp, 150.dp),
                    textStyle = TextStyle(fontSize = 15.sp),
                    textColor = Color.Black,
                    selectorProperties = WheelPickerDefaults.selectorProperties(
                        enabled = true,
                        shape = RoundedCornerShape(5.dp),
                        color = Color(0xffF7F2FA).copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, Color.White)
                    )
                ) {
                    rightDateTime = it
                }
            }
        }
    }
}

//@Composable
//fun AlarmContent(
//    modifier: Modifier,
//    isAlarmShow: Boolean = false,
//    onCheckedChange: (Boolean) -> Unit,
//    onAlarmTime: (LocalTime) -> Unit
//) {
//    Column {
//        Row(
//            modifier = modifier,
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Image(
//                modifier = Modifier
//                    .width(25.dp)
//                    .height(25.dp)
//                    .padding(end = 5.dp),
//                imageVector = Icons.Default.Alarm,
//                contentDescription = ""
//            )
//            Switch(checked = isAlarmShow, onCheckedChange = {
//                onCheckedChange(it)
//            })
//        }
//        AnimatedContent(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier.fillMaxWidth(),
//            targetState = isAlarmShow,
//            label = ""
//        ) {
//            if (it) {
//                WheelTimePicker(
//                    timeFormat = TimeFormat.AM_PM,
//                    rowCount = 5,
//                    size = DpSize(200.dp, 150.dp),
//                    textStyle = TextStyle(fontSize = 15.sp),
//                    textColor = Color.Black,
//                    selectorProperties = WheelPickerDefaults.selectorProperties(
//                        enabled = true,
//                        shape = RoundedCornerShape(5.dp),
//                        color = Color(0xffF7F2FA).copy(alpha = 0.2f),
//                        border = BorderStroke(1.dp, Color.White)
//                    )
//                ) {
//                    onAlarmTime(it)
//                }
//            }
//        }
//
//    }
//
//}

@Composable
fun ScheduleAddButton(
    buttonText: String, onEvent: (ScheduleEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
    ) {
        Button(modifier = Modifier
            .background(Color.Transparent)
            .imePadding(),
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = PaleRobinEggBlue, contentColor = Color.White
            ),
            onClick = { onEvent(ScheduleEvent.Save) }) {
            Text(text = buttonText)
        }
        Button(
            modifier = Modifier.imePadding(),
            onClick = { onEvent(ScheduleEvent.HideBottomSheet) },
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = VanillaIce, contentColor = Color.White
            ),
        ) {
            Text(text = "닫기")
        }
    }
}