package kr.sjh.myschedule.ui.screen.today

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.sjh.myschedule.data.local.entity.ScheduleWithTask
import kr.sjh.myschedule.data.repository.Result
import kr.sjh.myschedule.data.repository.Result.Fail
import kr.sjh.myschedule.data.repository.Result.Loading
import kr.sjh.myschedule.data.repository.Result.Success
import kr.sjh.myschedule.data.repository.ScheduleRepository
import kr.sjh.myschedule.domain.model.Schedule
import kr.sjh.myschedule.domain.model.Task
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Period
import javax.inject.Inject
import kotlin.math.abs

data class MainUiState(
    val yearScheduleMap: ImmutableMap<LocalDate, List<ScheduleWithTask>> = persistentMapOf(),
    val selectedDate: LocalDate = LocalDate.now(),
    val bottomSheetUiState: BottomSheetUiState = BottomSheetUiState()
)

data class BottomSheetUiState(
    val id: Long? = null,
    val title: String = "",
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now(),
    val startTime: LocalTime = LocalTime.now(),
    val endTime: LocalTime = LocalTime.now().plusHours(1)
)

@HiltViewModel
class ScheduleViewModel @Inject constructor(private val repository: ScheduleRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    var bottomSheetDialog by mutableStateOf(false)

    init {
        getScheduleWithTasks()
    }

    fun getScheduleWithTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getScheduleWithTasks().collect { result ->
                when (result) {
                    is Success -> {
                        _uiState.update {
                            it.copy(
                                yearScheduleMap = result.data.toPersistentMap()
                            )
                        }
                    }

                    is Fail -> {

                    }

                    is Loading -> {

                    }
                }
            }
        }
    }

    fun setSelectedSchedule(schedule: ScheduleWithTask) {
        _uiState.update {
            it.copy(
                bottomSheetUiState = it.bottomSheetUiState.copy(
                    id = schedule.schedule.id,
                    title = schedule.schedule.title,
//                    startDateTime = schedule.selectedDates.first(),
//                    endDateTime = schedule.selectedDates.last()
                )
            )
        }
        bottomSheetDialog = true
    }

    fun onAddSchedule() {
        bottomSheetDialog = true
    }

    fun setSelectedDate(date: LocalDate) {
        _uiState.update {
            it.copy(
                selectedDate = date, bottomSheetUiState = it.bottomSheetUiState.copy(
                    startDate = date, endDate = date
//                    startDate = LocalDateTime.of(date, LocalTime.now()),
//                    endDate = LocalDateTime.of(date, LocalTime.now()).plusHours(1)
                )
            )
        }
    }

    fun onTitleChange(title: String) {
        _uiState.update {
            it.copy(
                bottomSheetUiState = it.bottomSheetUiState.copy(
                    title = title
                )
            )
        }
    }

    fun onDateRange(startDateTime: LocalDateTime, endDateTime: LocalDateTime) {
        _uiState.update {
            it.copy(
                bottomSheetUiState = it.bottomSheetUiState.copy(
                    startDate = startDateTime.toLocalDate(),
                    endDate = endDateTime.toLocalDate(),
                    startTime = startDateTime.toLocalTime(),
                    endTime = endDateTime.toLocalTime()
                )
            )
        }
    }

    fun onAlarmTime(time: LocalTime) {
        _uiState.update {
            it.copy(
//                selectedSchedule = it.selectedSchedule?.copy(
//                    alarmTime = time
//                )
            )
        }
    }

    fun onAlarm(isAlarm: Boolean) {
        _uiState.update {
            it.copy(
//                selectedSchedule = it.selectedSchedule?.copy(
//                    isAlarm = isAlarm
//                )
            )
        }
    }

    fun onSave() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertScheduleWithTasks(
                schedule = Schedule(
                    title = uiState.value.bottomSheetUiState.title,
                ), tasks = getDaysBetween(
                    startDate = uiState.value.bottomSheetUiState.startDate,
                    endDate = uiState.value.bottomSheetUiState.endDate
                )
            )
            bottomSheetDialog = false
        }
    }

//    fun clearSelectedSchedule() {
//        _uiState.update {
//            it.copy(
//                selectedSchedule = null
//            )
//        }
//    }

    private fun getDaysBetween(
        startDate: LocalDate, endDate: LocalDate
    ): List<Task> {
        val tasks = mutableListOf<Task>()

        var currentDate = startDate
        while (!currentDate.isAfter(endDate)) {
            tasks.add(Task(regDt = currentDate))
            currentDate = currentDate.plusDays(1)
        }
        return tasks
    }
}

