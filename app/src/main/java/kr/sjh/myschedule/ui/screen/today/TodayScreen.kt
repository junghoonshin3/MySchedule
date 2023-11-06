package kr.sjh.myschedule.ui.screen.today

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.sjh.myschedule.ui.theme.SoftBlue
import kr.sjh.myschedule.utill.clickableSingle
import java.time.LocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodayScreen(onKeepOnScreenCondition: () -> Unit, onAdd: () -> Unit) {

    LaunchedEffect(key1 = Unit, block = {
        onKeepOnScreenCondition()
    })

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        stickyHeader {
            TodayTopBar("오늘의 스케쥴")
        }
        todayContent(
            (1..5).map { it.toString() },
            onAdd
        )
    }
}

@Composable
fun TodayTopBar(title: String) {
    TopAppBar(
        backgroundColor = SoftBlue,
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth(),
        contentPadding = PaddingValues(10.dp)
    ) {
        Text(text = title, color = Color.White, fontSize = 20.sp)


    }
}


@Composable
fun TodayItem(modifier: Modifier = Modifier, content: String, color: Color = Color.Transparent) {
    Card(modifier = modifier, backgroundColor = color) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(5.dp)
                    .background(SoftBlue)
            )
            Text(
                color = Color.White,
                text = content,
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .fillMaxHeight()
                    .padding(start = 10.dp)
            )
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    imageVector = Icons.Default.Alarm,
                    contentDescription = "",
                )
                Image(
                    imageVector = Icons.Default.CheckBoxOutlineBlank,
                    contentDescription = "",
                )
            }
        }
    }
}

@Composable
fun AddSchedule(
    modifier: Modifier, onAdd: () -> Unit
) {
    Card(backgroundColor = Color.LightGray, modifier = modifier.clickable {
        onAdd()
    }) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(imageVector = Icons.Default.Add, contentDescription = null)
            Text(text = "일정을 추가해보세요!")
        }
    }
}

fun LazyListScope.todayContent(
    list: List<String>, onAdd: () -> Unit
) {
    item {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 15.dp, end = 15.dp, top = 15.dp),
            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
            backgroundColor = Color.DarkGray
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = "오늘")
                Text(text = LocalDateTime.now().toString())
            }
        }
    }

    items(list) {
        TodayItem(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 50.dp)
                .padding(start = 15.dp, end = 15.dp),
            it,
        )
    }

    item {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
            shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
            backgroundColor = Color.DarkGray
        ) {
            AddSchedule(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(5.dp),
            ) {
                onAdd()
            }
        }
    }
}


