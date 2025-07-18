package com.example.videogifwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.videogifwidget.utils.MediaPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VideoGifWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Update all widgets
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        super.onEnabled(context)
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
        super.onDisabled(context)
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        super.onDeleted(context, appWidgetIds)
    }

    companion object {
        fun updateAllWidgets(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                android.content.ComponentName(context, VideoGifWidgetProvider::class.java)
            )
            
            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId)
            }
        }

        private fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.widget_loading_layout)
            
            // Get the selected media URI from preferences
            val mediaUri = MediaPreferences.getSelectedMediaUri(context)
            
            if (mediaUri != null) {
                try {
                    val uri = Uri.parse(mediaUri)
                    loadMediaIntoWidget(context, views, uri, appWidgetManager, appWidgetId)
                } catch (e: Exception) {
                    // Show error state
                    views.setTextViewText(R.id.widget_status_text, context.getString(R.string.error_loading_media))
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            } else {
                // No media selected, show configuration prompt
                views.setTextViewText(R.id.widget_status_text, context.getString(R.string.widget_tap_to_configure))
                
                // Set up click intent to open configuration
                val configIntent = Intent(context, WidgetConfigurationActivity::class.java).apply {
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                }
                val pendingIntent = PendingIntent.getActivity(
                    context, 
                    appWidgetId, 
                    configIntent, 
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_status_text, pendingIntent)
                
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }

        private fun loadMediaIntoWidget(
            context: Context,
            views: RemoteViews,
            mediaUri: Uri,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    // Use Glide to load the media (works for both GIFs and video thumbnails)
                    Glide.with(context)
                        .asBitmap()
                        .load(mediaUri)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                // Create a new RemoteViews for the actual media display
                                val mediaViews = RemoteViews(context.packageName, R.layout.widget_media_layout)
                                mediaViews.setImageViewBitmap(R.id.widget_media_view, resource)
                                
                                // Set up click intent to open the main app
                                val mainIntent = Intent(context, MainActivity::class.java)
                                val pendingIntent = PendingIntent.getActivity(
                                    context,
                                    appWidgetId,
                                    mainIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                                )
                                mediaViews.setOnClickPendingIntent(R.id.widget_media_view, pendingIntent)
                                
                                appWidgetManager.updateAppWidget(appWidgetId, mediaViews)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                                // Handle cleanup if needed
                            }

                            override fun onLoadFailed(errorDrawable: Drawable?) {
                                views.setTextViewText(R.id.widget_status_text, context.getString(R.string.error_loading_media))
                                appWidgetManager.updateAppWidget(appWidgetId, views)
                            }
                        })
                } catch (e: Exception) {
                    views.setTextViewText(R.id.widget_status_text, context.getString(R.string.error_loading_media))
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            }
        }
    }
}
