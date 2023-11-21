package kr.sjh.myschedule.ui.screen.today.bottomsheet

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kr.sjh.myschedule.ui.DateSelection
import kr.sjh.myschedule.ui.MainViewModel
import kr.sjh.myschedule.ui.screen.today.bottomsheet.add.ScheduleAddContent
import kr.sjh.myschedule.ui.theme.FontColorNomal
import kr.sjh.myschedule.ui.theme.SoftBlue
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun BottomSheetContent(
    selectedDate: LocalDate,
    title: String,
    onChangeTitle: (String) -> Unit,
    onSave: () -> Unit,
    onDateSelection: (DateSelection) -> Unit,
    onAlarmTime: (LocalTime) -> Unit,
    onCancel: () -> Unit
) {

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(10.dp)
    ) {
        ScheduleTitle(title, onChangeTitle)
        ScheduleAddContent(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(color = SoftBlue, shape = RoundedCornerShape(10.dp)),
            onSave = onSave,
            selectedDate = selectedDate,
            onDateSelection = onDateSelection,
            onAlarmTime = onAlarmTime,
            onCancel = onCancel
        )
    }
}

@Composable
private fun ScheduleTitle(title: String, changeTitle: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier.weight(0.8f),
            value = title,
            onValueChange = changeTitle,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            label = {
                Text(
                    text = "할일을 적어주세요", color = FontColorNomal
                )
            },
        )
        Spacer(modifier = Modifier.width(5.dp))
        Button(elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = SoftBlue, contentColor = SoftBlue
            ),
            modifier = Modifier
                .weight(0.2f)
                .aspectRatio(1f)
                .padding(5.dp),
            onClick = {

            }) {
            Image(
                imageVector = Icons.Default.Send,
                contentDescription = null,
                modifier = Modifier.wrapContentSize()
            )
        }
    }
}

