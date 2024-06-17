package com.bangkit.anemai.ui.login

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.bangkit.anemai.R

class LoginPasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private var visibilityToggleImage: Drawable
    private var isPasswordVisible: Boolean = false

    init {
        visibilityToggleImage = ContextCompat.getDrawable(context, R.drawable.ic_visibility_black_24) as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                error = if (s.toString().isEmpty()) {
                    context.getString(R.string.password_must_be_filled)
                } else {
                    null
                }
                showVisibilityToggle()
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        showVisibilityToggle()
    }

    private fun showVisibilityToggle() {
        val endDrawable: Drawable? = if (isPasswordVisible) {
            ContextCompat.getDrawable(context, R.drawable.ic_visibility_off_black_24)
        } else {
            ContextCompat.getDrawable(context, R.drawable.ic_visibility_black_24)
        }
        setButtonDrawables(endOfTheText = endDrawable)
    }

    private fun setButtonDrawables(startOfTheText: Drawable? = null, topOfTheText: Drawable? = null, endOfTheText: Drawable? = null, bottomOfTheText: Drawable? = null) {
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val visibilityToggleStart: Float
            val visibilityToggleEnd: Float
            var isVisibilityToggleClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                visibilityToggleEnd = (visibilityToggleImage.intrinsicWidth + paddingStart).toFloat()
                if (event.x < visibilityToggleEnd) isVisibilityToggleClicked = true
            } else {
                visibilityToggleStart = (width - paddingEnd - visibilityToggleImage.intrinsicWidth).toFloat()
                if (event.x > visibilityToggleStart) isVisibilityToggleClicked = true
            }

            if (isVisibilityToggleClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        togglePasswordVisibility()
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        } else {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        }
        isPasswordVisible = !isPasswordVisible
        setSelection(text?.length ?: 0)
        showVisibilityToggle()
    }

}