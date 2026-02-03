package com.abhishek.internships.identifier.focusflow.repositories

import android.app.usage.UsageStatsManager
import android.content.Context
import android.text.format.DateUtils
import com.abhishek.internships.identifier.focusflow.models.AppUsage
import java.util.Calendar

class UsageRepository(context: Context) {

    private val usageStatsManager =
        context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    fun getTodayUsage(): List<AppUsage> {
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val startTime = calendar.timeInMillis
        return getUsageBetween(startTime, endTime)
    }


    fun getYesterdayUsage(): List<AppUsage> {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val endTime = calendar.timeInMillis
        val startTime = endTime - DateUtils.DAY_IN_MILLIS

        return getUsageBetween(startTime, endTime)
    }

    fun getUsageForDate(dayMillis: Long): Pair<List<AppUsage>, String> {

        val calendar = Calendar.getInstance().apply {
            timeInMillis = dayMillis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val startTime = calendar.timeInMillis
        val endTime = startTime + DateUtils.DAY_IN_MILLIS

        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_BEST,
            startTime,
            endTime
        )

        val apps = stats
            .filter { it.totalTimeInForeground > 60_000 } // > 1 min
            .map {
                AppUsage(
                    appName = it.packageName.substringAfterLast("."),
                    packageName = it.packageName,
                    usageTimeMinutes = (it.totalTimeInForeground / 1000 / 60).toInt()
                )
            }
            .sortedByDescending { it.usageTimeMinutes }

        val totalMinutes = apps.sumOf { it.usageTimeMinutes }
        val formattedTime = formatMinutes(totalMinutes)

        return apps to formattedTime
    }

    private fun getUsageBetween(startTime: Long, endTime: Long): List<AppUsage> {

        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            startTime,
            endTime
        )

        return stats
            .filter { it.totalTimeInForeground > 60_000 } // ignore < 1 min
            .sortedByDescending { it.totalTimeInForeground }
            .map {
                AppUsage(
                    appName = it.packageName.substringAfterLast("."),
                    packageName = it.packageName,
                    usageTimeMinutes = (it.totalTimeInForeground / 1000 / 60).toInt()
                )
            }
    }

    fun getTotalMinutes(list: List<AppUsage>): Int {
        return list.sumOf { it.usageTimeMinutes }
    }

    fun formatMinutes(totalMinutes: Int): String {
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60

        return if (hours > 0) "${hours} hrs ${minutes} min"
        else "${minutes} min"
    }
}
