package com.abhishek.internships.identifier.focusflow.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.abhishek.internships.identifier.focusflow.models.DashBoardUiState
import com.abhishek.internships.identifier.focusflow.models.PieChartItem
import com.abhishek.internships.identifier.focusflow.repositories.UsageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashBoardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UsageRepository(application)

    private val _uiState = MutableLiveData(DashBoardUiState())
    val uiState: LiveData<DashBoardUiState> = _uiState

    init {
        loadToday()
    }

    fun loadToday() = loadData(isToday = true)
    fun loadYesterday() = loadData(isToday = false)

    private fun loadData(isToday: Boolean) {

        _uiState.value = _uiState.value?.copy(isLoading = true)

        viewModelScope.launch(Dispatchers.IO) {

            val usageList = if (isToday)
                repository.getTodayUsage()
            else
                repository.getYesterdayUsage()

            val totalMinutes = repository.getTotalMinutes(usageList)
            val formattedTime = repository.formatMinutes(totalMinutes)

            // Pie chart logic (AGGREGATED)
            val topApps = usageList.take(5)
            val others = usageList.drop(5)

            val pieItems = mutableListOf<PieChartItem>()

            topApps.forEach {
                pieItems.add(
                    PieChartItem(
                        label = it.appName,
                        value = it.usageTimeMinutes.toFloat()
                    )
                )
            }

            if (others.isNotEmpty()) {
                pieItems.add(
                    PieChartItem(
                        label = "Others",
                        value = others.sumOf { it.usageTimeMinutes }.toFloat()
                    )
                )
            }

            _uiState.postValue(
                DashBoardUiState(
                    isLoading = false,
                    centerText = "${if (isToday) "Today" else "Yesterday"}\n$formattedTime",
                    totalScreenTime = formattedTime,
//                    appUsageList = usageList,
                    pieEntries = pieItems
                )
            )
        }
    }
}
