package  com.abhishek.internships.identifier.focusflow.permissions

import android.app.AppOpsManager
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.abhishek.internships.identifier.focusflow.databinding.ActivityPermissionBinding
import com.abhishek.internships.identifier.focusflow.ui.DashBoardActivity

class PermissionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPermissionBinding

    companion object {
        private const val TAG = "PermissionActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isPermissionGranted = hasUsageAccessPermission()

        if (isPermissionGranted) {
            navigateToDashboard()
        }

        binding.btnGrantPermission.setOnClickListener {
            openUsageAccessSettings()
        }
    }

    override fun onResume() {
        super.onResume()

        val isPermissionGranted = hasUsageAccessPermission()

        if (isPermissionGranted) {
            navigateToDashboard()
        } else {
        }
    }

    /**
     * Opens system Usage Access settings screen
     */
    private fun openUsageAccessSettings() {
        Log.d(TAG, "Opening Usage Access Settings")
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }

    /**
     * Checks whether Usage Access permission is granted
     */
    private fun hasUsageAccessPermission(): Boolean {
        return try {
            val appOpsManager = getSystemService(APP_OPS_SERVICE) as AppOpsManager

            val mode = appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                packageName
            )

            Log.d(TAG, "AppOpsManager mode value: $mode")

            mode == AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
            Log.e(TAG, "Error checking usage access permission", e)
            false
        }
    }

    /**
     * Navigate to Dashboard screen
     */
    private fun navigateToDashboard() {
        Log.d(TAG, "Navigating to DashboardActivity")
        startActivity(Intent(this, DashBoardActivity::class.java))
        finish()
    }
}
