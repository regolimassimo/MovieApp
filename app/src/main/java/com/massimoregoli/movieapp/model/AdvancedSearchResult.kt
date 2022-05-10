package com.massimoregoli.movieapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdvancedSearchResult(
    var Id: String,
    var Image: String,
    var Title: String,
    var Description: String,
    var Plot: String,
    var Stars: String
) : Parcelable