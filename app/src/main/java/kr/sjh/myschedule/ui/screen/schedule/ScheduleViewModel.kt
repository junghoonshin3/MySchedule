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

@HiltViewModel
class ScheduleViewModel @Inject constructor(private val repository: ScheduleRepository) :
    ViewModel() {

    private val _scheduleList = MutableStateFlow<List<ScheduleEntity>>(emptyList())
    val scheduleList: StateFlow<List<ScheduleEntity>> = _scheduleList

    private val _schedule = MutableStateFlow<ScheduleEntity?>(null)
    val schedule: StateFlow<ScheduleEntity?> = _schedule

    var isFabShow: MutableStateFlow<Boolean> = MutableStateFlow(true)

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
            repository.deleteSchedule(schedule.id)
            val new = _scheduleList.value.toMutableList()
            Log.i("sjh", "new 1 >>> :${new.size}")
            new.remove(schedule)
            Log.i("sjh", "new 2 >>> :${new.size}")
            _scheduleList.value = new
        }
    }

    fun insertSchedule(schedule: ScheduleEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertSchedule(schedule = schedule)
        }
    }

    fun getSchedule(schedule: ScheduleEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertSchedule(schedule = schedule)
        }
    }

}