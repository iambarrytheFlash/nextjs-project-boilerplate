# GIF Video Widget - Android App

An Android application for Android 14 that allows users to display animated GIFs and videos as home screen widgets with 24/7 playback capability.

## Features

- **Custom Media Selection**: Choose GIFs or videos from your device gallery
- **Home Screen Widget**: Display animated content directly on your home screen
- **Customizable Display Options**: 
  - Fit to widget size or crop to fill
  - Loop playback control
  - Auto-play settings
- **Modern UI**: Built with Jetpack Compose and Material 3 design
- **Android 14 Support**: Optimized for the latest Android version
- **Permissions Handling**: Proper runtime permissions for media access

## Installation

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 34 (Android 14)
- Kotlin 1.9.10 or later

### Build Instructions

1. Clone or download the project
2. Open the `android-app` folder in Android Studio
3. Sync the project with Gradle files
4. Build and run the application

```bash
cd android-app
./gradlew assembleDebug
```

## Usage

### Adding the Widget

1. **Install the App**: Install the GIF Video Widget app on your Android device
2. **Grant Permissions**: Allow storage permissions to access your media files
3. **Select Media**: Choose a GIF or video from your gallery in the main app
4. **Add Widget**: 
   - Long press on your home screen
   - Select "Widgets" from the menu
   - Find "GIF Video Widget"
   - Drag it to your desired location
5. **Configure**: The widget configuration screen will appear automatically
6. **Customize**: Set your display preferences (scale type, loop, auto-play)
7. **Save**: Tap "Save Configuration" to apply your settings

### Widget Configuration

The widget offers several customization options:

- **Scale Type**:
  - **Fit to Widget**: Scales media to fit within widget bounds
  - **Crop to Fill**: Crops media to fill the entire widget area
- **Loop Playback**: Enable/disable continuous looping
- **Auto Play**: Control automatic playback start

## Technical Details

### Architecture

- **MVVM Pattern**: Clean separation of concerns
- **Jetpack Compose**: Modern declarative UI framework
- **Material 3**: Latest Material Design components
- **Glide**: Efficient image and GIF loading
- **SharedPreferences**: Persistent storage for user settings

### Key Components

1. **MainActivity**: Main app interface for media selection
2. **VideoGifWidgetProvider**: Core widget provider handling updates
3. **WidgetConfigurationActivity**: Widget setup and customization
4. **WidgetUpdateService**: Background service for periodic updates
5. **MediaPreferences**: Utility for storing user preferences

### Permissions

The app requires the following permissions:

- `READ_EXTERNAL_STORAGE` (Android < 13)
- `READ_MEDIA_IMAGES` (Android 13+)
- `READ_MEDIA_VIDEO` (Android 13+)
- `BIND_REMOTEVIEWS` (Widget functionality)
- `INTERNET` (Optional, for online media sources)

## File Structure

```
android-app/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/videogifwidget/
│   │   │   ├── MainActivity.kt
│   │   │   ├── VideoGifWidgetProvider.kt
│   │   │   ├── WidgetConfigurationActivity.kt
│   │   │   ├── WidgetUpdateService.kt
│   │   │   ├── ui/theme/
│   │   │   │   ├── Theme.kt
│   │   │   │   └── Type.kt
│   │   │   └── utils/
│   │   │       └── MediaPreferences.kt
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── widget_loading_layout.xml
│   │   │   │   └── widget_media_layout.xml
│   │   │   ├── values/
│   │   │   │   ├── strings.xml
│   │   │   │   ├── colors.xml
│   │   │   │   └── themes.xml
│   │   │   ├── xml/
│   │   │   │   └── video_gif_widget_info.xml
│   │   │   └── drawable/
│   │   │       └── widget_preview.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
└── settings.gradle
```

## Dependencies

- **Jetpack Compose**: UI framework
- **Glance**: Modern app widget framework
- **Glide**: Image loading and caching
- **Material 3**: Design system
- **Activity Compose**: Activity integration
- **Media3**: Video handling (optional)

## Troubleshooting

### Common Issues

1. **Widget Not Updating**: 
   - Check if the app has storage permissions
   - Verify the selected media file still exists
   - Try removing and re-adding the widget

2. **Media Not Loading**:
   - Ensure the file format is supported (GIF, MP4, etc.)
   - Check file permissions and accessibility
   - Try selecting a different media file

3. **Performance Issues**:
   - Large video files may impact performance
   - Consider using smaller GIF files for better performance
   - Adjust update frequency in widget settings

### Supported Formats

- **Images**: GIF, PNG, JPEG, WebP
- **Videos**: MP4, AVI, MOV, WebM (thumbnails)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly on different devices
5. Submit a pull request

## License

This project is open source. Feel free to use, modify, and distribute according to your needs.

## Support

For issues, questions, or feature requests, please create an issue in the project repository.

---

**Note**: This app is designed for Android 14 but should be compatible with Android 5.0 (API 21) and above. Some features may vary depending on the Android version and device manufacturer.
