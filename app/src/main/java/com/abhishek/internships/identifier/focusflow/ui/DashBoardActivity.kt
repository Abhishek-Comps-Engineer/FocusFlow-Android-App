package com.abhishek.internships.identifier.focusflow.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.abhishek.internships.identifier.focusflow.databinding.ActivityBoardDashBinding
import com.abhishek.internships.identifier.focusflow.models.PieChartItem
import com.abhishek.internships.identifier.focusflow.viewmodels.DashBoardViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class DashBoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardDashBinding
    private val viewModel: DashBoardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityBoardDashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.dashboardToolbar)

        observeUi()
        viewModel.loadToday()

        binding.dashboardDetail.setOnClickListener {
            startActivity(Intent(this, DashboardDetailActivity::class.java))
        }

        binding.bedtimeDetail.setOnClickListener {
            startActivity(Intent(this, DashboardDetailActivity::class.java))
        }

        binding.focusModeDetail.setOnClickListener {
            startActivity(Intent(this, DashboardDetailActivity::class.java))
        }

    }

    private fun observeUi() {
        viewModel.uiState.observe(this) { state ->

//            binding.tvTotalTime.text = state.totalScreenTime
            binding.tvUnlocks.text = "${state.unlockCount} \nUnlocks"
            binding.tvNotifications.text = "${state.notificationCount} \nNotifications"

            setupPieChart(state.pieEntries, state.centerText)
        }
    }


    private fun setupPieChart(
        items: List<PieChartItem>,
        centerText: String
    ) {
        val entries = items.map {
            PieEntry(it.value, it.label)
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = listOf(
            Color.parseColor("#3F51B5"),
            Color.parseColor("#FF4081"),
            Color.parseColor("#FFC107"),
            Color.parseColor("#4CAF50"),
            Color.parseColor("#9C27B0"),
            Color.GRAY
        )
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 12f

        binding.pieChart.apply {
            data = PieData(dataSet)
            description.isEnabled = false
            setUsePercentValues(true)
            this.centerText = centerText
            setCenterTextSize(14f)
            invalidate()
        }
    }
}
