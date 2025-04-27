package com.example.composewatch.core.designsystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ComposeWatchProgressBar(modifier: Modifier = Modifier){
    Box(modifier = modifier,
        contentAlignment = Alignment.Center){
        CircularProgressIndicator(
            modifier = modifier.size(24.dp),
            trackColor = Color.White,
            color = Color.Red
        )
    }

}