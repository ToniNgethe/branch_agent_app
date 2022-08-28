package com.branch.customerservice.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.branch.core_utils.designs.Ascent
import com.branch.core_utils.designs.BranchCustomerAppTheme
import com.branch.core_utils.navigation.UiEvent

@Composable
fun SplashPage(modifier: Modifier = Modifier, onNavigate: (UiEvent) -> Unit) {
    BranchCustomerAppTheme {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {

            Image(
                painter = painterResource(id = com.branch.core_resources.R.drawable.ic_logo),
                contentDescription = "logo",
                modifier = Modifier
                    .height(70.dp)
                    .width(70.dp)
                    .padding(6.dp),
                colorFilter = ColorFilter.tint(Ascent)
            )
            Text(
                text = stringResource(id = com.branch.core_resources.R.string.app_name),
                style = MaterialTheme.typography.h2.copy(color = Ascent)
            )
        }
    }
}


@Preview
@Composable
fun SplashPagePreview() {
    SplashPage(onNavigate = {

    })
}