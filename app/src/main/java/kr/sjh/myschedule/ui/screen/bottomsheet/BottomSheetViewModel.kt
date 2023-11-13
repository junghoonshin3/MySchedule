package kr.sjh.myschedule.ui.screen.bottomsheet

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kr.sjh.myschedule.data.repository.ScheduleRepository
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(private val repository: ScheduleRepository) :
    ViewModel() {
    private var _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private var _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private var _dateSelection = MutableStateFlow(DateSelection())
    val dateSelection: StateFlow<DateSelection> = _dateSelection.asStateFlow()

    fun changeTitle(title: String) {
        _title.update {
            title
        }
    }

    fun clickDate(date: LocalDate) {
        _selectedDate.update {
            date
        }
    }

    fun setDateSelection(startDate: LocalDateTime?, endDate: LocalDateTime?) {
        _dateSelection.update {
            DateSelection(startDate, endDate)
        }
    }
}

data class DateSelection(val startDate: LocalDateTime? = null, val endDate: LocalDateTime? = null)