package kr.sjh.myschedule.ui.screen.schedule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.data.repository.ScheduleRepository
import java.time.LocalDate
import javax.inject.Inject


data class ScheduleUiState(
    val scheduleList: List<ScheduleEntity> = emptyList(),
)

@HiltViewModel
class ScheduleViewModel @Inject constructor(private val repository: ScheduleRepository) :
    ViewModel() {

    private var _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState
//    private val _scheduleList = MutableStateFlow<List<ScheduleEntity>>(emptyList())
//    val scheduleList: StateFlow<List<ScheduleEntity>> = _scheduleList

    init {
        getAllSchedules(LocalDate.now())
    }

    fun getAllSchedules(localDate: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllSchedules(localDate).collectLatest {
                _uiState.value = ScheduleUiState(it)
            }
        }
    }

    fun deleteSchedule(schedule: ScheduleEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSchedule(schedule.id).collectLatest {
                val delete = _uiState.value.scheduleList.toMutableList()
                delete.remove(schedule)
                _uiState.value = ScheduleUiState(
                    delete
                )
            }
        }
    }

    fun insertSchedule(schedule: ScheduleEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertSchedule(schedule = schedule).collectLatest {
                val insert = _uiState.value.scheduleList.toMutableList()
                insert.add(schedule)
                _uiState.value = ScheduleUiState(
                    insert
                )
            }
        }
    }
//
//    fun getSchedule(schedule: ScheduleEntity) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.insertSchedule(schedule = schedule).collectLatest {
//
//            }
//        }
//    }

    fun updateSchedule(schedule: ScheduleEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSchedule(schedule).collectLatest {
                val new = _uiState.value.scheduleList.toMutableList()
                new.remove(schedule)
                _uiState.value = ScheduleUiState(new)
            }

        }
    }
}