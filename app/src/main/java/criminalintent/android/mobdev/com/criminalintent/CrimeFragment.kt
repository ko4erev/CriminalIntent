package criminalintent.android.mobdev.com.criminalintent

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.CheckBox
import java.util.*


class CrimeFragment : Fragment() {

    private var mCrime: Crime = Crime()
    private var mTitleField: EditText? = null
    private var mDateButton: Button? = null
    private var mSolvedCheckBox: CheckBox? = null

    companion object {
        private const val ARG_CRIME_ID = "crime_id"
        private const val DIALOG_DATE = "DialogDate"
        private const val REQUEST_DATE = 0

        fun newInstance(crimeId: UUID): CrimeFragment {
            val args = Bundle()
            args.putSerializable(ARG_CRIME_ID, crimeId)
            val fragment = CrimeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeId = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        mCrime = CrimeLab.get(activity as Context).getCrime(crimeId) ?: Crime()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.fragment_crime, container, false)
        mTitleField = v.findViewById(R.id.crime_title) as EditText
        mTitleField?.setText(mCrime?.mTitle)
        mTitleField?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mCrime?.mTitle = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        mDateButton = v.findViewById(R.id.crime_date)
        mDateButton?.text = mCrime?.mDate.toString()
        mDateButton?.setOnClickListener {
            val manager = fragmentManager
            val dialog = DatePickerFragment().newInstance(mCrime.mDate)
            dialog.setTargetFragment(this@CrimeFragment, REQUEST_DATE)
            dialog.show(manager, DIALOG_DATE)
        }

        mSolvedCheckBox = v.findViewById(R.id.crime_solved)
        mSolvedCheckBox?.isChecked = mCrime.mSolve
        mSolvedCheckBox?.setOnCheckedChangeListener { buttonView, isChecked -> mCrime?.mSolve = isChecked }
        return v
    }
}