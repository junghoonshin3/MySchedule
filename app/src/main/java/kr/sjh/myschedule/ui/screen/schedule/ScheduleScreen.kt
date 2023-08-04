package kr.sjh.myschedule.ui.screen.schedule

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kr.sjh.myschedule.components.MyWeekCalendar
import kr.sjh.myschedule.components.ScheduleList
import java.time.LocalDate

@Composable
fun ScheduleScreen(viewModel: ScheduleViewModel, onScheduleClick: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffF7F2FA)),
    ) {
        var selectedDate by remember {
            mutableStateOf(LocalDate.now())
        }

        val scheduleList by viewModel.scheduleList.collectAsState()

        MyWeekCalendar {
            Log.i("sjh", "$it")
            selectedDate = it
        }

        ScheduleList(scheduleList) {
            onScheduleClick(it)
        }
    }

}
