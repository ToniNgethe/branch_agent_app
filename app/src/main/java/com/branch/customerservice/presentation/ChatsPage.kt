package com.branch.customerservice.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.branch.core_utils.designs.Ascent
import com.branch.core_utils.designs.BranchCustomerAppTheme

@Composable
fun ChatsPage(modifier: Modifier = Modifier) {
    BranchCustomerAppTheme {
        Box {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .fillMaxSize()
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = stringResource(com.branch.core_resources.R.string.chat),
                    style = MaterialTheme.typography.h1.copy(fontSize = 30.sp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(10.dp)
                ) {
                    items(15) { index ->
                        ChatsItem(
                            modifier = modifier,
                            sender = "John Does",
                            message = "Hi there, i have a query regarding my message Hi there, i have a query regarding my message",
                            time = "3rd, Jun 2020 "
                        )
                    }
                }

            }
        }
    }
}


@Composable
fun ChatsItem(modifier: Modifier, sender: String, message: String, time: String) {
    Row {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Ascent)
                .fillMaxHeight()
        ) {
            Text(
                text = sender[0].toString(), textAlign = TextAlign.Center, style = TextStyle(
                    fontSize = 20.sp, color = MaterialTheme.colors.onSurface
                )
            )
        }
        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = modifier.fillMaxWidth()) {
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = sender,
                    style = MaterialTheme.typography.h2.copy(color = MaterialTheme.colors.onSurface)
                )
                Text(
                    text = time, style = MaterialTheme.typography.h3.copy(
                        color = Color.LightGray, fontSize = 14.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = message,
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
    ChatsPage()
}