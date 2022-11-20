package com.ddr1.storyapp.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.ddr1.storyapp.R

class EditText: AppCompatEditText {
    constructor(context: Context): super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        setBackgroundResource(R.drawable.bg_edit_text)
        inputType = android.text.InputType.TYPE_CLASS_TEXT
    }
}