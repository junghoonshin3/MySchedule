package kr.sjh.myschedule.ui.screen.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.commandiron.wheel_picker_compose.WheelTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat
import kr.sjh.myschedule.AlarmItem
import kr.sjh.myschedule.MyAlarmScheduler
import kr.sjh.myschedule.components.BackPressHandler
import kr.sjh.myschedule.components.CustomToggleButton
import kr.sjh.myschedule.utill.clickableWithoutRipple
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

    val context = LocalContext.current

    val alarmScheduler = MyAlarmScheduler(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
            .verticalScroll(scrollState)
            .imePadding()
    ) {
        // 하드웨어 back 이벤트 처리
        BackPressHandler(onBackPressed = onBackClick)

        DetailTopBar(onBackClick)

        Text(text = "제목", fontSize = TextUnit(20f, TextUnitType.Sp))

        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        Spacer(modifier = Modifier.padding(2.dp))

        OutlinedTextField(
            value = title,
            maxLines = 2,
            onValueChange = {
                setTitle(it)
            },
            label = { Text(text = "Title") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, autoCorrect = false),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
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
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, autoCorrect = false),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )

        Spacer(modifier = Modifier.padding(10.dp))

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
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    val alarmItem = AlarmItem(message = title.text, time = alarmTime)

                    if (isAlarm) {
                        alarmScheduler.schedule(alarmItem)
                    } else {
                        alarmScheduler.cancel(alarmItem)
                    }

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

@Composable
fun DetailTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        backgroundColor = Color.Transparent,
        modifier = Modifier.fillMaxWidth(),
        elevation = 0.dp,
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .fillMaxHeight()
                .width(50.dp)
                .clickableWithoutRipple {
                    onBackClick()
                }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        }
    }
}