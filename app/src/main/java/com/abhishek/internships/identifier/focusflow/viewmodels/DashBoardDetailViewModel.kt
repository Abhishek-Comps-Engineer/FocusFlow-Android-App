package com.abhishek.internships.identifier.focusflow.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.abhishek.internships.identifier.focusflow.models.AppUsage
import com.abhishek.internships.identifier.focusflow.repositories.UsageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class DashBoardDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UsageRepository(application)

    private val calendar = Calendar.getInstance()

    private val _appUsageList = MutableLiveData<List<AppUsage>>()
    val appUsageList: LiveData<List<AppUsage>> = _appUsageList

    private val _totalTimeText = MutableLiveData<String>()
    val totalTimeText: LiveData<String> = _totalTimeText

    init {
        loadUsageForCurrentDate()
    }

    /** Load usage for current calendar date */
    private fun loadUsageForCurrentDate() {
        viewModelScope.launch(Dispatchers.IO) {

            val (list, totalTimeText) =
                repository.getUsageForDate(calendar.timeInMillis)

            _appUsageList.postValue(list)
            _totalTimeText.postValue(totalTimeText)
        }
    }

    /** ← Chevron (Previous Day) */
    fun loadPreviousDay() {
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        loadUsageForCurrentDate()
    }

    /** → Chevron (Next Day) */
    fun loadNextDay() {
        // Prevent future dates
        val today = Calendar.getInstance()

        if (calendar.before(today)) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            loadUsageForCurrentDate()
        }
    }

    /** Optional: reset to today */
    fun loadToday() {
        calendar.timeInMillis = System.currentTimeMillis()
        loadUsageForCurrentDate()
    }
}
