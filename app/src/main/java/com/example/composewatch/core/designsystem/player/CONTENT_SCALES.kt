package com.example.composewatch.core.designsystem.player

import androidx.compose.ui.layout.ContentScale

val CONTENT_SCALES =
  listOf(
    "Fit" to ContentScale.Fit,
    "Crop" to ContentScale.Crop,
    "None" to ContentScale.None,
    "Inside" to ContentScale.Inside,
    "FillBounds" to ContentScale.FillBounds,
    "FillHeight" to ContentScale.FillHeight,
    "FillWidth" to ContentScale.FillWidth,
  )