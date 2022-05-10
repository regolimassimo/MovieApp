package com.massimoregoli.movieapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResult(
    var imdbID: String,
    var Type: String,
    val Poster: String,
    var Title: String,
    var Year: String
) : Parcelable {
    constructor() : this("", "", "", "", "")
}
