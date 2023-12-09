package kr.sjh.myschedule.ui.screen.today.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.sjh.myschedule.domain.model.Schedule
import kr.sjh.myschedule.ui.screen.today.bottomsheet.add.ScheduleAddContent
import kr.sjh.myschedule.ui.screen.today.generateRandomColor
import kr.sjh.myschedule.ui.theme.FontColorNomal
import kr.sjh.myschedule.ui.theme.SoftBlue
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun BottomSheetContent(
    modifier: Modifier = Modifier,
    title: String,
    startDateTime: LocalDateTime,
    endDateTime: LocalDateTime,
    onTitleChange: (String) -> Unit,
    onSave: () -> Unit,
    onDateRange: (LocalDateTime, LocalDateTime) -> Unit,
    onAlarmTime: (LocalTime) -> Unit,
    onAlarm: (Boolean) -> Unit,
    onCancel: () -> Unit,
    onDone: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        ScheduleTitle(title, onTitleChange, onDone)
        ScheduleAddContent(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(color = SoftBlue, shape = RoundedCornerShape(10.dp)),
            onSave = onSave,
            startDate = startDateTime,
            endDate = endDateTime,
            onDateRange = onDateRange,
            onAlarmTime = onAlarmTime,
            onCancel = onCancel,
            onAlarm = onAlarm
        )
    }
}

@Composable
private fun ScheduleTitle(title: String, onTitleChange: (String) -> Unit, onDone: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier.weight(0.8f),
            value = title,
            onValueChange = onTitleChange,
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

@Preview
@Composable
private fun BottomSheetContentPreview() {
    BottomSheetContent(
        modifier = Modifier.background(Color.White),
        title = "",
        startDateTime = LocalDateTime.now(),
        endDateTime = LocalDateTime.now(),
        onTitleChange = {
        },
        onSave = { },
        onDateRange = { l, r ->

        },
        onAlarmTime = {

        },
        onAlarm = {

        },
        onCancel = { /*TODO*/ }) {
    }
}

