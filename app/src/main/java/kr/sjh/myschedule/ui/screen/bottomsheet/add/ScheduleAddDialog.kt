package kr.sjh.myschedule.ui.screen.bottomsheet.add

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import kr.sjh.myschedule.ui.screen.bottomsheet.DateSelection
import kr.sjh.myschedule.ui.theme.PaleRobinEggBlue
import kr.sjh.myschedule.ui.theme.VanillaIce
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle.*
import java.util.Locale

@Composable
fun ScheduleAddDialog(
    modifier: Modifier,
    selectedDate: LocalDate,
    onSave: (DateSelection) -> Unit,
    onCancel: () -> Unit
) {

    var dateSelection by remember {
        mutableStateOf(DateSelection())
    }

    var isAlarm by remember {
        mutableStateOf(false)
    }

    LazyColumn(
        modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
    ) {
        item {
            ScheduleAddTitle()
        }
        item {
            DateTimeContent(selectedDate) { start, end ->
                dateSelection.startDate = start
                dateSelection.endDate = end
            }
        }

        item {
            AlarmContent(modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp, start = 10.dp),
                isAlarmShow = isAlarm,
                onCheckedChange = {
                    isAlarm = it
                },
                onSelectedDateTime = {
                    Log.i("sjh", "${it}")
                })
        }

        item {
            ScheduleAddButton(onSave = { onSave.invoke(dateSelection) }, onCancel = onCancel)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DateTimeContent(
    selectedDate: LocalDate, onDateSelection: (LocalDateTime, LocalDateTime) -> Unit
) {
    var isLeftSpinnerShow by remember {
        mutableStateOf(false)
    }
    var isRightSpinnerShow by remember {
        mutableStateOf(false)
    }

    var leftDateTime by remember {
        mutableStateOf(LocalDateTime.of(selectedDate, LocalTime.now()))
    }

    var rightDateTime by remember {
        mutableStateOf(
            LocalDateTime.of(
                selectedDate,
                LocalTime.now().withSecond(0).withNano(0).plusHours(1)
            )
        )
    }

    val dateFormat = DateTimeFormatter.ofPattern("MM월 dd일 (E)").withLocale(Locale.KOREAN)

    val timeFormat = DateTimeFormatter.ofPattern("HH:mm a")


    LaunchedEffect(key1 = leftDateTime, key2 = rightDateTime, block = {
        onDateSelection(leftDateTime, rightDateTime)
    })

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

@Composable
fun AlarmContent(
    modifier: Modifier,
    isAlarmShow: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    onSelectedDateTime: (LocalTime) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .width(25.dp)
                .height(25.dp)
                .padding(end = 5.dp),
            imageVector = Icons.Default.Alarm,
            contentDescription = ""
        )
        Switch(checked = isAlarmShow, onCheckedChange = {
            onCheckedChange(it)
        })
    }
    AnimatedContent(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
        targetState = isAlarmShow,
        label = ""
    ) {
        if (it) {
            WheelTimePicker(
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
                onSelectedDateTime(it)
            }
        }
    }
}

@Composable
fun ScheduleAddTitle() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = "일정 등록",
            color = Color.White,
            fontSize = 20.sp
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.White)
        )
    }
}

@Composable
fun ScheduleAddButton(
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
    ) {
        Button(
            modifier = Modifier.background(Color.Transparent),
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = PaleRobinEggBlue, contentColor = Color.White
            ),
            onClick = onSave
        ) {
            Text(text = "저장")
        }
        Button(
            onClick = { onCancel() },
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = VanillaIce, contentColor = Color.White
            ),
        ) {
            Text(text = "닫기")
        }
    }
}