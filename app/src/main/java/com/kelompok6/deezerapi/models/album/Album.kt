package com.kelompok6.deezerapi.models.album

data class Album(
    val `data`: List<Data>,
    val next: String,
    val total: Int
)