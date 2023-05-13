package com.nurfauzi.storyapp.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.nurfauzi.storyapp.R

class MyEditEmailText : AppCompatEditText, View.OnTouchListener {

    private lateinit var warningButtonImage: Drawable

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun showWarningButton() {
        setButtonDrawables(endOfTheText = warningButtonImage)
    }

    private fun hideWarningButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    private fun init() {
        warningButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_warning) as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (!Patterns.EMAIL_ADDRESS.matcher(s).matches() && s.toString()
                        .isNotEmpty()
                ) showWarningButton() else hideWarningButton()
            }
        })
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    warningButtonImage =
                        ContextCompat.getDrawable(context, R.drawable.ic_warning) as Drawable
                    error = "Format email salah"
                    if (text == null) {
                        hideWarningButton()
                    }
                    return true
                }
                else -> return false
            }
        }
        return false
    }
}