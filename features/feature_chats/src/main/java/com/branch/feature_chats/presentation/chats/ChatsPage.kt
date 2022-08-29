package com.branch.feature_chats.presentation.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.branch.core_utils.designs.Ascent
import com.branch.core_utils.designs.BranchCustomerAppTheme
import com.branch.core_utils.navigation.UiEvent
import com.branch.feature_chats.domain.models.Chat

@Composable
fun ChatsPage(
    modifier: Modifier = Modifier,
    viewModel: ChatVm = hiltViewModel(),
    onNavigate: (UiEvent.OnNavigate) -> Unit
) {
    val chatUiState = viewModel.chatUiState.collectAsState()
    BranchCustomerAppTheme {
        Box {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .fillMaxSize()
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 10.dp, top = 16.dp, start = 10.dp),
                    text = stringResource(com.branch.core_resources.R.string.chat),
                    style = MaterialTheme.typography.h1.copy(fontSize = 30.sp)
                )

                Divider(thickness = 1.dp)
                Spacer(modifier = Modifier.height(10.dp))

                // loading
                if (chatUiState.value.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)
                        )
                    }
                }

                if (chatUiState.value.errorMessage != null && chatUiState.value.chats.isEmpty()) Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = chatUiState.value.errorMessage ?: "")
                }

                if (chatUiState.value.chats.isNotEmpty()) {
                    val chats = chatUiState.value.chats
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(40.dp),
                        contentPadding = PaddingValues(10.dp)
                    ) {
                        items(items = chats, key = { listItem -> listItem.id }) { chat ->
                            ChatsItem(
                                modifier = modifier, chat
                            ) {
                                onNavigate.invoke(
                                    UiEvent.OnNavigate(
                                        "chat_messages_page?threadId=${it.threadId.toString()}&&user=${it.userId}&&agent=${it.agentId}"
                                    )
                                )
                            }
                        }
                    }
                }


            }
        }
    }
}


@Composable
fun ChatsItem(modifier: Modifier, chat: Chat, onItemClick: (Chat) -> Unit) {
    Row(modifier = modifier.clickable { onItemClick.invoke(chat) }) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Ascent)
                .fillMaxHeight()
        ) {
            chat.userId?.get(0)?.let {
                Text(
                    text = it.toString(), textAlign = TextAlign.Center, style = TextStyle(
                        fontSize = 20.sp, color = MaterialTheme.colors.onSurface
                    )
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = modifier.fillMaxWidth()) {
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = chat.userId ?: "",
                    style = MaterialTheme.typography.h2.copy(color = MaterialTheme.colors.onSurface)
                )
                Text(
                    text = chat.timeStamp ?: "", style = MaterialTheme.typography.h3.copy(
                        color = Color.LightGray, fontSize = 14.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = chat.latestMessage ?: "",
                style = MaterialTheme.typography.h3.copy(color = Color.Gray, fontSize = 14.sp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Preview
@Composable
fun PreviewChatPage() {
    ChatsPage() {

    }
}