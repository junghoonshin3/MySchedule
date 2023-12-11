package kr.sjh.myschedule.domain.model

import androidx.compose.ui.graphics.toArgb
import kr.sjh.myschedule.ui.screen.schedule.generateRandomColor
import java.time.LocalDate
import java.time.LocalTime

data class Schedule(
    val id: Long? = null,
    val title: String = "",
    val describe: String = "",
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now(),
    val startTime: LocalTime = LocalTime.now(),
    val endTime: LocalTime = LocalTime.now().plusHours(1),
    val color: Int = generateRandomColor().toArgb()
)