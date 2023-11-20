package kr.sjh.myschedule.ui.screen.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.data.repository.Result
import kr.sjh.myschedule.data.repository.ScheduleRepository
import java.time.LocalDate
import javax.inject.Inject


data class ScheduleUiState(
    var allYearSchedules: List<ScheduleEntity> = emptyList()
)

data class ErrorUiState(
    var throwable: Throwable?
)

@HiltViewModel
class ScheduleViewModel @Inject constructor(private val repository: ScheduleRepository) :
    ViewModel() {

    private var _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState

    private var _errorState = MutableStateFlow(ErrorUiState(null))
    val errorState: StateFlow<ErrorUiState> = _errorState

    init {
        getAllYearSchedules(LocalDate.now().year)
    }


    fun deleteSchedule(schedule: ScheduleEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSchedule(schedule.id).collectLatest {
                remove(schedule)
            }
        }
    }

    fun insertOrUpdate(schedule: ScheduleEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertOrUpdate(schedule).collectLatest {
                schedule.id = it
                update(schedule)
            }
        }
    }

    fun completeSchedule(schedule: ScheduleEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            schedule.isComplete = true
            repository.updateSchedule(schedule).collectLatest {
                remove(schedule)
            }
        }
    }

    fun getAllYearSchedules(year: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getYearSchedules(year).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                allYearSchedules = result.data
                            )
                        }
                    }

                    is Result.Fail -> {
                        _errorState.update {
                            it.copy(
                                throwable = result.throwable
                            )
                        }
                    }

                    is Result.Loading -> {

                    }

                    else -> {}
                }
            }
        }
    }

    private fun remove(schedule: ScheduleEntity) {
        val removeList = _uiState.value.allYearSchedules.toMutableList()
        removeList.remove(schedule)
        _uiState.update {
            ScheduleUiState(allYearSchedules = removeList)
        }
    }

    private fun update(schedule: ScheduleEntity) {
        val updateList = uiState.value.allYearSchedules.toMutableList()
        val findSchedule = updateList.find { it.id == schedule.id }
        if (findSchedule != null) {
            val index = updateList.indexOf(findSchedule)
            updateList[index] = schedule
        } else {
            updateList.add(schedule)
        }
        _uiState.value.allYearSchedules = updateList
    }

}
