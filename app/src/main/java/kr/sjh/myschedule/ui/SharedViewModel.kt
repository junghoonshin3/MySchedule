package kr.sjh.myschedule.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kr.sjh.myschedule.data.repository.ScheduleRepository
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: ScheduleRepository) :
    ViewModel() {
    private var _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    fun changeTitle(title: String) {
        _title.update {
            title
        }
    }
}