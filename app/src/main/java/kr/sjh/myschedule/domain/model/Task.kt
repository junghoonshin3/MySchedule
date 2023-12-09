package kr.sjh.myschedule.domain.model

import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

data class Task(
    var id: Long? = null, var scheduleId: Long? = null, val regDt: LocalDate
)