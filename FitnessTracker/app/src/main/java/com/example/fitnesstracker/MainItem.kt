package com.example.fitnesstracker

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes

data class MainItem(

    val id: Int,
    @StringRes val textStringId: Int,
    @DrawableRes val drawableId: Int,

)
