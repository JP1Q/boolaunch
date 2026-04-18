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
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.jp1q.boolaunch.databinding.ActivityMainBinding
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AppAdapter
    private lateinit var searchAdapter: SearchAppAdapter
    private lateinit var gestureDetector: GestureDetector
    private var allApps: List<AppInfo> = emptyList()
    private var isSearchScreenVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appList.layoutManager = LinearLayoutManager(this)
        adapter = AppAdapter(emptyList()) { app -> launchApp(app) }
        binding.appList.adapter = adapter
        binding.searchAppList.layoutManager = LinearLayoutManager(this)
        searchAdapter = SearchAppAdapter(
            apps = emptyList(),
            packageManager = packageManager,
            iconTintColor = ContextCompat.getColor(this, R.color.text)
        ) { app -> launchApp(app) }
        binding.searchAppList.adapter = searchAdapter

        gestureDetector = createSwipeGestureDetector()
        binding.searchInput.doAfterTextChanged { query ->
            searchAdapter.updateApps(AppSearchFilter.filter(allApps, query?.toString().orEmpty()))
        }

        updateHeaderStatus()
        refreshAppList()
    }

    override fun onResume() {
        super.onResume()
        updateHeaderStatus()
        refreshAppList()
    }

    private fun refreshAppList() {
        allApps = getInstalledApps()
        adapter.updateApps(allApps)
        searchAdapter.updateApps(
            AppSearchFilter.filter(allApps, binding.searchInput.text?.toString().orEmpty())
        )
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

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return super.dispatchTouchEvent(event)
    }

    private fun createSwipeGestureDetector(): GestureDetector {
        return GestureDetector(
            this,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onFling(
                    e1: MotionEvent?,
                    e2: MotionEvent?,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    val startEvent = e1 ?: return false
                    val endEvent = e2 ?: return false
                    val deltaX = endEvent.x - startEvent.x
                    val deltaY = endEvent.y - startEvent.y
                    val isHorizontalSwipe = kotlin.math.abs(deltaX) > kotlin.math.abs(deltaY)
                    val hasEnoughDistance = kotlin.math.abs(deltaX) > SWIPE_DISTANCE_THRESHOLD
                    val hasEnoughVelocity = kotlin.math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD
                    if (!isHorizontalSwipe || !hasEnoughDistance || !hasEnoughVelocity) {
                        return false
                    }

                    if (deltaX < 0f && !isSearchScreenVisible) {
                        showSearchScreen()
                        return true
                    }

                    if (deltaX > 0f && isSearchScreenVisible) {
                        showHomeScreen()
                        return true
                    }

                    return false
                }
            }
        )
    }

    private fun showSearchScreen() {
        isSearchScreenVisible = true
        binding.appList.isVisible = false
        binding.searchContainer.isVisible = true
        binding.footerHintText.text = getString(R.string.footer_hint_back)
    }

    private fun showHomeScreen() {
        isSearchScreenVisible = false
        binding.searchContainer.isVisible = false
        binding.appList.isVisible = true
        binding.footerHintText.text = getString(R.string.footer_hint)
    }

    companion object {
        private const val SWIPE_DISTANCE_THRESHOLD = 80f
        private const val SWIPE_VELOCITY_THRESHOLD = 300f
    }
}
