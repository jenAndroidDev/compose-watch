package com.example.composewatch.feature.feed.presentation


import android.os.Build
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import androidx.media3.ui.compose.modifiers.resizeWithContentScale
import androidx.media3.ui.compose.state.rememberPresentationState
import com.example.composewatch.core.designsystem.player.CONTENT_SCALES
import com.example.composewatch.core.designsystem.player.ComposeWatchSeekBar
import com.example.composewatch.core.designsystem.player.ExtraControls
import com.example.composewatch.core.designsystem.player.MinimalControls
import com.example.composewatch.core.designsystem.player.noRippleClickable
import com.example.composewatch.core.player.ComposeWatchPlayerFactory
import com.example.composewatch.core.player.utils.MediaSource

private val url = "https://storage.googleapis.com/exoplayer-test-media-0/shortform_2.mp4"
private val hlsUrl ="https://devstreaming-cdn.apple.com/videos/streaming/examples/adv_dv_atmos/main.m3u8"

/*1.Fix the Control Ui
* 2.Add Seek Bar
* 3.Handle Rotation
* 4.Add media items as youtube
* 5.Click and Change the Media Item*/
@OptIn(UnstableApi::class)
@Composable
fun FeedScreen(modifier: Modifier){
    val context = LocalContext.current
    var player by rememberSaveable  { mutableStateOf<Player?>(null) }

    if (Build.VERSION.SDK_INT>23){
        LifecycleStartEffect(Unit) {
            player = ComposeWatchPlayerFactory.createPlayer(context)
            val mediaSource = MediaSource.fromUrl(hlsUrl, context)
            player?.run {
                setMediaItem(mediaSource.mediaItem)
                prepare()
            }
            onStopOrDispose {
                player?.apply { release() }
                player = null
            }
        }
    }else{
        LifecycleResumeEffect(Unit) {
            player = ComposeWatchPlayerFactory.createPlayer(context)
            player!!.prepare()
            onPauseOrDispose {
                player?.apply { release() }
                player = null
            }
        }
    }

    player?.let {
        MediaPlayer(player = it,
            modifier = modifier.fillMaxWidth()
                .fillMaxHeight(0.3f))
    }
}

@OptIn(UnstableApi::class)
@Composable
fun MediaPlayer(
    modifier: Modifier,
    player: Player){

    val showControls by remember { mutableStateOf(true) }
    val currentContentScaleIndex by remember { mutableIntStateOf(0) }
    val contentScale = CONTENT_SCALES[currentContentScaleIndex].second

    val presentationState = rememberPresentationState(player = player)
    val scaleModifier = Modifier.resizeWithContentScale(contentScale,presentationState.videoSizeDp)

    Box(
        modifier.aspectRatio(16f/9f),
        contentAlignment = Alignment.TopCenter) {

        PlayerSurface(
            player = player,
            surfaceType = SURFACE_TYPE_SURFACE_VIEW,
            modifier = scaleModifier.noRippleClickable { showControls!=showControls },

        )
        if (presentationState.coverSurface){
            Box(modifier = modifier.matchParentSize().background(Color.Black))
        }
        if (showControls){
            MinimalControls(player, Modifier.align(Alignment.Center))
//            ExtraControls(
//                player,
//                Modifier.fillMaxWidth()
//                    .align(Alignment.BottomCenter)
//                    .background(Color.Gray.copy(alpha = 0.4f))
//                    .navigationBarsPadding(),
//            )
        }
    }
    ComposeWatchSeekBar(player = player, modifier = modifier)
}