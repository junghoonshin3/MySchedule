package kr.sjh.myschedule.ui.screen.schedule

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kr.sjh.myschedule.ScheduleNavHost
import kr.sjh.myschedule.components.MyWeekCalendar
import kr.sjh.myschedule.components.ScheduleList
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.ui.theme.Screen
import kr.sjh.myschedule.utill.Common
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun ScheduleScreen(
    navController: NavController,
    scheduleViewModel: ScheduleViewModel,
    onDeleteSwipe: (ScheduleEntity) -> Unit,
    onCompleteSwipe: (ScheduleEntity) -> Unit

) {

    var isFabShow by remember {
        mutableStateOf(true)
    }

    var selectedDate by remember {
        mutableStateOf(LocalDate.now())
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            AnimatedVisibility(visible = isFabShow) {
                FloatingActionButton(
                    backgroundColor = Color(0xffECE6F0),
                    onClick = {
                        navController.navigate(
                            Screen.Detail.createRoute(
                                Common.ADD_PAGE,
                                selectedDate
                            )
                        )
                        isFabShow = false
                    }) {
                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = "write")
                }
            }
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xffF7F2FA)),
        ) {
            val scheduleList = scheduleViewModel.scheduleList.collectAsState()

            MyWeekCalendar {
                selectedDate = it
                scheduleViewModel.getAllSchedules(it)
            }

            if (scheduleList.value.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "등록된 스케쥴이 없습니다.")
                }
                return@Column
            }

            ScheduleList(scheduleList.value,
                onScheduleClick = {
                    Log.i("sjh", "selectedDate >>>>>>>>>>>>>>>>>>>>>>> $selectedDate")
                    navController.navigate(Screen.Detail.createRoute(it, selectedDate))
                    isFabShow = false
                }, onDeleteSwipe = {
                    onDeleteSwipe(it)
//                    scheduleViewModel.deleteSchedule(it)
                }, onCompleteSwipe = {
                    onCompleteSwipe(it)
//                    scheduleViewModel.updateSchedule(it)
                })
        }
    }
}

