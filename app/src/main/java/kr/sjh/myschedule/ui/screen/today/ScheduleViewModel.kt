package kr.sjh.myschedule.ui.screen.today

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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
import kr.sjh.myschedule.data.local.entity.ScheduleWithTask
import kr.sjh.myschedule.data.repository.Result.Fail
import kr.sjh.myschedule.data.repository.Result.Loading
import kr.sjh.myschedule.data.repository.Result.Success
import kr.sjh.myschedule.data.repository.ScheduleRepository
import kr.sjh.myschedule.domain.model.Schedule
import kr.sjh.myschedule.domain.model.Task
import kr.sjh.myschedule.utill.common.BottomSheetMode
import kr.sjh.myschedule.utill.common.BottomSheetMode.EDIT
import kr.sjh.myschedule.utill.common.BottomSheetMode.NEW_ADD
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

data class MainUiState(
    val yearScheduleMap: ImmutableMap<LocalDate, List<ScheduleWithTask>> = persistentMapOf(),
    val selectedDate: LocalDate = LocalDate.now(),
    val bottomSheetUiState: BottomSheetUiState = BottomSheetUiState()
)

data class BottomSheetUiState(
    val mode: BottomSheetMode = NEW_ADD,
    val id: Long? = null,
    val title: String = "",
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now(),
    val startTime: LocalTime = LocalTime.now(),
    val endTime: LocalTime = LocalTime.now().plusHours(1),
    val color: Int = generateRandomColor().toArgb()
)

@HiltViewModel
class ScheduleViewModel @Inject constructor(private val repository: ScheduleRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    var bottomSheetDialog by mutableStateOf(false)

    fun getScheduleWithTasks(startDate: LocalDate, endDate: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getScheduleWithTasks(startDate, endDate).collectLatest { result ->
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

    fun setSelectedSchedule(scheduleWithTask: ScheduleWithTask) {
        _uiState.update {
            it.copy(
                bottomSheetUiState = it.bottomSheetUiState.copy(
                    id = scheduleWithTask.schedule.id,
                    mode = BottomSheetMode.EDIT,
                    title = scheduleWithTask.schedule.title,
                    startDate = scheduleWithTask.schedule.startDate,
                    endDate = scheduleWithTask.schedule.endDate,
                    startTime = scheduleWithTask.schedule.startTime,
                    endTime = scheduleWithTask.schedule.endTime
                )
            )
        }
        bottomSheetDialog = true
    }

    fun onAddSchedule() {
        _uiState.update {
            it.copy(
                bottomSheetUiState = it.bottomSheetUiState.copy(
                    id = null,
                    mode = NEW_ADD,
                    startDate = it.selectedDate,
                    endDate = it.selectedDate
                )
            )
        }
        bottomSheetDialog = true
    }

    fun setSelectedDate(date: LocalDate) {
        _uiState.update {
            it.copy(
                selectedDate = date
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


    fun onSave() {
        viewModelScope.launch(Dispatchers.IO) {
            when (uiState.value.bottomSheetUiState.mode) {
                EDIT -> {
                    repository.updateScheduleWithTasks(
                        schedule = Schedule(
                            id = uiState.value.bottomSheetUiState.id,
                            title = uiState.value.bottomSheetUiState.title,
                            startDate = uiState.value.bottomSheetUiState.startDate,
                            endDate = uiState.value.bottomSheetUiState.endDate,
                            startTime = uiState.value.bottomSheetUiState.startTime,
                            endTime = uiState.value.bottomSheetUiState.endTime,
                            color = uiState.value.bottomSheetUiState.color
                        ), tasks = getDaysBetween(
                            startDate = uiState.value.bottomSheetUiState.startDate,
                            endDate = uiState.value.bottomSheetUiState.endDate
                        )
                    )
                }

                NEW_ADD -> {
                    repository.updateScheduleWithTasks(
                        schedule = Schedule(
                            title = uiState.value.bottomSheetUiState.title,
                            startDate = uiState.value.bottomSheetUiState.startDate,
                            endDate = uiState.value.bottomSheetUiState.endDate,
                            startTime = uiState.value.bottomSheetUiState.startTime,
                            endTime = uiState.value.bottomSheetUiState.endTime,
                            color = uiState.value.bottomSheetUiState.color,
                        ), tasks = getDaysBetween(
                            startDate = uiState.value.bottomSheetUiState.startDate,
                            endDate = uiState.value.bottomSheetUiState.endDate
                        )
                    )
                }
            }
            bottomSheetDialog = false
        }
    }

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

