package com.example.composewatch.core.designsystem.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.media3.common.Player

@Composable
fun ExtraControls(player: Player,modifier: Modifier = Modifier){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PlayBackSpeedPopUpButton(player)
        ShuffleButton(player)
        RepeatButton(player)
    }
}