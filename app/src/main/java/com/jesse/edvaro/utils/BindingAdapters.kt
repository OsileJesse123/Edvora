package com.jesse.edvaro.utils

import android.widget.Button
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageSrcUrl")
fun bindImage(imageView: ImageView, imgUrl: String?){
    imgUrl?.let {
        if(it.isNotEmpty()){
            val imageUri = it.toUri().buildUpon().scheme("https").build()
            Glide.with(imageView.context)
                .load(imageUri)
                .into(imageView)
        }
    }
}

@BindingAdapter("formatText")
fun formatButtonText(button: Button, text: String?){
    text?.let {
        button.text = formatReceivedText(it, 11)
    }
}