package com.example.composewatch.core.designsystem.player

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderDefaults.Thumb
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import com.example.composewatch.R
import com.example.composewatch.core.player.ComposeWatchPlayerFactory
import com.example.composewatch.ui.theme.Grey20
import com.example.composewatch.ui.theme.NetflixRed
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
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
    val valueRange = (0f..duration.toFloat())
    Box(modifier = modifier,
        contentAlignment = Alignment.BottomCenter) {
        Slider(
            value = currentPosition.toFloat(),
            onValueChange = {
                player.seekTo(it.toLong())
            },
            valueRange = valueRange,
            modifier = modifier.fillMaxWidth()
                .heightIn(2.dp)
            ,
            colors = SliderDefaults.colors(
                thumbColor = Color.Transparent,
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent
            ),
                track = { sliderState ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .background(Grey20)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(sliderState.value / valueRange.endInclusive)
                                .height(4.dp)
                                .background(NetflixRed)
                        )
                    }
            },
            thumb = {
                Thumb(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color.White, shape = CircleShape),
                    interactionSource = remember { MutableInteractionSource() },
                    colors = SliderDefaults.colors(thumbColor = Color.White)
                )
            }
        )
    }
}

@Preview
@Composable
private fun PreviewSeekBar(){
    val content = LocalContext.current
    val player = ComposeWatchPlayerFactory.createPlayer(content)
    ComposeWatchSeekBar(player, modifier = Modifier)
}