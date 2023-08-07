package kr.sjh.myschedule.ui.screen.detail

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.commandiron.wheel_picker_compose.WheelTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat
import kr.sjh.myschedule.components.CustomToggleButton
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.ui.screen.schedule.ScheduleViewModel
import kr.sjh.myschedule.utill.collectAsMutableState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Composable
fun ScheduleDetailScreen(
    onBackClick: () -> Unit,
    viewModel: ScheduleDetailViewModel = hiltViewModel()
) {

    val scrollState = rememberScrollState()

    val schedule by viewModel.schedule.collectAsState()

    val (title, setTitle) = viewModel.title.collectAsMutableState()

    val (memo, setMemo) = viewModel.memo.collectAsMutableState()

    val (isAlarm, setAlarm) = viewModel.isAlarm.collectAsMutableState()

    val (alarmTime, setAlarmTime) = viewModel.alarmTime.collectAsMutableState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp, top = 30.dp)
            .verticalScroll(scrollState)
    ) {

        Text(text = "제목", fontSize = TextUnit(20f, TextUnitType.Sp))

        Spacer(modifier = Modifier.padding(2.dp))

        OutlinedTextField(
            value = title,
            onValueChange = {
                setTitle(it)
            },
            label = { Text(text = "Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(5.dp))

        Text(text = "내용", fontSize = TextUnit(20f, TextUnitType.Sp))

        Spacer(modifier = Modifier.padding(5.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            value = memo,
            onValueChange = {
                setMemo(it)
            },
            label = { Text(text = "memo") }
        )

        Spacer(modifier = Modifier.padding(5.dp))

        Text(text = "알람 ON/OFF", fontSize = TextUnit(20f, TextUnitType.Sp))

        CustomToggleButton(selected = isAlarm, modifier = Modifier.padding(top = 10.dp)) {
            setAlarm(it)
        }

        Spacer(modifier = Modifier.padding(5.dp))

        if (isAlarm) {
            WheelTimePicker(
                startTime = alarmTime.toLocalTime(),
                timeFormat = TimeFormat.AM_PM
            ) { snappedTime ->
                setAlarmTime(LocalDateTime.of(LocalDate.now(), snappedTime).apply {
                    truncatedTo(ChronoUnit.SECONDS)
                })
            }
        }
        Box(
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    viewModel.onSave()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp)
            ) {
                Text(text = "SAVE")
            }
        }
    }
}