package com.bangkit.anemai.ui.register

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class RegisterGenderEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    init {
        inputType = InputType.TYPE_NULL
        setOnTouchListener(this)
        setOnClickListener { showGenderDialog() }
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            showGenderDialog()
            return true
        }
        return false
    }

    private fun showGenderDialog() {
        val genders = arrayOf("Male", "Female")
        AlertDialog.Builder(context)
            .setTitle("Select Gender")
            .setItems(genders) { _, which ->
                setText(genders[which])
            }
            .show()
    }
}