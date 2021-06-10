package com.wayapay.ng.landingpage.dialog

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.utils.getFirstHourOfDay
import java.util.*

class DatePickerDialog(private val activity: Activity,
                       private val onClickListener: OnClickListener,
                       private val TAG:String): DialogFragment(), DatePickerDialog.OnDateSetListener {

    interface OnClickListener{
        fun onDatePicked(date: GregorianCalendar, tag:String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(activity, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        val currentDate = GregorianCalendar()
        val date = GregorianCalendar()
        date.set(year, month, day)

        //if chosen date is ahead of today return
        if(getFirstHourOfDay(date) < getFirstHourOfDay(currentDate)) {
            Toast.makeText(activity.applicationContext, getString(R.string.select_a_feature_date), Toast.LENGTH_SHORT).show()
            return
        }

        onClickListener.onDatePicked(date, TAG)
    }
}