package com.example.composewatch.feature.feed.presentation


import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import android.view.OrientationEventListener
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.example.composewatch.core.designsystem.player.MinimalControls
import com.example.composewatch.core.player.ComposeWatchPlayerFactory
import com.example.composewatch.core.player.utils.MediaSource
import com.example.composewatch.ui.theme.PurpleGrey40
import com.google.android.gms.location.DeviceOrientation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

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
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val videoModifier = if (isLandscape) {
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    } else {
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .background(Color.Gray)
    }
    Column(modifier = modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Top) {
        player?.let {
            MediaPlayer(player = it,
                modifier = videoModifier
                    .background(color = Color.Gray)
                    .fillMaxSize()
            )
        }

    }
}

@OptIn(UnstableApi::class)
@Composable
fun MediaPlayer(
    modifier: Modifier,
    player: Player){

    var showControls by remember { mutableStateOf(true) }
    var currentContentScaleIndex by remember { mutableIntStateOf(0) }
    val contentScale = CONTENT_SCALES[currentContentScaleIndex].second


    val presentationState = rememberPresentationState(player = player)
    val scaleModifier = Modifier.resizeWithContentScale(contentScale,presentationState.videoSizeDp)
    val interactionSource = remember { MutableInteractionSource() }
    var playBackStateBuffering = remember { mutableStateOf(false) }


    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect{interaction->
            when(interaction){
                is PressInteraction.Press->{
                    delay(100)
                    showControls = false
                    player.pause()
                }
                is PressInteraction.Release->{
                    delay(100)
                    showControls = true
                    player.play()
                }
            }
        }
    }

    Box(
        modifier = modifier
            .clickable(
                onClick = {},
                interactionSource = interactionSource,
                indication = ripple()
            ),
        contentAlignment = Alignment.TopStart
        ) {

        PlayerSurface(
            player = player,
            surfaceType = SURFACE_TYPE_SURFACE_VIEW,
            modifier = scaleModifier.align(Alignment.TopCenter),
            )

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
        Box(modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter){
            ComposeWatchSeekBar(player = player)
        }
        Button(
            onClick = { currentContentScaleIndex = currentContentScaleIndex.inc() % CONTENT_SCALES.size },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp),
        ) {
            Text("ContentScale is ${CONTENT_SCALES[currentContentScaleIndex].first}")
        }
    }




//    val context = LocalContext.current
//    OrientationEffect(modifier = modifier, player = player, context = context)

}

@OptIn(UnstableApi::class)
@Composable
private fun OrientationEffect(
    modifier: Modifier,
    player: Player,
    context: Context){

    var orientation by remember { mutableIntStateOf(Configuration.ORIENTATION_PORTRAIT) }
    val configuration = LocalConfiguration.current
    val screenHeight by remember {   mutableStateOf( configuration.screenHeightDp.dp)}
    val screenWidth by remember {  mutableStateOf(configuration.screenWidthDp.dp)}
    Log.d(
        "Resize",
        "OrientationEffect()...$screenWidth,$screenHeight"
    )
    LaunchedEffect(configuration) {
        snapshotFlow { configuration.orientation }
            .collectLatest {
                player.pause()
                orientation = it
            }
    }
    when (orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            Log.d(
                "Resize",
                "OrientationEffect() Landscape"
            )
            if (!player.isPlaying){
                player.playWhenReady=true
                player.videoSize
            }
            with(modifier){
                height(screenHeight)
                width(screenWidth)
            }
        }
        Configuration.ORIENTATION_PORTRAIT->{
            Log.d(
                "Resize",
                "OrientationEffect() called with Portrait"
            )
            with(modifier) {
                fillMaxWidth(1f)
                //aspectRatio(portrait_aspect_ratio)
            }
            if (!player.isPlaying)player.play()
        }
        else -> {
        }
    }
}

private val portrait_aspect_ratio = 9f/16f
private val landscape_aspect_ratio= 16f/9f
