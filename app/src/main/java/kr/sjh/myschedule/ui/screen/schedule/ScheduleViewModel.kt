package kr.sjh.myschedule.ui.screen.schedule

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

@HiltViewModel
class ScheduleViewModel @Inject constructor(private val repository: ScheduleRepository) :
    ViewModel() {

    private val _scheduleList = MutableStateFlow<List<ScheduleEntity>>(emptyList())
    val scheduleList: StateFlow<List<ScheduleEntity>> = _scheduleList

    init {
        getAllSchedules(LocalDate.now())
    }

    fun getAllSchedules(localDate: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllSchedules(localDate).collectLatest {
                _scheduleList.value = it
            }
        }
    }

    fun deleteSchedule(schedule: ScheduleEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSchedule(schedule.id).collectLatest {
                val new = _scheduleList.value.toMutableList()
                new.remove(schedule)
                _scheduleList.value = new
            }
        }
    }

//    fun insertSchedule(schedule: ScheduleEntity) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.insertSchedule(schedule = schedule).collectLatest {
//
//            }
//        }
//    }
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
                val new = _scheduleList.value.toMutableList()
                new.remove(schedule)
                _scheduleList.value = new
            }

        }
    }
}