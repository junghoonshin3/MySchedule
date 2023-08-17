package kr.sjh.myschedule.ui.screen.schedule

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import kr.sjh.myschedule.components.MyWeekCalendar
import kr.sjh.myschedule.components.ScheduleList
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import java.time.LocalDate


//stateless
@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = hiltViewModel(),
    selectedDate: LocalDate,
    onScheduleClick: (ScheduleEntity?) -> Unit,
    onSelectedDate: (LocalDate) -> Unit,
    onDeleteSwipe: (ScheduleEntity) -> Unit,
    onCompleteSwipe: (ScheduleEntity) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    ScheduleScreen(
        uiState,
        selectedDate,
        onScheduleClick,
        onSelectedDate,
        onDeleteSwipe,
        onCompleteSwipe,
    )
}

@Composable
private fun ScheduleScreen(
    uiState: ScheduleUiState,
    selectedDate: LocalDate,
    onScheduleClick: (ScheduleEntity?) -> Unit,
    onSelectedDate: (LocalDate) -> Unit,
    onDeleteSwipe: (ScheduleEntity) -> Unit,
    onCompleteSwipe: (ScheduleEntity) -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = Color(0xffECE6F0),
                onClick = {
                    onScheduleClick(null)
                }) {
                Icon(imageVector = Icons.Rounded.Edit, contentDescription = "ADD")
            }
        }
    ) {
        Column(modifier.background(Color(0xffF7F2FA))) {
            ScheduleTopBar(selectedDate, onSelectedDate)
            if (uiState.scheduleList.isNotEmpty()) {
                ScheduleContent(uiState, onScheduleClick, onDeleteSwipe, onCompleteSwipe)
            } else {
                ScheduleEmptyContent()
            }
        }

    }
}

@Composable
fun ScheduleContent(
    uiState: ScheduleUiState,
    onScheduleClick: (ScheduleEntity) -> Unit,
    onDeleteSwipe: (ScheduleEntity) -> Unit,
    onCompleteSwipe: (ScheduleEntity) -> Unit,
) {

    ScheduleList(
        uiState.scheduleList,
        onScheduleClick = onScheduleClick,
        onDeleteSwipe = onDeleteSwipe,
        onCompleteSwipe = onCompleteSwipe
    )
}

@Composable
fun ScheduleTopBar(
    selectedDate: LocalDate,
    onSelectedDate: (LocalDate) -> Unit
) {
    MyWeekCalendar(selectedDate) { date ->
        onSelectedDate(date)
    }
}

@Composable
fun ScheduleEmptyContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "등록된 스케쥴이 없습니다.")
    }
}