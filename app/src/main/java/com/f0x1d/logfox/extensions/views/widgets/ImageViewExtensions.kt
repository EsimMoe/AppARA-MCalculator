package com.f0x1d.logfox.extensions.views.widgets

import android.widget.ImageView
import com.bumptech.glide.Glide
import moe.sekiu.appara_mcalculator.R

fun ImageView.loadIcon(pkg: String) {
    Glide
        .with(this)
        .load("icon:${pkg}")
        .error(R.drawable.ic_bug)
        .into(this)
}