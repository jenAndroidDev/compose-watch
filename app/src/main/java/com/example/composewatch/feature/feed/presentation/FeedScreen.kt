package com.example.composewatch.feature.feed.presentation


import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.Player
import com.example.composewatch.core.player.ComposeWatchPlayerFactory

@Composable
fun FeedScreen(modifier: Modifier){
    val context = LocalContext.current
    var player by remember { mutableStateOf<Player?>(null) }

    if (Build.VERSION.SDK_INT>23){
        LifecycleStartEffect(Unit) {
            player = ComposeWatchPlayerFactory.createPlayer(context,"")
            onStopOrDispose {
                player?.apply { release() }
                player = null
            }
        }
    }else{
        LifecycleResumeEffect(Unit) {
            player = ComposeWatchPlayerFactory.createPlayer(context,"")
            onPauseOrDispose {
                player?.apply { release() }
                player = null
            }
        }
    }

}

@Composable
fun MediaPlayer(modifier: Modifier){

    var showControls by remember { mutableStateOf(true) }
    var currentContentScaleIndex by remember { mutableIntStateOf(0) }



}