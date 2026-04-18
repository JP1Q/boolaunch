package com.jp1q.boolaunch

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jp1q.boolaunch.databinding.ActivityMainBinding
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AppAdapter
    private val mainHandler = Handler(Looper.getMainLooper())
    private val clockRefreshRunnable = object : Runnable {
        override fun run() {
            updateHeaderStatus()
            mainHandler.postDelayed(this, millisUntilNextMinute())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appList.layoutManager = LinearLayoutManager(this)
        adapter = AppAdapter(emptyList()) { app -> launchApp(app) }
        binding.appList.adapter = adapter

        updateHeaderStatus()
        refreshAppList()
    }

    override fun onResume() {
        super.onResume()
        startClockRefresh()
        refreshAppList()
    }

    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(clockRefreshRunnable)
    }

    private fun refreshAppList() {
        adapter.updateApps(getInstalledApps())
    }

    private fun getInstalledApps(): List<AppInfo> {
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val resolveInfos: List<ResolveInfo> = packageManager.queryIntentActivities(
            intent,
            0
        )
        return resolveInfos
            .map { ri ->
                AppInfo(
                    label = ri.loadLabel(packageManager).toString(),
                    packageName = ri.activityInfo.packageName
                )
            }
            .filter { it.packageName != packageName }
            .sortedBy { it.label.lowercase() }
    }

    private fun launchApp(app: AppInfo) {
        val launchIntent = packageManager.getLaunchIntentForPackage(app.packageName)
        if (launchIntent != null) {
            startActivity(launchIntent)
        } else {
            Toast.makeText(this, getString(R.string.unable_to_launch, app.label), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateHeaderStatus() {
        binding.headerBatteryText.text = formatBatteryStatus()
        binding.headerTimeText.text = formatCurrentTime()
        binding.asciiClockText.text = formatAsciiClock()
    }

    private fun formatBatteryStatus(): String {
        // Null receiver queries the sticky battery broadcast once without subscribing long-term.
        val batteryStatusIntent = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            ?: return "[ --% ]"
        val batteryLevel = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val batteryScale = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        return HeaderStatusFormatter.formatBatteryStatus(
            batteryLevel = batteryLevel,
            batteryScale = batteryScale
        )
    }

    private fun formatCurrentTime(): String {
        return HeaderStatusFormatter.formatCurrentTime(
            currentDate = Date(),
            locale = Locale.getDefault()
        )
    }

    private fun formatAsciiClock(): String {
        return HeaderStatusFormatter.formatAsciiClock(
            currentDate = Date(),
            locale = Locale.getDefault()
        )
    }

    private fun startClockRefresh() {
        mainHandler.removeCallbacks(clockRefreshRunnable)
        mainHandler.post(clockRefreshRunnable)
    }

    private fun millisUntilNextMinute(): Long {
        val elapsedThisMinute = System.currentTimeMillis() % CLOCK_REFRESH_PERIOD_MILLIS
        return CLOCK_REFRESH_PERIOD_MILLIS - elapsedThisMinute
    }

    companion object {
        private const val CLOCK_REFRESH_PERIOD_MILLIS = 60_000L
    }
}
