package com.example.smssynologychat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smssynologychat.ui.theme.SMSSynologyChatTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SMSSynologyChatTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SmsForwarderApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmsForwarderApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var webhookUrl by remember { mutableStateOf("") }
    var hasPermissions by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var isTesting by remember { mutableStateOf(false) }
    var testResult by remember { mutableStateOf<String?>(null) }
    var showTestResult by remember { mutableStateOf(false) }

    val settingsStore = remember { SettingsDataStore(context) }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasPermissions = permissions.all { it.value }
    }

    // Load initial data
    LaunchedEffect(Unit) {
        webhookUrl = settingsStore.webhookUrl.first()
        hasPermissions = PermissionManager.hasAllSmsPermissions(context)
        isLoading = false
    }

    // Test webhook function
    fun testWebhook() {
        isTesting = true
        showTestResult = false
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            val result = SynologyWebhookService.testWebhook(context)
            kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                isTesting = false
                testResult = if (result.isSuccess) {
                    result.getOrNull()
                } else {
                    "Test failed: ${result.exceptionOrNull()?.message}"
                }
                showTestResult = true
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "SMS to Synology Chat Forwarder",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        // Permission section
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "SMS Permissions",
                    style = MaterialTheme.typography.titleMedium
                )

                if (hasPermissions) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("✅ SMS permissions granted")
                    }
                } else {
                    Text(
                        text = "SMS permissions are required to read incoming messages",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )

                    Button(
                        onClick = {
                            permissionLauncher.launch(PermissionManager.getSmsPermissions())
                        }
                    ) {
                        Text("Grant Permissions")
                    }
                }
            }
        }

        // Webhook configuration section
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Synology Chat Webhook",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = webhookUrl,
                    onValueChange = { webhookUrl = it },
                    label = { Text("Webhook URL") },
                    placeholder = { Text("https://your-synology:5000/webapi/entry.cgi/SYNO.Chat.External/chatbot/...") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                                settingsStore.saveWebhookUrl(webhookUrl)
                            }
                        },
                        enabled = webhookUrl.isNotBlank(),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save URL")
                    }

                    Button(
                        onClick = { testWebhook() },
                        enabled = webhookUrl.isNotBlank() && !isTesting,
                        modifier = Modifier.weight(1f)
                    ) {
                        if (isTesting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Test Webhook")
                        }
                    }
                }

                // Test result display
                if (showTestResult && testResult != null) {
                    Card(
                        colors = if (testResult!!.contains("successfully")) {
                            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        } else {
                            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = testResult!!,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        // Status section
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Status",
                    style = MaterialTheme.typography.titleMedium
                )

                val isConfigured = hasPermissions && webhookUrl.isNotBlank()

                if (isConfigured) {
                    Text(
                        text = "✅ App is configured and ready to forward SMS messages",
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Text(
                        text = "⚠️ Please complete the configuration above",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        // Instructions
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "How to get Synology Chat Webhook URL:",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "1. Open Synology Chat\n" +
                            "2. Go to the channel where you want to receive SMS\n" +
                            "3. Click the gear icon → Integrations\n" +
                            "4. Add Incoming Webhook\n" +
                            "5. Copy the webhook URL and paste it above",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SmsForwarderAppPreview() {
    SMSSynologyChatTheme {
        SmsForwarderApp()
    }
}