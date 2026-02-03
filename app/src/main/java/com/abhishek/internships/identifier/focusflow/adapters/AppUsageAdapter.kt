package com.abhishek.internships.identifier.focusflow.adapters

import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abhishek.internships.identifier.focusflow.databinding.ItemTopAppBinding
import com.abhishek.internships.identifier.focusflow.models.AppUsage

class AppUsageAdapter(
    private var list: List<AppUsage>
) : RecyclerView.Adapter<AppUsageAdapter.AppUsageViewHolder>() {

    inner class AppUsageViewHolder(
        private val binding: ItemTopAppBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AppUsage, maxUsage: Int) {

            binding.tvAppName.text = item.appName

            val hours = item.usageTimeMinutes / 60
            val minutes = item.usageTimeMinutes % 60
            binding.tvAppTime.text = "${hours}h ${minutes}m"

            // Progress relative to most used app
            binding.progressUsage.max = maxUsage
            binding.progressUsage.progress = item.usageTimeMinutes

            // Load REAL app icon
            try {
                val pm = itemView.context.packageManager
                binding.appIcon.setImageDrawable(
                    pm.getApplicationIcon(item.packageName)
                )
            } catch (e: PackageManager.NameNotFoundException) {
                binding.appIcon.setImageResource(android.R.drawable.sym_def_app_icon)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppUsageViewHolder {
        val binding =
            ItemTopAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppUsageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppUsageViewHolder, position: Int) {
        val maxUsage = list.maxOfOrNull { it.usageTimeMinutes } ?: 1
        holder.bind(list[position], maxUsage)
    }

    override fun getItemCount(): Int = list.size

    fun updateList(newList: List<AppUsage>) {
        list = newList
        notifyDataSetChanged()
    }
}
