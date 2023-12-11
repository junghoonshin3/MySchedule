package kr.sjh.myschedule.ui.screen.schedule

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
import kr.sjh.myschedule.data.repository.Result.Fail
import kr.sjh.myschedule.data.repository.Result.Loading
import kr.sjh.myschedule.data.repository.Result.Success
import kr.sjh.myschedule.data.repository.ScheduleRepositoryImpl
import kr.sjh.myschedule.domain.model.ScheduleWithTask
import kr.sjh.myschedule.domain.model.Task
import kr.sjh.myschedule.utill.common.SaveType
import kr.sjh.myschedule.utill.common.SaveType.EDIT
import kr.sjh.myschedule.utill.common.SaveType.NEW
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

data class MainUiState(
    val yearScheduleMap: ImmutableMap<LocalDate, List<ScheduleWithTask>> = persistentMapOf(),
    val selectedDate: LocalDate = LocalDate.now(),
    val bottomSheetUiState: BottomSheetUiState = BottomSheetUiState()
)

data class BottomSheetUiState(
    val saveType: SaveType = NEW,
    val scheduleWithTask: ScheduleWithTask = ScheduleWithTask(),
    val color: Int = generateRandomColor().toArgb(),
    val buttonText: String = "저장",
    val bottomSheetVisible: Boolean = false
)

@HiltViewModel
class ScheduleViewModel @Inject constructor(private val repository: ScheduleRepositoryImpl) :
    ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

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

    fun onEvent(event: ScheduleEvent) {
        when (event) {
            ScheduleEvent.ShowBottomSheet -> {
                _uiState.update {
                    it.copy(
                        bottomSheetUiState = it.bottomSheetUiState.copy(
                            bottomSheetVisible = true
                        )
                    )
                }
            }

            ScheduleEvent.HideBottomSheet -> {
                _uiState.update {
                    it.copy(
                        bottomSheetUiState = it.bottomSheetUiState.copy(
                            bottomSheetVisible = false
                        )
                    )
                }
            }

            ScheduleEvent.Save -> {
                viewModelScope.launch(Dispatchers.IO) {
                    when (uiState.value.bottomSheetUiState.saveType) {
                        EDIT -> {
                            repository.updateScheduleWithTasks(
                                uiState.value.bottomSheetUiState.scheduleWithTask.schedule,
                                uiState.value.bottomSheetUiState.scheduleWithTask.tasks
                            )
                        }

                        NEW -> {
                            repository.insertScheduleWithTasks(
                                uiState.value.bottomSheetUiState.scheduleWithTask.schedule,
                                uiState.value.bottomSheetUiState.scheduleWithTask.tasks
                            )
                        }
                    }
                    _uiState.update {
                        it.copy(
                            bottomSheetUiState = it.bottomSheetUiState.copy(
                                bottomSheetVisible = false
                            )
                        )
                    }
                }
            }

            is ScheduleEvent.SelectedDate -> {
                _uiState.update {
                    it.copy(
                        selectedDate = event.date
                    )
                }
            }

            is ScheduleEvent.SetStartEndDateTime -> {
                _uiState.update {
                    it.copy(
                        bottomSheetUiState = it.bottomSheetUiState.copy(
                            scheduleWithTask = it.bottomSheetUiState.scheduleWithTask.copy(
                                schedule = it.bottomSheetUiState.scheduleWithTask.schedule.copy(
                                    startDate = event.startDateTime.toLocalDate(),
                                    endDate = event.endDateTime.toLocalDate(),
                                    startTime = event.startDateTime.toLocalTime(),
                                    endTime = event.endDateTime.toLocalTime()
                                ), tasks = getDaysBetween(
                                    event.startDateTime.toLocalDate(),
                                    event.endDateTime.toLocalDate()
                                )
                            )

                        )
                    )
                }
            }

            is ScheduleEvent.SetTitle -> {
                _uiState.update {
                    it.copy(
                        bottomSheetUiState = it.bottomSheetUiState.copy(
                            scheduleWithTask = it.bottomSheetUiState.scheduleWithTask.copy(
                                schedule = it.bottomSheetUiState.scheduleWithTask.schedule.copy(
                                    title = event.title
                                )
                            )
                        )
                    )
                }
            }

            is ScheduleEvent.AddSchedule -> {
                _uiState.update {
                    when (event.type) {
                        EDIT -> {
                            it.copy(
                                bottomSheetUiState = it.bottomSheetUiState.copy(
                                    scheduleWithTask = event.scheduleWithTask,
                                    bottomSheetVisible = true,
                                    buttonText = "수정"
                                )
                            )
                        }

                        NEW -> {
                            it.copy(
                                bottomSheetUiState = it.bottomSheetUiState.copy(
                                    scheduleWithTask = event.scheduleWithTask.copy(
                                        schedule = event.scheduleWithTask.schedule.copy(
                                            startDate = uiState.value.selectedDate,
                                            endDate = uiState.value.selectedDate,
                                        )
                                    ), bottomSheetVisible = true, buttonText = "저장"
                                )
                            )
                        }
                    }

                }
            }

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

