package com.abhishek.internships.identifier.focusflow.models

data class DashBoardUiState(
    val totalScreenTime: String = "0m",
    val centerText: String = "",
    val unlockCount: Int = 0,
    val notificationCount: Int = 0,
    val pieEntries: List<PieChartItem> = emptyList(),
    val isLoading: Boolean = true
)

data class PieChartItem(
    val label: String,
    val value: Float
)
