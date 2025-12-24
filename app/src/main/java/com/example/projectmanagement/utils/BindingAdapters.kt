package com.example.projectmanagement.utils

import android.view.View
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("isVisible")
fun View.setIsVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("errorText")
fun TextInputLayout.setErrorText(error: String?) {
    this.error = error
    isErrorEnabled = error != null
}



