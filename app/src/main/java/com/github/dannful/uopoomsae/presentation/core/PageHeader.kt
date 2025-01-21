package com.github.dannful.uopoomsae.presentation.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.dannful.uopoomsae.R
import com.github.dannful.uopoomsae.ui.theme.LocalSpacing

@Composable
fun PageHeader(
    modifier: Modifier = Modifier,
    title: String = "",
    bottomBar: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(bottomBar = bottomBar, topBar = {
        Surface(tonalElevation = 1.dp, shadowElevation = 1.dp) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                    .fillMaxHeight(0.1f)
                    .fillMaxWidth()
                    .padding(
                        LocalSpacing.current.small
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Image(
                    painter = painterResource(id = R.drawable.c_farpoomsae),
                    contentDescription = null
                )
            }
        }
    }) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .then(modifier)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(LocalSpacing.current.small)
                    .align(Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.small)
            ) {
                content()
            }
        }
    }
}