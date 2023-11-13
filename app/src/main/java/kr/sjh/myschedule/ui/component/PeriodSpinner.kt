package kr.sjh.myschedule.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.commandiron.wheel_picker_compose.WheelDateTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import kr.sjh.myschedule.ui.theme.PaleRobinEggBlue
import kr.sjh.myschedule.ui.theme.SoftBlue
import kr.sjh.myschedule.ui.theme.VanillaIce
import java.time.LocalDateTime

@Composable
fun PeriodSpinner(
    startDateTime: LocalDateTime,
    onSave: (LocalDateTime, LocalDateTime) -> Unit,
    onCancel: () -> Unit
) {
    var snappedDateTime by remember {
        mutableStateOf(startDateTime)
    }

    Column(modifier = Modifier.background(color = SoftBlue, shape = RoundedCornerShape(10.dp))) {
        Text(
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp),
            text = "기간 설정",
            color = Color.White
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.White)
        )
        WheelDateTimePicker(
            modifier = Modifier.fillMaxWidth(),
            startDateTime = startDateTime,
            timeFormat = TimeFormat.AM_PM,
            rowCount = 5,
            size = DpSize(300.dp, 250.dp),
            textStyle = TextStyle(fontSize = 18.sp),
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
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        ) {
            Button(modifier = Modifier.background(Color.Transparent),
                elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = PaleRobinEggBlue, contentColor = Color.White
                ),
                onClick = {
                    onSave(startDateTime, snappedDateTime)
                }) {
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
}