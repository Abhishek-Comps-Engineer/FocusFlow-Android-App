package com.abhishek.internships.identifier.focusflow.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhishek.internships.identifier.focusflow.adapters.AppUsageAdapter
import com.abhishek.internships.identifier.focusflow.databinding.ActivityDashBoardDetailBinding
import com.abhishek.internships.identifier.focusflow.models.AppUsage
import com.abhishek.internships.identifier.focusflow.viewmodels.DashBoardDetailViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class DashboardDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashBoardDetailBinding
    private val viewModel: DashBoardDetailViewModel by viewModels()
    private lateinit var adapter: AppUsageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupBarChart()
        observeViewModel()

        binding.ivChervronLeft.setOnClickListener {
            viewModel.loadPreviousDay()
        }

        binding.ivChervronRight.setOnClickListener {
            viewModel.loadNextDay()
        }
    }

    private fun setupRecyclerView() {
        adapter = AppUsageAdapter(emptyList())
        binding.rvAppUsage.layoutManager = LinearLayoutManager(this)
        binding.rvAppUsage.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.totalTimeText.observe(this) {
            binding.tvMainTime.text = it
        }

        viewModel.appUsageList.observe(this) {
            adapter.updateList(it)
            updateBarChart(it)
        }
    }

    /* ---------------- BAR CHART ---------------- */

    private fun setupBarChart() = binding.barChart.apply {
        description.isEnabled = false
        legend.isEnabled = false
        setDrawGridBackground(false)
        setScaleEnabled(false)
        setTouchEnabled(false)

        axisRight.isEnabled = false

        axisLeft.apply {
            textColor = Color.GRAY
            setDrawGridLines(true)
            axisMinimum = 0f
        }

        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textColor = Color.GRAY
            setDrawGridLines(false)
            granularity = 1f
        }
    }

    private fun updateBarChart(apps: List<AppUsage>) {

        // Show top 7 apps (Digital Wellbeing style)
        val topApps = apps.take(7)

        val entries = topApps.mapIndexed { index, app ->
            BarEntry(index.toFloat(), app.usageTimeMinutes.toFloat())
        }

        val labels = topApps.map { it.appName }

        val dataSet = BarDataSet(entries, "Usage").apply {
            color = Color.parseColor("#3F51B5")
            valueTextColor = Color.BLACK
            valueTextSize = 10f
        }

        binding.barChart.xAxis.valueFormatter =
            IndexAxisValueFormatter(labels)

        binding.barChart.data = BarData(dataSet).apply {
            barWidth = 0.6f
        }

        binding.barChart.invalidate()
    }
}
