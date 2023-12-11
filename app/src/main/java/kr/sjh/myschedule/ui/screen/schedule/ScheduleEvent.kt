package kr.sjh.myschedule.ui.screen.schedule

import kr.sjh.myschedule.domain.model.ScheduleWithTask
import kr.sjh.myschedule.utill.common.SaveType
import java.time.LocalDate
import java.time.LocalDateTime

sealed interface ScheduleEvent {
    object Save : ScheduleEvent
    data class AddSchedule(val scheduleWithTask: ScheduleWithTask, val type: SaveType) :
        ScheduleEvent

    object ShowBottomSheet : ScheduleEvent
    object HideBottomSheet : ScheduleEvent
    data class SetTitle(val title: String) : ScheduleEvent

    data class SetStartEndDateTime(
        val startDateTime: LocalDateTime, val endDateTime: LocalDateTime
    ) : ScheduleEvent

    data class SelectedDate(val date: LocalDate) : ScheduleEvent
}
