package criminalintent.android.mobdev.com.criminalintent

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.widget.DatePicker
import java.util.*


class DatePickerFragment : DialogFragment() {

    private val ARG_DATE = "date"
    private var mDatePicker: DatePicker? = null

    fun newInstance(date: Date): DatePickerFragment {
        val args = Bundle()
        args.putSerializable(ARG_DATE, date)
        val fragment = DatePickerFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = arguments?.getSerializable(ARG_DATE) as Date

        val calendar = Calendar.getInstance()
        calendar.time = date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val v = LayoutInflater.from(activity).inflate(R.layout.dialog_date, null)

        mDatePicker = v.findViewById(R.id.dialog_date_picker) as DatePicker
        mDatePicker?.init(year, month, day, null)

        return AlertDialog.Builder(activity)
            .setView(v)
            .setTitle(R.string.date_picker_title)
            .setPositiveButton(android.R.string.ok, null)
            .create()
    }
}