package kr.sjh.myschedule.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.data.local.entity.ScheduleWithDetail
import kr.sjh.myschedule.ui.theme.MemoColor
import kr.sjh.myschedule.ui.theme.TextColor

@Composable
fun ScheduleList(list: List<ScheduleEntity> = listOf(), onScheduleClick: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        items(list) {
            ScheduleItem(
                it, onScheduleClick
            )
        }
    }
}

@Composable
fun ScheduleItem(schedule: ScheduleEntity, onScheduleClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(top = 10.dp, bottom = 10.dp)
            .clickable {
                onScheduleClick(schedule.id)
            }
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xffFEF7FF))
        ) {
            Text(
                text = schedule.title,
                fontSize = 25.sp,
                color = TextColor,
                modifier = Modifier.padding(5.dp)
            )

            Text(
                text = schedule.memo,
                fontSize = 15.sp,
                color = MemoColor,
                modifier = Modifier.padding(5.dp)
            )

        }


    }
}