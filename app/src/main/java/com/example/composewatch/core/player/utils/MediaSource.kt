package com.example.composewatch.core.player.utils

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.rtsp.RtspMediaSource
import androidx.media3.exoplayer.smoothstreaming.SsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource

@OptIn(UnstableApi::class)
/*
* Returns The Media Source Based on the Url Provided*/
sealed class MediaSource{
    companion object{
        fun fromUrl(url:String,context: Context): MediaSource {
            val dataSource = DefaultDataSource.Factory(context)
           return when(Util.inferContentType(Uri.parse(url))){
                C.CONTENT_TYPE_SS->{
                    SsMediaSource.Factory(dataSource)
                        .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
                }
                C.CONTENT_TYPE_HLS->{
                    HlsMediaSource.Factory(dataSource)
                        .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
                }
                C.CONTENT_TYPE_DASH->{
                    DashMediaSource.Factory(dataSource)
                        .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
                }
                C.CONTENT_TYPE_RTSP->{
                    RtspMediaSource.Factory()
                        .createMediaSource(MediaItem.fromUri((Uri.parse(url))))
                }
                else->{
                    ProgressiveMediaSource.Factory(dataSource)
                        .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
                }
            }
        }
    }
}

//@OptIn(UnstableApi::class)
//sealed class MediaItem{
//    companion object{
//
//        fun fromMediaSource(url:String,
//                            context: Context,
//                            shouldLoadAds:Boolean){
//
//            when{
//                shouldLoadAds->{
//                    val mediaSource = MediaSourceFactory.fromUrl(url,context)
//                    mediaSource.mediaItem.
//                    val mediaItem = MediaItem.Builder(
//                    )
//                        .setUri(mediaSource.mediaItem)
//                }
//                else->{
//
//                }
//            }
//        }
//    }
//}