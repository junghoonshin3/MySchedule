package kr.sjh.myschedule.ui.screen.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kr.sjh.myschedule.components.MyWeekCalendar
import kr.sjh.myschedule.components.ScheduleList
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

        if (scheduleList.value.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "등록된 스케쥴이 없습니다.")
            }
            return@Column
        }

        ScheduleList(scheduleList.value, onScheduleClick = {
            onScheduleClick(it)
        }, onDeleteSwipe = {
            viewModel.deleteSchedule(it)
        }, onCompleteSwipe = {
            viewModel.updateSchedule(it)
        })
    }

}
