package com.ddr1.storyapp.view

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.ddr1.storyapp.R

fun Context.showToast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showGenericAlertDialog(message: String){
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton(getString(R.string.button_text_ok)){ dialog, _ ->
            dialog.dismiss()
        }
    }.show()
}