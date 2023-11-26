package kr.sjh.myschedule.ui.screen.today.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kr.sjh.myschedule.ui.MainViewModel
import kr.sjh.myschedule.ui.screen.today.bottomsheet.add.ScheduleAddContent
import kr.sjh.myschedule.ui.theme.FontColorNomal
import kr.sjh.myschedule.ui.theme.SoftBlue
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetContent(
    selectedDate: LocalDate,
    mainViewModel: MainViewModel,
    viewModel: BottomSheetViewModel,
    sheetState: ModalBottomSheetState,
    focusManager: FocusManager
) {

    val schedule by mainViewModel.schedule.collectAsState()

    var textFieldText by remember { mutableStateOf("") }

    var startDate by remember(selectedDate) {
        mutableStateOf(
            LocalDateTime.of(
                selectedDate, LocalTime.now().withSecond(0).withNano(0)
            )
        )
    }

    var endDate by remember(selectedDate) {
        mutableStateOf(
            LocalDateTime.of(
                selectedDate, LocalTime.now().plusHours(1).withSecond(0).withNano(0)
            )
        )
    }

    var isAlarm by remember {
        mutableStateOf(false)
    }

    var alarmTime by remember {
        mutableStateOf(
            LocalTime.now()
        )
    }

    val coroutineScope = rememberCoroutineScope()

    BottomSheetContent(modifier = Modifier
        .wrapContentSize()
        .padding(10.dp),
        title = textFieldText,
        startDate = startDate,
        endDate = endDate,
        onChangeTitle = {
            textFieldText = it
        },
        onSave = {
            viewModel.onSave(textFieldText, startDate, endDate, isAlarm, alarmTime)
            coroutineScope.launch {
                sheetState.hide()
            }
        },
        onDateSelection = { start, end ->
            startDate = start
            endDate = end
        },
        onAlarmTime = {
            alarmTime = it
        },
        onAlarm = {
            isAlarm = it
        },
        onCancel = {
            coroutineScope.launch {
                sheetState.hide()
            }
        },
        onDone = {
            focusManager.clearFocus()
        })
}

@Composable
private fun BottomSheetContent(
    modifier: Modifier = Modifier,
    startDate: LocalDateTime,
    endDate: LocalDateTime,
    title: String,
    onChangeTitle: (String) -> Unit,
    onSave: () -> Unit,
    onDateSelection: (LocalDateTime, LocalDateTime) -> Unit,
    onAlarmTime: (LocalTime) -> Unit,
    onAlarm: (Boolean) -> Unit,
    onCancel: () -> Unit,
    onDone: () -> Unit
) {


    Column(
        modifier = modifier
    ) {
        ScheduleTitle(title, onChangeTitle, onDone)
        ScheduleAddContent(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(color = SoftBlue, shape = RoundedCornerShape(10.dp)),
            onSave = onSave,
            startDate = startDate,
            endDate = endDate,
            onDateSelection = onDateSelection,
            onAlarmTime = onAlarmTime,
            onCancel = onCancel,
            onAlarm = onAlarm

        )
    }
}

@Composable
private fun ScheduleTitle(title: String, changeTitle: (String) -> Unit, onDone: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier.weight(0.8f),
            value = title,
            onValueChange = changeTitle,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done, keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(onDone = {
                onDone()
            }),
            label = {
                Text(
                    text = "할일을 적어주세요", color = FontColorNomal
                )
            },
        )
    }
}

