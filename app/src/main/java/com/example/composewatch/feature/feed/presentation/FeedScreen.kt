package com.example.composewatch.feature.feed.presentation


import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Looper
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.AudioAttributes
import androidx.media3.common.DeviceInfo
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.TrackSelectionParameters
import androidx.media3.common.Tracks
import androidx.media3.common.VideoSize
import androidx.media3.common.text.CueGroup
import androidx.media3.common.util.Size
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import androidx.media3.ui.compose.modifiers.resizeWithContentScale
import androidx.media3.ui.compose.state.rememberPresentationState
import com.example.composewatch.core.designsystem.ComposeWatchProgressBar
import com.example.composewatch.core.designsystem.player.CONTENT_SCALES
import com.example.composewatch.core.designsystem.player.ComposeWatchSeekBar
import com.example.composewatch.core.designsystem.player.MinimalControls
import com.example.composewatch.core.player.ComposeWatchPlayerFactory
import com.example.composewatch.core.player.utils.MediaSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import java.util.logging.LogManager

private val url = "https://storage.googleapis.com/exoplayer-test-media-0/shortform_2.mp4"
private const val hlsUrl ="https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8"

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

    val mediaSource = MediaSource.fromUrl(hlsUrl, context)
    if (Build.VERSION.SDK_INT>23){
        LifecycleStartEffect(Unit) {
            player = ComposeWatchPlayerFactory.createPlayer(context)

            player?.run {
                setMediaItem(mediaSource.mediaItem)
                prepare()
            }
            Log.d("Feed Screen", "$player")
            onStopOrDispose {
                Log.d("Feed Screen", "player Stooped")
                player?.apply { release() }
                player = null
            }
        }
    }else{
        LifecycleResumeEffect(Unit) {
            player = ComposeWatchPlayerFactory.createPlayer(context)
            player!!.prepare()
            player?.run {
                setMediaItem(mediaSource.mediaItem)
                prepare()
            }
            onPauseOrDispose {
                Log.d("Feed Screen", "Player Instance..$player")
                player?.apply { release() }
                player = null
            }
        }
    }

    player?.let {
        MediaPlayer(player = it,
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f))
    }
}

@OptIn(UnstableApi::class)
@Composable
fun MediaPlayer(
    modifier: Modifier,
    player: Player){

    var showControls by remember { mutableStateOf(true) }
    val currentContentScaleIndex by remember { mutableIntStateOf(0) }
    val contentScale = CONTENT_SCALES[currentContentScaleIndex].second


    val presentationState = rememberPresentationState(player = player)
    val scaleModifier = Modifier.resizeWithContentScale(contentScale,presentationState.videoSizeDp)
    val interactionSource = remember { MutableInteractionSource() }
    var playBackStateBuffering = remember { mutableStateOf(false) }


    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect{interaction->
            when(interaction){
                is PressInteraction.Press->{
                    delay(200)
                    showControls = false
                }
                is PressInteraction.Release->{
                    delay(200)
                    showControls = true
                }
            }
        }
    }

    Box(
        modifier
            .aspectRatio(16f / 9f)
            .clickable(
                onClick = {},
                interactionSource = interactionSource,
                indication = ripple()
            ),
        ) {

        PlayerSurface(
            player = player,
            surfaceType = SURFACE_TYPE_SURFACE_VIEW,
            modifier = scaleModifier,
            )
//        val playerListener = object:Player.Listener{
//            override fun onPlaybackStateChanged(playbackState: Int) {
//                when(playbackState){
//                    Player.STATE_READY->{
//                        playBackStateBuffering.value = false
//                    }
//                    Player.STATE_BUFFERING->{
//                       playBackStateBuffering.value=true
//                    }
//                    else->{}
//                }
//
//            }
//        }
//        player.addListener(playerListener)

        if (presentationState.coverSurface){
            Box(modifier = modifier
                .matchParentSize()
                .background(Color.Black))
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = showControls,
                enter = slideInVertically(
                    initialOffsetY = { it / 2 }
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it / 2 }
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MinimalControls(player, Modifier)
                }
            }
        }
    }
    ComposeWatchSeekBar(player = player, modifier = modifier)
    val context = LocalContext.current
    OrientationEffect(modifier = scaleModifier, player = player, context = context)

}

@OptIn(UnstableApi::class)
@Composable
private fun OrientationEffect(
    modifier: Modifier,
    player: Player,
    context: Context){

    var orientation by remember { mutableIntStateOf(Configuration.ORIENTATION_PORTRAIT) }
    val configuration = LocalConfiguration.current
    LaunchedEffect(configuration) {
        snapshotFlow { configuration.orientation }
            .collectLatest {
                player.pause()
                Log.d("Orientation Effect", "OrientationEffect() called..$it")
                orientation = it
            }
    }
    when (orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            if (!player.isPlaying){
                Log.d(
                    "OrientationChanging",
                    "OrientationEffect() called with:" +
                            " modifier..${player.isPlaying}"+"" +
                            "$player")

                player.playWhenReady=true
            }
            modifier.fillMaxSize()
        }
        Configuration.ORIENTATION_PORTRAIT->{
            modifier.aspectRatio(16f/9f)
            if (!player.isPlaying)player.play()
        }
        else -> {
        }
    }
}
