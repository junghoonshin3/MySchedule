package kr.sjh.myschedule.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.sjh.myschedule.data.local.entity.ScheduleDetailEntity
import kr.sjh.myschedule.data.repository.ScheduleRepository
import javax.inject.Inject

@HiltViewModel
class ScheduleDetailViewModel @Inject constructor(private val repository: ScheduleRepository) :
    ViewModel() {

    fun insertScheduleDetail(detail: ScheduleDetailEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertScheduleDetail(detail)
        }
    }

    fun getScheduleDetail(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getScheduleDetail(userId)
        }
    }


}