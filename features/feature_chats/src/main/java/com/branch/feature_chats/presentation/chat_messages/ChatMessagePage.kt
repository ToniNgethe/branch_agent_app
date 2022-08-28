package com.branch.customerservice.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.branch.core_utils.designs.Ascent
import com.branch.core_utils.designs.BranchCustomerAppTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChatMessagePage() {
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
                            text = "J", textAlign = TextAlign.Center, style = TextStyle(
                                fontSize = 20.sp, color = MaterialTheme.colors.onSurface
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "John Does")
                }
            }, navigationIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowBack, contentDescription = "arrow_back"
                )
            })
        }, content = {
            Column {
                LazyColumn(
                    contentPadding = PaddingValues(10.dp),
                    verticalArrangement = Arrangement.spacedBy(26.dp),
                ) {
                    items(10) { index ->
                        if (index % 2 == 0) Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
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
                                    text = "J", textAlign = TextAlign.Center, style = TextStyle(
                                        fontSize = 20.sp, color = MaterialTheme.colors.onSurface
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.width(5.dp))

                            Text(
                                text = "Message one goes here. Thank  you for resolving this issue",
                                modifier = Modifier
                                    .background(Color.LightGray.copy(alpha = 0.2f))
                                    .padding(10.dp)
                                    .weight(1f),
                                style = MaterialTheme.typography.h2.copy(
                                    fontSize = 14.sp, color = MaterialTheme.colors.onSurface
                                )
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "10th Jun, 2022", style = MaterialTheme.typography.h3.copy(
                                    fontSize = 12.sp, color = Color.Gray
                                )
                            )

                        }
                        else {
                            Row(
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .width(5.dp)
                                        .weight(1f)
                                )
                                Text(
                                    text = "10th Jun, 2022",
                                    style = MaterialTheme.typography.h3.copy(
                                        fontSize = 12.sp, color = Color.Gray
                                    )
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "Message one goes here.Message one goes",
                                    modifier = Modifier
                                        .background(Ascent)
                                        .padding(10.dp),
                                    style = MaterialTheme.typography.h2.copy(
                                        fontSize = 14.sp, color = Color.White
                                    )
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                            }
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
                        onValueChange = { it ->
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

@Preview
@Composable
fun PreviewChatMessage() {
    ChatMessagePage()
}