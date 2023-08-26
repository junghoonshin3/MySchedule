package kr.sjh.myschedule.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.ui.theme.MemoColor
import kr.sjh.myschedule.ui.theme.TextColor
import kr.sjh.myschedule.utill.clickableSingle

@Composable
fun ScheduleList(
    list: List<ScheduleEntity> = listOf(),
    onScheduleClick: (ScheduleEntity) -> Unit,
    onDeleteSwipe: (ScheduleEntity) -> Unit,
    onCompleteSwipe: (ScheduleEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        items(list, key = {
            it.id
        }) {
            ScheduleItem(
                it, onScheduleClick, onDeleteSwipe, onCompleteSwipe
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScheduleItem(
    schedule: ScheduleEntity,
    onScheduleClick: (ScheduleEntity) -> Unit,
    onDeleteSwipe: (ScheduleEntity) -> Unit,
    onCompleteSwipe: (ScheduleEntity) -> Unit
) {

    val dismissState = rememberDismissState(
        confirmStateChange = { disMissValue ->
            when (disMissValue) {
                DismissValue.Default -> { // dismissThresholds 만족 안한 상태
                    CoroutineScope(Dispatchers.Default).launch {
                        delay(200)
                    }
                    false
                }

                DismissValue.DismissedToEnd -> { // -> 방향 스와이프 (완료)
                    CoroutineScope(Dispatchers.Default).launch {
                        delay(200)
                        onCompleteSwipe(schedule)
                    }

                    true
                }

                DismissValue.DismissedToStart -> { // <- 방향 스와이프 (삭제)
                    CoroutineScope(Dispatchers.Default).launch {
                        delay(200)
                        onDeleteSwipe(schedule)
                    }
                    true
                }
            }
        })


    SwipeToDismiss(state = dismissState,
        dismissThresholds = { FractionalThreshold(0.6f) },
        background = {
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> backgroundColor.copy(alpha = 0.5f) // dismissThresholds 만족 안한 상태
                    DismissValue.DismissedToEnd -> Color.Green.copy(alpha = 0.4f) // -> 방향 스와이프 (완료)
                    DismissValue.DismissedToStart -> Color.Red.copy(alpha = 0.5f) // <- 방향 스와이프 (삭제)
                }, label = ""
            )
            val icon = when (dismissState.targetValue) {
                DismissValue.Default -> Icons.Default.Circle
                DismissValue.DismissedToEnd -> Icons.Default.Done
                DismissValue.DismissedToStart -> Icons.Default.Delete
            }

            val scale by animateFloatAsState(
                when (dismissState.targetValue == DismissValue.Default) {
                    true -> 0.8f
                    else -> 1.5f
                }, label = ""
            )

            val alignment = when (direction) {
                DismissDirection.EndToStart -> Alignment.CenterEnd
                DismissDirection.StartToEnd -> Alignment.CenterStart
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 30.dp),
                contentAlignment = alignment
            ) {
                Icon(
                    modifier = Modifier.scale(scale),
                    contentDescription = null,
                    imageVector = icon
                )
            }
        },
        dismissContent = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 100.dp)
                    .background(Color.Transparent)
                    .padding(top = 12.dp, bottom = 12.dp)
                    .clickableSingle {
                        onScheduleClick(schedule)
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
                        modifier = Modifier.padding(5.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = schedule.memo,
                        fontSize = 15.sp,
                        color = MemoColor,
                        modifier = Modifier.padding(5.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

        }
    )

}