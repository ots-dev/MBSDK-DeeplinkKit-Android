package com.daimler.mbdeeplinkkit.sample.utils.bindings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("loadUrl")
fun ImageView.loadImageFromUrl(url: String?) {
    url?.let {
        Glide.with(this).load(it).into(this)
    }
}