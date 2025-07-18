package com.example.videogifwidget

import android.Manifest
import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.videogifwidget.ui.theme.VideoGifWidgetTheme
import com.example.videogifwidget.utils.MediaPreferences

class WidgetConfigurationActivity : ComponentActivity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the result to CANCELED. This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)

        // Find the widget id from the intent.
        appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        setContent {
            VideoGifWidgetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WidgetConfigurationScreen(
                        onConfigurationComplete = { mediaUri ->
                            configureWidget(mediaUri)
                        }
                    )
                }
            }
        }
    }

    private fun configureWidget(mediaUri: Uri?) {
        val context = this

        // Save the selected media URI
        mediaUri?.let {
            MediaPreferences.saveSelectedMediaUri(context, it.toString())
        }

        // Update the widget
        val appWidgetManager = AppWidgetManager.getInstance(context)
        VideoGifWidgetProvider.updateAllWidgets(context)

        // Make sure we pass back the original appWidgetId
        val resultValue = Intent().apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
        setResult(RESULT_OK, resultValue)
        finish()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WidgetConfigurationScreen(
    onConfigurationComplete: (Uri?) -> Unit
) {
    val context = LocalContext.current
    var selectedMediaUri by remember { mutableStateOf<Uri?>(null) }
    var scaleType by remember { mutableStateOf(MediaPreferences.ScaleType.FIT_TO_WIDGET) }
    var loopPlayback by remember { mutableStateOf(true) }
    var autoPlay by remember { mutableStateOf(true) }
    var hasStoragePermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED
            } else {
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            }
        )
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasStoragePermission = permissions.values.all { it }
    }

    // Media picker launcher
    val mediaPickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedMediaUri = uri
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Configure Widget",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Select media and configure display options for your widget",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        // Permission Card
        if (!hasStoragePermission) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Storage Permission Required",
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                arrayOf(
                                    Manifest.permission.READ_MEDIA_IMAGES,
                                    Manifest.permission.READ_MEDIA_VIDEO
                                )
                            } else {
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                            permissionLauncher.launch(permissions)
                        }
                    ) {
                        Text("Grant Permission")
                    }
                }
            }
        }

        // Media Selection Card
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Media",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))

                selectedMediaUri?.let { uri ->
                    Text(
                        text = "Selected: ${uri.lastPathSegment}",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = {
                        if (hasStoragePermission) {
                            val intent = Intent(Intent.ACTION_PICK).apply {
                                type = "*/*"
                                val mimeTypes = arrayOf("image/gif", "video/*")
                                putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                            }
                            mediaPickerLauncher.launch(intent)
                        }
                    },
                    enabled = hasStoragePermission,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Choose GIF or Video")
                }
            }
        }

        // Display Options Card
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Display Options",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Scale Type Selection
                Text(
                    text = "Scale Type",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        onClick = { scaleType = MediaPreferences.ScaleType.FIT_TO_WIDGET },
                        label = { Text("Fit to Widget", fontSize = 12.sp) },
                        selected = scaleType == MediaPreferences.ScaleType.FIT_TO_WIDGET,
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        onClick = { scaleType = MediaPreferences.ScaleType.CROP_TO_FILL },
                        label = { Text("Crop to Fill", fontSize = 12.sp) },
                        selected = scaleType == MediaPreferences.ScaleType.CROP_TO_FILL,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Playback Options
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Loop Playback", fontSize = 14.sp)
                    Switch(
                        checked = loopPlayback,
                        onCheckedChange = { loopPlayback = it }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Auto Play", fontSize = 14.sp)
                    Switch(
                        checked = autoPlay,
                        onCheckedChange = { autoPlay = it }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save Configuration Button
        Button(
            onClick = {
                // Save preferences
                MediaPreferences.saveScaleType(context, scaleType)
                MediaPreferences.saveLoopPlayback(context, loopPlayback)
                MediaPreferences.saveAutoPlay(context, autoPlay)
                
                // Complete configuration
                onConfigurationComplete(selectedMediaUri)
            },
            enabled = selectedMediaUri != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Configuration")
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
