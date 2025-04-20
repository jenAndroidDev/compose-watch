package com.example.composewatch.core.designsystem.player

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import kotlinx.coroutines.delay

@Composable
fun ComposeWatchSeekBar(player: Player,modifier: Modifier){
    var currentPosition by rememberSaveable  { mutableLongStateOf(player.currentPosition) }
    var bufferedPosition by rememberSaveable{ mutableLongStateOf(player.bufferedPosition) }
    var duration by rememberSaveable { mutableLongStateOf(0L) }


    Log.d("Watch", "ComposeWatchSeekBar() called with: player = $currentPosition,$bufferedPosition,$duration, modifier = $modifier")
    LaunchedEffect(player) {
        while (true) {
            if (player.isPlaying || player.playbackState == Player.STATE_BUFFERING) {
                currentPosition = player.currentPosition
                bufferedPosition = player.bufferedPosition
                duration = player.duration.takeIf { it > 0 } ?: 1L
            }
            delay(1000)
        }

    }
    Slider(
        value = currentPosition.toFloat(),
        onValueChange = {
            player.seekTo(it.toLong())
        },
        valueRange = 0f..duration.toFloat(),
        modifier = modifier.fillMaxWidth()
            .height(12.dp),
        colors = SliderDefaults.colors(
            thumbColor = Color.Red,
            activeTrackColor = Color.Red,
            inactiveTrackColor = Color.LightGray
        )
    )
}