package com.example.smssynologychat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smssynologychat.data.AppDatabase
import com.example.smssynologychat.data.SmsMessageEntity
import com.example.smssynologychat.data.SmsStatus
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageStatusScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var messages by remember { mutableStateOf<List<SmsMessageEntity>>(emptyList()) }

    LaunchedEffect(Unit) {
        val db = AppDatabase.getInstance(context)
        val dao = db.smsMessageDao()
        messages = dao.getMessagesByStatus(SmsStatus.PENDING) +
                   dao.getMessagesByStatus(SmsStatus.FAILED) +
                   dao.getMessagesByStatus(SmsStatus.SENT)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("SMS Message Status") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("← Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { msg ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "From: ${msg.sender}",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "Message: ${msg.content}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Time: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(msg.timestamp))}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Status: ${msg.status}",
                            style = MaterialTheme.typography.bodySmall,
                            color = when (msg.status) {
                                SmsStatus.SENT -> MaterialTheme.colorScheme.primary
                                SmsStatus.FAILED -> MaterialTheme.colorScheme.error
                                SmsStatus.PENDING -> MaterialTheme.colorScheme.secondary
                            }
                        )
                    }
                }
            }
        }
    }
}
