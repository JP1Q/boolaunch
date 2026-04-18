package com.jp1q.boolaunch

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.jp1q.boolaunch.databinding.ItemSearchAppBinding

class SearchAppAdapter(
    apps: List<AppInfo>,
    private val packageManager: PackageManager,
    private val iconTintColor: Int,
    private val onAppClick: (AppInfo) -> Unit
) : RecyclerView.Adapter<SearchAppAdapter.SearchAppViewHolder>() {

    private val apps: MutableList<AppInfo> = apps.toMutableList()

    fun updateApps(newApps: List<AppInfo>) {
        apps.clear()
        apps.addAll(newApps)
        notifyDataSetChanged()
    }

    inner class SearchAppViewHolder(private val binding: ItemSearchAppBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(app: AppInfo) {
            binding.appName.text = TuiTextFormatter.formatLauncherItemLabel(app.label)
            binding.appIcon.setImageDrawable(createMonochromeIcon(app.packageName))
            binding.appIcon.contentDescription = binding.root.context.getString(
                R.string.app_icon_content_description_with_name,
                app.label
            )
            binding.root.setOnClickListener { onAppClick(app) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAppViewHolder {
        val binding = ItemSearchAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchAppViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchAppViewHolder, position: Int) {
        holder.bind(apps[position])
    }

    override fun getItemCount(): Int = apps.size

    private fun createMonochromeIcon(packageName: String): Drawable? {
        val originalIcon = runCatching { packageManager.getApplicationIcon(packageName) }.getOrNull()
            ?: return null
        val wrappedIcon = DrawableCompat.wrap(originalIcon.mutate())
        DrawableCompat.setTint(wrappedIcon, iconTintColor)
        return wrappedIcon
    }
}
