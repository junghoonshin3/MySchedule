package kr.sjh.myschedule.ui.screen.detail

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
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

    val (title, setTitle) = viewModel.title.collectAsMutableState()

    val (memo, setMemo) = viewModel.memo.collectAsMutableState()

    val (isAlarm, setAlarm) = viewModel.isAlarm.collectAsMutableState()

    val (alarmTime, setAlarmTime) = viewModel.alarmTime.collectAsMutableState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp, top = 30.dp)
            .verticalScroll(scrollState)
            .imePadding()
    ) {

        BackHandler {
            onBackClick()
        }

        Text(text = "제목", fontSize = TextUnit(20f, TextUnitType.Sp))

        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        Spacer(modifier = Modifier.padding(2.dp))

        OutlinedTextField(
            value = title,
            onValueChange = {
                setTitle(it)
            },
            label = { Text(text = "Title") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
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
            label = { Text(text = "memo") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
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
                    onBackClick()
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