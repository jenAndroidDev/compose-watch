package com.example.composewatch.core.designsystem.player

import androidx.annotation.OptIn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberNextButtonState
import com.example.composewatch.R

@OptIn(UnstableApi::class)
@Composable
fun NextButton(player: Player,modifier: Modifier = Modifier){
    val state = rememberNextButtonState(player)
    IconButton(onClick = state::onClick, modifier = modifier, enabled = state.isEnabled) {
        Icon(painter = painterResource(R.drawable.ic_skip_next),
            contentDescription = "Next Button")
    }
}