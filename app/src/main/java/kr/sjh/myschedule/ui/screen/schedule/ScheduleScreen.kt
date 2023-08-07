package kr.sjh.myschedule.ui.screen.schedule

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kr.sjh.myschedule.components.MyWeekCalendar
import kr.sjh.myschedule.components.ScheduleList
import kr.sjh.myschedule.ui.theme.Screen
import kr.sjh.myschedule.utill.collectAsMutableState
import java.time.LocalDate

@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel,
    onScheduleClick: (Long) -> Unit,
    onDateClick: (LocalDate) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffF7F2FA)),
    ) {
        val scheduleList = viewModel.scheduleList.collectAsState()

        MyWeekCalendar {
            onDateClick(it)
        }

        ScheduleList(scheduleList.value, onScheduleClick = {
            onScheduleClick(it)
        }, onDeleteSwipe = {
            viewModel.deleteSchedule(it)
        })
    }

}
