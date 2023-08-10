package kr.sjh.myschedule

import java.time.LocalDateTime

data class AlarmItem(
    var id: Int,
    val time: LocalDateTime,
    val title: String,
    val content: String = ""
)