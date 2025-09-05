# SMS to Synology Chat Forwarder

[![Build and Release APK](https://github.com/yourusername/SMSSynologyChat/actions/workflows/build-release.yml/badge.svg)](https://github.com/yourusername/SMSSynologyChat/actions/workflows/build-release.yml)

An Android application that automatically forwards incoming SMS messages to your Synology Chat channels via webhook integration. Perfect for monitoring important messages when you're not near your phone.

## 🚀 Features

- **Automatic SMS Forwarding**: Instantly forwards all incoming SMS messages to Synology Chat
- **Background Service**: Runs reliably in the background with foreground service
- **Battery Optimization Handling**: Built-in battery optimization exemption for uninterrupted operation
- **Test Webhook Functionality**: Test your webhook configuration before relying on it
- **Boot Persistence**: Automatically starts after device reboot
- **Clean UI**: Modern Material Design interface with clear status indicators
- **Privacy Focused**: All data stays between your phone and your Synology server

## 📱 Screenshots

> *Add screenshots of your app here*

## 🔧 Installation

### Option 1: Download Pre-built APK (Recommended)
1. Go to the [Releases](../../releases) page
2. Download the latest `sms-synology-chat-forwarder.apk`
3. Enable "Install from unknown sources" in Android settings
4. Install the APK

### Option 2: Build from Source
```bash
git clone https://github.com/yourusername/SMSSynologyChat.git
cd SMSSynologyChat
./gradlew assembleRelease
```

## ⚙️ Setup

### 1. Configure Synology Chat Webhook

1. Open **Synology Chat** on your NAS
2. Navigate to the channel where you want to receive SMS notifications
3. Click the **gear icon** → **Integrations**
4. Click **Add** → **Incoming Webhook**
5. Configure the webhook:
   - **Name**: SMS Forwarder
   - **Description**: Forwards SMS messages from Android
6. Copy the generated webhook URL

### 2. Configure the Android App

1. **Grant Permissions**: 
   - SMS reading permissions (required)
   - Notification permissions (for background service)
   - **Note**: On some devices (especially Samsung, Xiaomi, Huawei), you may need to enable **"Allow Restricted Settings"** in Developer Options before granting SMS permissions

2. **Configure Webhook**:
   - Paste your Synology Chat webhook URL
   - Click **"Test Webhook"** to verify configuration

3. **Disable Battery Optimization**:
   - Tap **"Disable Battery Optimization"** when prompted
   - This ensures reliable background operation

4. **Verify Status**:
   - Check that all indicators show green checkmarks
   - You should see "App is configured and running in background"

## 📋 Requirements

- **Android**: 10.0 (API level 30) or higher
- **Synology Chat**: Running on your Synology NAS
- **Network**: Phone and NAS must be able to communicate (same network or internet access)
- **Permissions**: SMS reading, notifications, battery optimization exemption

## 🛡️ Privacy & Security

- **No Data Collection**: The app doesn't collect or store any personal data
- **Local Processing**: SMS messages are processed locally on your device
- **Direct Communication**: Messages are sent directly from your phone to your Synology server
- **No Third-Party Services**: No external services or analytics involved

## 🔄 How It Works

```
📱 SMS Received → 📊 App Processes → 🌐 Webhook Request → 💬 Synology Chat
```

1. **SMS Reception**: Android system delivers SMS to the app's broadcast receiver
2. **Processing**: App extracts sender, message content, and timestamp
3. **Formatting**: Creates formatted message with sender info and timestamp
4. **Webhook**: Sends POST request to your Synology Chat webhook
5. **Notification**: Message appears in your configured Synology Chat channel

## 🛠️ Development

### Prerequisites
- Android Studio Hedgehog | 2023.1.1 or later
- JDK 17
- Android SDK with API 36

### Building
```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Run tests
./gradlew test
```

### Project Structure
```
app/src/main/java/com/example/smssynologychat/
├── MainActivity.kt              # Main UI and configuration
├── SmsReceiver.kt              # SMS broadcast receiver
├── SynologyWebhookService.kt   # Webhook communication
├── SmsForwarderService.kt      # Background foreground service
├── BootReceiver.kt             # Auto-start on boot
├── PermissionManager.kt        # Permission handling
└── SettingsDataStore.kt        # Configuration storage
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 Troubleshooting

### SMS Not Being Forwarded

1. **Check Permissions**: Ensure SMS reading permissions are granted
2. **Restricted Settings**: On some devices, enable "Allow Restricted Settings" in Developer Options
3. **Battery Optimization**: Verify battery optimization is disabled for the app
4. **Background Service**: Look for the persistent notification indicating the service is running
5. **Webhook URL**: Test the webhook URL using the built-in test button
6. **Network Connectivity**: Ensure your phone can reach your Synology server

### Webhook Test Fails

1. **URL Format**: Verify the webhook URL is complete and correctly copied
2. **Network Access**: Ensure your phone can reach your Synology server
3. **Firewall**: Check if firewall settings are blocking the connection
4. **SSL Certificates**: If using HTTPS, ensure certificates are valid

### App Stops Working After Reboot

1. **Auto-start**: The app should automatically start after reboot
2. **Permissions**: Re-check that all permissions are still granted
3. **Battery Optimization**: Verify exemption is still active

### Permission Issues on Specific Devices

#### Samsung Devices
- Enable "Allow Restricted Settings" in Developer Options
- Check "Special app access" in Settings → Apps

#### Xiaomi/MIUI
- Enable "Allow Restricted Settings" in Developer Options
- Go to Security → Permissions → Special app access
- Allow "Display pop-up windows while running in background"

#### Huawei/EMUI
- Enable "Allow Restricted Settings" in Developer Options
- Go to Phone Manager → Protected apps and enable the app

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Built with [Android Jetpack Compose](https://developer.android.com/jetpack/compose)
- Uses [OkHttp](https://square.github.io/okhttp/) for HTTP requests
- Material Design components from [Material 3](https://m3.material.io/)

## 📬 Support

- **Issues**: [GitHub Issues](../../issues)
- **Discussions**: [GitHub Discussions](../../discussions)

---

**⚠️ Note**: This app requires SMS reading permissions and is designed for personal use. Make sure you comply with your local laws regarding SMS interception and forwarding.
