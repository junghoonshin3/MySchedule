//package kr.sjh.myschedule.ui.screen.schedule
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.TextUnit
//import androidx.compose.ui.unit.TextUnitType
//import kr.sjh.myschedule.components.MyWeekCalendar
//import kr.sjh.myschedule.components.ScheduleList
//import kr.sjh.myschedule.data.local.entity.ScheduleEntity
//import kr.sjh.myschedule.ui.theme.DarkCobaltBlue
//import kr.sjh.myschedule.ui.theme.FontColorNomal
//import kr.sjh.myschedule.ui.theme.SoftBlue
//import java.time.LocalDate
//import java.time.Month
//
//
//@Composable
//fun ScheduleScreen(
//    onKeepOnScreenCondition: () -> Unit,
//    allYearSchedules: List<ScheduleEntity>,
//    selectedDate: LocalDate,
//    onScheduleClick: (ScheduleEntity?) -> Unit,
//    onSelectedDate: (LocalDate) -> Unit,
//    onDeleteSwipe: (ScheduleEntity) -> Unit,
//    onCompleteSwipe: (ScheduleEntity) -> Unit
//) {
//    LaunchedEffect(key1 = Unit, block = {
//        onKeepOnScreenCondition()
//    })
//
//    Column(
//        modifier = Modifier.background(DarkCobaltBlue)
//    ) {
//        val yearSchedules = allYearSchedules.groupBy { it.regDt.month }
//        val scheduleList = allYearSchedules.groupBy { it.regDt }[selectedDate] ?: emptyList()
//        ScheduleTopBar(
//            yearSchedules, selectedDate, onSelectedDate
//        )
//        ScheduleContent(
//            scheduleList, onScheduleClick, onDeleteSwipe, onCompleteSwipe
//        )
//
//    }
//}
//
//@Composable
//fun ScheduleContent(
//    scheduleList: List<ScheduleEntity>,
//    onScheduleClick: (ScheduleEntity) -> Unit,
//    onDeleteSwipe: (ScheduleEntity) -> Unit,
//    onCompleteSwipe: (ScheduleEntity) -> Unit,
//) {
//    if (scheduleList.isNotEmpty()) {
//        ScheduleList(
//            scheduleList,
//            onScheduleClick = onScheduleClick,
//            onDeleteSwipe = onDeleteSwipe,
//            onCompleteSwipe = onCompleteSwipe
//        )
//    } else {
//        ScheduleEmptyContent()
//    }
//}
//
//@Composable
//fun ScheduleTopBar(
//    scheduleMap: Map<Month, List<ScheduleEntity>>,
//    selectedDate: LocalDate,
//    onSelectedDate: (LocalDate) -> Unit
//) {
//    MyWeekCalendar(
//        Modifier.background(SoftBlue), scheduleMap, selectedDate, onSelectedDate
//    )
//}
//
//@Composable
//fun ScheduleEmptyContent() {
//    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        Text(
//            text = "등록된 스케쥴이 없습니다.",
//            fontWeight = FontWeight.Bold,
//            fontSize = TextUnit(16f, TextUnitType.Sp),
//            color = FontColorNomal
//        )
//    }
//}
