package kr.sjh.myschedule.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TimerOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import java.time.format.DateTimeFormatter

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

    var show by remember { mutableStateOf(true) }
    val dismissState = rememberDismissState(confirmStateChange = {
        when (it) {
            DismissValue.DismissedToStart -> {
                //삭제
                show = false
                onDeleteSwipe(schedule)

            }

            DismissValue.DismissedToEnd -> {
                //완료
                show = false
                onCompleteSwipe(schedule)

            }

            DismissValue.Default -> return@rememberDismissState false
        }
        true
    })
    AnimatedVisibility(
        show, exit = fadeOut(spring())
    ) {
        SwipeToDismiss(state = dismissState, background = {
            DismissBackground(dismissState = dismissState)
        }, dismissThresholds = { FractionalThreshold(0.7f) }, dismissContent = {
            ScheduleCard(schedule, onScheduleClick)
        })
    }

    LaunchedEffect(show) {
        if (!show) {
            delay(800)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScheduleCard(
    schedule: ScheduleEntity, onScheduleClick: (ScheduleEntity) -> Unit
) {
//    ListItem(modifier = Modifier
//        .padding(bottom = 10.dp, top = 10.dp)
//        .fillMaxSize()
//        .background(Color.White)
//
//        .clickable { onScheduleClick(schedule) }, icon = {
//        Icon(
//            imageVector = if (schedule.isAlarm) {
//                Icons.Default.Timer
//            } else {
//                Icons.Default.TimerOff
//            }, contentDescription = "Icon"
//        )
//    }, text = {
//        Text(
//            overflow = TextOverflow.Ellipsis,
//            maxLines = 1,
//            fontSize = TextUnit(17f, TextUnitType.Sp),
//            text = schedule.memo
//        )
//    }, secondaryText = {
//        Text(
//            overflow = TextOverflow.Ellipsis,
//            maxLines = 1,
//            fontSize = TextUnit(15f, TextUnitType.Sp),
//            text = schedule.regDt.format(
//                DateTimeFormatter.ofPattern("yyyy년MM월dd일")
//            )
//        )
//    }, overlineText = {
//        Text(
//            overflow = TextOverflow.Ellipsis,
//            maxLines = 1,
//            fontSize = TextUnit(20f, TextUnitType.Sp),
//            text = schedule.title
//        )
//    })
}

@ExperimentalMaterialApi
@Composable
fun DismissBackground(dismissState: DismissState) {
    val color = when (dismissState.dismissDirection) {
        DismissDirection.StartToEnd -> Color(0xFFFF1744)
        DismissDirection.EndToStart -> Color(0xFF1DE9B6)
        else -> Color.Transparent
    }
    val direction = dismissState.dismissDirection

    Row(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp)
            .fillMaxSize()
            .background(color),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (direction == DismissDirection.StartToEnd) Icon(
            modifier = Modifier.padding(start = 10.dp),
            imageVector = Icons.Default.Delete,
            contentDescription = "delete"
        )
        Spacer(modifier = Modifier)
        if (direction == DismissDirection.EndToStart) Icon(
            modifier = Modifier.padding(end = 10.dp),
            // make sure add baseline_archive_24 resource to drawable folder
            imageVector = Icons.Default.Archive, contentDescription = "Archive"
        )
    }

}