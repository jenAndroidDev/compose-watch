package com.example.composewatch.core.designsystem.player

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberPreviousButtonState
import com.example.composewatch.R

@OptIn(UnstableApi::class)
@Composable
internal fun PreviousButton(player: Player,modifier: Modifier){
    val state = rememberPreviousButtonState(player)
    IconButton(onClick = state::onClick, modifier = modifier, enabled = state.isEnabled) {
        Icon(painter = painterResource(R.drawable.ic_previous),
            contentDescription = "Skip Previous",
            modifier = modifier.padding(24.dp))
    }

}