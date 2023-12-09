package kr.sjh.myschedule.domain.model

import androidx.compose.ui.graphics.toArgb
import kr.sjh.myschedule.ui.screen.today.generateRandomColor
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class Schedule(
    val id: Long? = null,
    val title: String = "",
    val describe: String = "",
)