package kr.sjh.myschedule.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.commandiron.wheel_picker_compose.WheelDateTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import java.time.LocalDateTime

@Composable
fun PeriodSpinner(
    startDateTime: LocalDateTime,
    onSave: (LocalDateTime) -> Unit,
    onCancel: () -> Unit
) {
    var snappedDateTime by remember {
        mutableStateOf(startDateTime)
    }

    Column {
        WheelDateTimePicker(
            modifier = Modifier.fillMaxWidth(),
            startDateTime = startDateTime,
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
            snappedDateTime = it
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally)
        ) {
            Button(onClick = { onSave(snappedDateTime) }) {
                Text(text = "저장")
            }
            Button(onClick = { onCancel() }) {
                Text(text = "닫기")
            }
        }
    }
}