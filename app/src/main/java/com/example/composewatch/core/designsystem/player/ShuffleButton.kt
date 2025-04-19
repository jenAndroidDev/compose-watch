package com.example.composewatch.core.designsystem.player

import androidx.annotation.OptIn
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberShuffleButtonState
import com.example.composewatch.R

@OptIn(UnstableApi::class)
@Composable
fun ShuffleButton(player:Player,modifier: Modifier = Modifier){
    val state = rememberShuffleButtonState(player)
    val icon = if (state.shuffleOn) {
        Icon(painter = painterResource(R.drawable.ic_shuffle_on),
            contentDescription = "Shuffle Icon")
    }else{
        Icon(painter = painterResource(R.drawable.ic_shuffle),
            contentDescription = "Shuffle Icon Off")
    }
    IconButton(onClick = state::onClick, enabled = state.isEnabled) {
        icon
    }
}