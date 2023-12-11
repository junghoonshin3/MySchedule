package kr.sjh.myschedule.domain.model

data class ScheduleWithTask(
    val schedule: Schedule = Schedule(),
    val tasks: List<Task> = emptyList()
)