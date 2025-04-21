package com.example.composewatch.core.designsystem.player

import android.graphics.Color
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player

@Composable
fun MinimalControls(player: Player,modifier: Modifier){
    val graySemiTransparentBackground = androidx.compose.ui.graphics.Color.Gray.copy(alpha = 0.5f)

    val modifierForIconButton = modifier.size(60.dp).background(graySemiTransparentBackground, shape = CircleShape)


          Row(
              modifier = modifier.fillMaxWidth(),
              verticalAlignment = Alignment.Bottom,
              horizontalArrangement = Arrangement.SpaceEvenly
          ) {
              PreviousButton(player, modifierForIconButton)
              PlayPauseButton(player, modifierForIconButton)
              NextButton(player, modifierForIconButton)
          }
      }
