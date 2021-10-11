package com.example.blogappfirebase.core

import android.opengl.Visibility
import android.view.View

fun View.hideIt() {
    this.visibility = View.GONE
}

fun View.showIt() {
    this.visibility = View.VISIBLE
}