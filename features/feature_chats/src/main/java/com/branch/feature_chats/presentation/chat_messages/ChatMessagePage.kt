package com.branch.feature_chats.presentation.chat_messages

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.branch.core_utils.designs.Ascent
import com.branch.core_utils.designs.BranchCustomerAppTheme
import com.branch.feature_chats.domain.models.Message

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChatMessagePage(
    navController: NavController,
    threadId: String,
    userId: String,
    viewModel: ChatMessageVm = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getMessages(threadId)
    }

    val uiState = viewModel.chatMessageUiState.collectAsState()
    val messages = uiState.value.messages
    BranchCustomerAppTheme {
        Scaffold(backgroundColor = MaterialTheme.colors.background, topBar = {
            TopAppBar(backgroundColor = Color.Transparent, elevation = 0.dp, title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .height(40.dp)
                            .width(40.dp)
                            .clip(CircleShape)
                            .background(Ascent.copy(alpha = 0.6f))
                            .fillMaxHeight()
                    ) {
                        Text(
                            text = userId.get(0).toString(),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 20.sp, color = MaterialTheme.colors.onSurface
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = userId)
                }
            }, navigationIcon = {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = "arrow_back",
                    modifier = Modifier.clickable {
                        navController.navigateUp()
                    })
            })
        }, content = {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                if (uiState.value.error != null && uiState.value.messages.isEmpty()) Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = uiState.value.error ?: "", textAlign = TextAlign.Center)
                }

                if (messages.isNotEmpty()) LazyColumn(
                    contentPadding = PaddingValues(10.dp),
                    verticalArrangement = Arrangement.spacedBy(26.dp),
                ) {
                    items(items = messages, key = { messages -> messages.id!! }) { msg ->
                        if (msg.agentId == null) CustomerChat(msg)
                        else {
                            AgentChat(msg = msg)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(50.dp))
            }
        }, bottomBar = {
            Box(modifier = Modifier.background(Color.White)) {
                Row {
                    var value by rememberSaveable { mutableStateOf("") }
                    TextField(
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(5.dp),
                        value = value,
                        label = { Text(text = "Type Something....") },
                        onValueChange = {
                            value = it
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "send_message"
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            backgroundColor = Ascent.copy(alpha = 0.2f),
                            disabledLabelColor = Ascent.copy(alpha = 0.8f)
                        )

                    )
                }
            }
        })
    }
}

@Composable
fun AgentChat(msg: Message) {
    Row(
        horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .width(5.dp)
                .weight(1f)
        )
        Column {
            Text(
                text = msg.message.toString(),
                modifier = Modifier
                    .background(Ascent)
                    .padding(10.dp),
                style = MaterialTheme.typography.h2.copy(
                    fontSize = 14.sp, color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = msg.timeStamp.toString(), style = MaterialTheme.typography.h3.copy(
                    fontSize = 11.sp, color = Color.Gray
                )
            )
        }

        Spacer(modifier = Modifier.width(5.dp))
    }
}

@Composable
fun CustomerChat(msg: Message) {
    Row {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(30.dp)
                .width(30.dp)
                .clip(CircleShape)
                .background(Ascent.copy(alpha = 0.7f))
                .fillMaxHeight()
        ) {
            Text(
                text = msg.userId?.get(0).toString(),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 20.sp, color = MaterialTheme.colors.onSurface
                )
            )
        }

        Spacer(modifier = Modifier.width(5.dp))

        Column {
            Text(
                text = msg.message ?: "",
                modifier = Modifier
                    .background(Color.LightGray.copy(alpha = 0.2f))
                    .padding(10.dp),
                style = MaterialTheme.typography.h2.copy(
                    fontSize = 14.sp, color = MaterialTheme.colors.onSurface
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                modifier = Modifier.align(Alignment.End),
                text = msg.timeStamp.toString(), style = MaterialTheme.typography.h3.copy(
                    fontSize = 11.sp, color = Color.Gray
                )
            )

        }
    }
}

@Preview
@Composable
fun PreviewChatMessage() {
    // ChatMessagePage()
}