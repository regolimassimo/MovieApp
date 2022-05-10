package com.massimoregoli.movieapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieResult(val Title: String,
var Year: String,
var Rated: String,
var Released: String,
var Genre: String,
var Director: String,
var Write: String,
var Actors: String,
var Plot: String,
var Awards: String,
var Poster: String,
var imdbID: String
) : Parcelable {
    constructor() : this("", "", "", "",
        "", "", "", "", "",
        "", "", "")
}