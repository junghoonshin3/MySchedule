package kr.sjh.myschedule.ui.screen.schedule.bottomsheet

import android.accessibilityservice.AccessibilityService.SoftKeyboardController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.sjh.myschedule.ui.screen.schedule.BottomSheetUiState
import kr.sjh.myschedule.ui.screen.schedule.ScheduleEvent
import kr.sjh.myschedule.ui.screen.schedule.bottomsheet.add.ScheduleAddContent
import kr.sjh.myschedule.ui.theme.FontColorNomal
import kr.sjh.myschedule.ui.theme.SoftBlue
import java.time.LocalDateTime

@Composable
fun BottomSheetContent(
    modifier: Modifier = Modifier,
    uiState: BottomSheetUiState,
    onEvent: (ScheduleEvent) -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        ScheduleTitle(uiState.scheduleWithTask.schedule.title, onEvent)
        ScheduleAddContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(color = SoftBlue, shape = RoundedCornerShape(10.dp)),
            onEvent = onEvent,
            bottomSheetVisible = uiState.bottomSheetVisible,
            startDate = LocalDateTime.of(
                uiState.scheduleWithTask.schedule.startDate,
                uiState.scheduleWithTask.schedule.startTime
            ),
            endDate = LocalDateTime.of(
                uiState.scheduleWithTask.schedule.endDate, uiState.scheduleWithTask.schedule.endTime
            ),
            buttonText = uiState.buttonText
        )
    }
}

@Composable
private fun ScheduleTitle(title: String, onEvent: (ScheduleEvent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier.weight(0.8f),
            value = title,
            onValueChange = { onEvent(ScheduleEvent.SetTitle(it)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done, keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(onDone = {}),
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
    BottomSheetContent(modifier = Modifier.background(Color.White),
        uiState = BottomSheetUiState(),
        onEvent = {})
}

