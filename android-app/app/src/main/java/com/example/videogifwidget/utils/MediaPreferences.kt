package com.example.videogifwidget.utils

import android.content.Context
import android.content.SharedPreferences

object MediaPreferences {
    private const val PREFS_NAME = "video_gif_widget_prefs"
    private const val KEY_SELECTED_MEDIA_URI = "selected_media_uri"
    private const val KEY_WIDGET_SCALE_TYPE = "widget_scale_type"
    private const val KEY_LOOP_PLAYBACK = "loop_playback"
    private const val KEY_AUTO_PLAY = "auto_play"

    enum class ScaleType {
        FIT_TO_WIDGET,
        CROP_TO_FILL
    }

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveSelectedMediaUri(context: Context, uri: String) {
        getPreferences(context).edit()
            .putString(KEY_SELECTED_MEDIA_URI, uri)
            .apply()
    }

    fun getSelectedMediaUri(context: Context): String? {
        return getPreferences(context).getString(KEY_SELECTED_MEDIA_URI, null)
    }

    fun saveScaleType(context: Context, scaleType: ScaleType) {
        getPreferences(context).edit()
            .putString(KEY_WIDGET_SCALE_TYPE, scaleType.name)
            .apply()
    }

    fun getScaleType(context: Context): ScaleType {
        val scaleTypeName = getPreferences(context).getString(KEY_WIDGET_SCALE_TYPE, ScaleType.FIT_TO_WIDGET.name)
        return try {
            ScaleType.valueOf(scaleTypeName ?: ScaleType.FIT_TO_WIDGET.name)
        } catch (e: IllegalArgumentException) {
            ScaleType.FIT_TO_WIDGET
        }
    }

    fun saveLoopPlayback(context: Context, loop: Boolean) {
        getPreferences(context).edit()
            .putBoolean(KEY_LOOP_PLAYBACK, loop)
            .apply()
    }

    fun getLoopPlayback(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_LOOP_PLAYBACK, true)
    }

    fun saveAutoPlay(context: Context, autoPlay: Boolean) {
        getPreferences(context).edit()
            .putBoolean(KEY_AUTO_PLAY, autoPlay)
            .apply()
    }

    fun getAutoPlay(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_AUTO_PLAY, true)
    }

    fun clearAllPreferences(context: Context) {
        getPreferences(context).edit().clear().apply()
    }
}
