package com.skierski.github.applause.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Repo(
    val id: Int,
    val name: String,
    val description: String
) : Parcelable