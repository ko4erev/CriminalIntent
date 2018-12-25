package criminalintent.android.mobdev.com.criminalintent

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.widget.Button
import android.widget.CheckBox
import java.util.*
import android.provider.ContactsContract


class CrimeFragment : Fragment() {

    private var mCrime: Crime = Crime()
    private var mTitleField: EditText? = null
    private var mDateButton: Button? = null
    private var mSolvedCheckBox: CheckBox? = null
    private var mReportButton: Button? = null
    private var mSuspectButton: Button? = null

    companion object {
        private const val ARG_CRIME_ID = "crime_id"
        private const val DIALOG_DATE = "DialogDate"
        private const val REQUEST_DATE = 0
        private const val REQUEST_CONTACT = 1

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

    override fun onPause() {
        super.onPause()
        CrimeLab.get(activity as Context).updateCrime(mCrime)
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
        updateDate()
        mDateButton?.setOnClickListener {
            val manager = fragmentManager
            val dialog = DatePickerFragment().newInstance(mCrime.mDate)
            dialog.setTargetFragment(this@CrimeFragment, REQUEST_DATE)
            dialog.show(manager, DIALOG_DATE)
        }

        mSolvedCheckBox = v.findViewById(R.id.crime_solved)
        mSolvedCheckBox?.isChecked = mCrime.mSolve
        mSolvedCheckBox?.setOnCheckedChangeListener { buttonView, isChecked -> mCrime?.mSolve = isChecked }

        mReportButton = v?.findViewById(R.id.crime_report)
        mReportButton?.setOnClickListener {
            var intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport())
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
            intent = Intent.createChooser(intent, getString(R.string.send_report))
            startActivity(intent)
        }

        val pickContact = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        mSuspectButton = v.findViewById(R.id.crime_suspect) as Button
        mSuspectButton?.setOnClickListener {
            startActivityForResult(pickContact, REQUEST_CONTACT)
        }

        if (mCrime.mSuspect != null) {
            mSuspectButton?.text = mCrime.mSuspect
        }

        val packageManager = activity?.packageManager
        if (packageManager?.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton?.isEnabled = false
        }
        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_DATE) {
            val date = data?.getSerializableExtra(DatePickerFragment().EXTRA_DATE) as Date
            mCrime.mDate = date
            updateDate()
        } else if (resultCode == REQUEST_CONTACT && data != null) {
            var contactUri = data.data
            var queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
            var cursor = activity?.contentResolver?.query(
                contactUri, queryFields,
                null, null, null
            )
            try {
                if (cursor?.count == 0) {
                    return
                }
                cursor?.moveToFirst()
                var suspect = cursor?.getString(0)
                mCrime.mSuspect = suspect
                mSuspectButton?.text = suspect
            } finally {
                cursor?.close()
            }
        }
    }

    private fun updateDate() {
        mDateButton?.text = mCrime.mDate.toString()
    }

    private fun getCrimeReport(): String {
        var solvedString: String? = if (mCrime.mSolve) {
            getString(R.string.crime_report_solved)
        } else {
            getString(R.string.crime_report_unsolved)
        }
        val dateFormat = "EEE, MMM dd"
        val dateString = DateFormat.format(dateFormat, mCrime.mDate).toString()
        var suspect = mCrime.mSuspect
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect)
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect)
        }
        return getString(R.string.crime_report, mCrime.mTitle, dateString, solvedString, suspect)
    }
}