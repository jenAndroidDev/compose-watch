package com.example.composewatch.core.designsystem.player

import androidx.annotation.OptIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import com.example.composewatch.R

@OptIn(UnstableApi::class)
@Composable
fun PlayPauseButton(player: Player,modifier: Modifier){
    val state = rememberPlayPauseButtonState(player)
    IconButton(onClick = state::onClick, modifier = modifier, enabled = state.isEnabled) {
      if (state.showPlay)
          Icon(
          painter = painterResource(R.drawable.ic_play_circle),
              contentDescription = ""
        )else{
          Icon(
              painter = painterResource(R.drawable.ic_pause_circle),
              contentDescription = "")
        }
    }

}