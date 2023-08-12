package kr.sjh.myschedule.ui.screen.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.commandiron.wheel_picker_compose.WheelDateTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import kr.sjh.myschedule.components.CustomToggleButton
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.utill.clickableWithoutRipple
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ScheduleDetailScreen(
    onBackClick: () -> Unit,
    onSave: (ScheduleEntity) -> Unit,
    viewModel: ScheduleDetailViewModel = hiltViewModel(),
) {

    val scrollState = rememberScrollState()


    val title by viewModel._title.collectAsState()

    val memo by viewModel._memo.collectAsState()

    val isAlarm by viewModel._isAlarm.collectAsState()

    val alarmTime by viewModel._alarmTime.collectAsState()

    DetailContent(
        scrollState,
        onBackClick,
        viewModel,
        title,
        memo,
        isAlarm,
        alarmTime,
        onSave
    )
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

@Composable
fun DetailContent(
    scrollState: ScrollState,
    onBackClick: () -> Unit,
    viewModel: ScheduleDetailViewModel,
    title: TextFieldValue,
    memo: String,
    isAlarm: Boolean,
    alarmTime: LocalDateTime,
    onSave: (ScheduleEntity) -> Unit
) {

    val focusRequester = remember { FocusRequester() }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffF7F2FA))
            .padding(10.dp)
            .verticalScroll(scrollState)
    ) {
        Column(Modifier.fillMaxHeight()) {
            DetailTopBar(onBackClick)
            OutlinedTextField(
                value = title,
                maxLines = 2,
                onValueChange = {
                    viewModel._title.value = it
                },
                label = { Text(text = "Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, autoCorrect = false),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                value = memo,
                onValueChange = {
                    viewModel._memo.value = it
                },
                label = { Text(text = "memo") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, autoCorrect = false),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "알람 ON/OFF", fontSize = TextUnit(20f, TextUnitType.Sp))
            CustomToggleButton(selected = isAlarm, modifier = Modifier.padding(top = 10.dp)) {
                viewModel._isAlarm.value = it
            }
            if (isAlarm) {
                WheelDateTimePicker(
                    modifier = Modifier.fillMaxWidth(),
                    startDateTime = alarmTime,
                    timeFormat = TimeFormat.AM_PM,
                    size = DpSize(300.dp, 200.dp),
                    rowCount = 3,
                    textColor = Color.Black,
                    textStyle = TextStyle(fontSize = 18.sp),
                    selectorProperties = WheelPickerDefaults.selectorProperties(
                        enabled = true,
                        shape = RoundedCornerShape(0.dp),
                        color = Color(0xffF7F2FA).copy(alpha = 0.2f),
                        border = BorderStroke(2.dp, Color.Red)
                    )
                ) {
                    viewModel._alarmTime.value = it
                }
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )
        Box(
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    viewModel.onSaveSchedule {
                        onSave(it)
                        onBackClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp)
            ) {
                Text(text = "저장")
            }
        }
    }
}