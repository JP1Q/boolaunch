package com.jp1q.boolaunch

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jp1q.boolaunch.databinding.ActivityMainBinding
import java.util.Date
import java.util.Locale
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AppAdapter
    private lateinit var gestureDetector: GestureDetector

    companion object {
        private const val PAGE_INDEX_APPS = 0
        private const val PAGE_INDEX_SETTINGS = 1
        private const val SWIPE_DISTANCE_THRESHOLD_PX = 120
        private const val PREFERENCES_NAME = "boolaunch_settings"
        private const val COLOR_SCHEME_PREFERENCE_KEY = "color_scheme"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        applySelectedTheme()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appList.layoutManager = LinearLayoutManager(this)
        adapter = AppAdapter(emptyList()) { app -> launchApp(app) }
        binding.appList.adapter = adapter

        setupSwipeNavigation()
        setupSettingsPage()
        showAppsPage()
        updateHeaderStatus()
        refreshAppList()
    }

    override fun onResume() {
        super.onResume()
        updateHeaderStatus()
        refreshAppList()
    }

    private fun refreshAppList() {
        adapter.updateApps(getInstalledApps())
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return super.dispatchTouchEvent(event)
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
    }

    private fun setupSwipeNavigation() {
        gestureDetector = GestureDetector(
            this,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onDown(event: MotionEvent): Boolean {
                    return true
                }

                override fun onFling(
                    startEvent: MotionEvent?,
                    endEvent: MotionEvent,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    if (startEvent == null) {
                        return false
                    }
                    val horizontalDistance = endEvent.x - startEvent.x
                    val verticalDistance = endEvent.y - startEvent.y
                    if (abs(horizontalDistance) < SWIPE_DISTANCE_THRESHOLD_PX || abs(horizontalDistance) < abs(verticalDistance)) {
                        return false
                    }

                    if (horizontalDistance < 0) {
                        showSettingsPage()
                    } else {
                        showAppsPage()
                    }
                    return true
                }
            }
        )
    }

    private fun setupSettingsPage() {
        val selectedScheme = getSelectedColorScheme()
        binding.colorSchemeGroup.check(
            when (selectedScheme) {
                ColorScheme.CRIMSON -> R.id.colorSchemeCrimson
                ColorScheme.EMERALD -> R.id.colorSchemeEmerald
            }
        )

        binding.colorSchemeGroup.setOnCheckedChangeListener { _, checkedId ->
            val scheme = when (checkedId) {
                R.id.colorSchemeEmerald -> ColorScheme.EMERALD
                else -> ColorScheme.CRIMSON
            }
            onColorSchemeSelected(scheme)
        }
    }

    private fun onColorSchemeSelected(colorScheme: ColorScheme) {
        if (colorScheme == getSelectedColorScheme()) {
            return
        }
        getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)
            .edit()
            .putString(COLOR_SCHEME_PREFERENCE_KEY, colorScheme.preferenceValue)
            .apply()
        recreate()
    }

    private fun applySelectedTheme() {
        setTheme(
            when (getSelectedColorScheme()) {
                ColorScheme.CRIMSON -> R.style.Theme_Boolaunch
                ColorScheme.EMERALD -> R.style.Theme_Boolaunch_Emerald
            }
        )
    }

    private fun getSelectedColorScheme(): ColorScheme {
        val selectedValue = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)
            .getString(COLOR_SCHEME_PREFERENCE_KEY, null)
        return ColorScheme.fromPreferenceValue(selectedValue)
    }

    private fun showAppsPage() {
        if (binding.contentFlipper.displayedChild == PAGE_INDEX_APPS) {
            return
        }
        binding.contentFlipper.displayedChild = PAGE_INDEX_APPS
        binding.footerHintText.text = getString(R.string.footer_hint_apps)
    }

    private fun showSettingsPage() {
        if (binding.contentFlipper.displayedChild == PAGE_INDEX_SETTINGS) {
            return
        }
        binding.contentFlipper.displayedChild = PAGE_INDEX_SETTINGS
        binding.footerHintText.text = getString(R.string.footer_hint_settings)
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
}
