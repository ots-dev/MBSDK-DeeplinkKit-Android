package com.daimler.mbdeeplinkkit.sample.utils.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T : ViewModel> LifecycleOwner.newViewModel() =
    ViewModelProvider.NewInstanceFactory().create(T::class.java)