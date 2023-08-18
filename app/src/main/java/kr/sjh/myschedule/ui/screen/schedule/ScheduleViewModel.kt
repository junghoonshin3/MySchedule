package kr.sjh.myschedule.ui.screen.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kizitonwose.calendar.core.atStartOfMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.data.repository.ScheduleRepository
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject


data class ScheduleUiState(
    val monthSchedule: Map<LocalDate, List<ScheduleEntity>> = emptyMap(),
)

@HiltViewModel
class ScheduleViewModel @Inject constructor(private val repository: ScheduleRepository) :
    ViewModel() {

    private var _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState

    //    private val _scheduleList = MutableStateFlow<List<ScheduleEntity>>(emptyList())
//    val scheduleList: StateFlow<List<ScheduleEntity>> = _scheduleList
    val yearMonth = YearMonth.now()

    init {
        getAllBetweenSchedulesByGroup(yearMonth.atStartOfMonth(), yearMonth.atEndOfMonth())
    }

    fun getAllSchedules(localDate: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllSchedules(localDate).collectLatest {
//                _uiState.value = ScheduleUiState(it)
            }
        }
    }

    fun deleteSchedule(schedule: ScheduleEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSchedule(schedule.id).collectLatest {
//                val delete = _uiState.value.scheduleList.toMutableList()
//                delete.remove(schedule)
//                _uiState.value = ScheduleUiState(
//                    delete
//                )
            }
        }
    }

    fun insertSchedule(schedule: ScheduleEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertSchedule(schedule = schedule).collectLatest {
//                val insert = _uiState.value.scheduleList.toMutableList()
//                insert.add(schedule)
//                _uiState.value = ScheduleUiState(
//                    insert
//                )
            }
        }
    }

    fun getAllBetweenSchedulesByGroup(sDt: LocalDate, eDt: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllBetweenSchedulesByGroup(sDt, eDt).collectLatest { schedules ->
                val groupBySchedule = schedules.groupBy { it.regDt }
                _uiState.value = ScheduleUiState(groupBySchedule)
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
                val newSchedule =
                    _uiState.value.monthSchedule[schedule.regDt]?.toMutableList() ?: mutableListOf()
                newSchedule.add(schedule)
                _uiState.value = ScheduleUiState(
                    _uiState.value.copy().monthSchedule.toMutableMap().apply {
                        set(schedule.regDt, newSchedule)
                    }
                )
            }
        }
    }
}