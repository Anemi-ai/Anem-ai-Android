package com.bangkit.anemai.ui.register

import android.app.DatePickerDialog
import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.bangkit.anemai.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RegisterBirthDateEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)

    init {
        inputType = InputType.TYPE_NULL
        setOnTouchListener(this)
        setOnClickListener { showDatePicker() }
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            showDatePicker()
            return true
        }
        return false
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateEditText()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun updateEditText() {
        val selectedDate = calendar.time
        val age = calculateAge(selectedDate)
        if (age < 13) {
            error = context.getString(R.string.error_age_less_than_17)
            setText(R.string.error_age_less_than_17)
        } else {
            setText(dateFormat.format(selectedDate))
            error = null
        }
    }

    private fun calculateAge(birthDate: Date): Int {
        val today = Calendar.getInstance()
        val birthDay = Calendar.getInstance()
        birthDay.time = birthDate

        var age = today.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < birthDay.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        return age
    }
}