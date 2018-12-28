package criminalintent.android.mobdev.com.criminalintent

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.support.v7.app.AppCompatActivity


class CrimeListFragment : Fragment() {

    private var mCrimeRecyclerView: RecyclerView? = null
    private var mAdapter: CrimeAdapter? = null
    private var mSubtitleVisible: Boolean = false
    private var mCallbacks: Callbacks? = null
    private val SAVED_SUBTITLE_VISIBLE = "subtitle"

    interface Callbacks {
        fun onCrimeSelected(crime: Crime)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCallbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE)
        }
        var view: View = inflater.inflate(R.layout.fragment_crime_list, container, false)
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view)
        mCrimeRecyclerView?.layoutManager = LinearLayoutManager(activity)
        updateUI()
        return view
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible)
    }

    override fun onDetach() {
        super.onDetach()
        mCallbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.fragment_crime_list, menu)
        val subtitleItem = menu?.findItem(R.id.show_subtitle)
        if (mSubtitleVisible) {
            subtitleItem?.setTitle(R.string.hide_subtitle)
        } else {
            subtitleItem?.setTitle(R.string.show_subtitle)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_crime -> {
                val crime = Crime()
                CrimeLab.get(activity as Context).addCrime(crime)
                updateUI()
                mCallbacks?.onCrimeSelected(crime)
                true
            }
            R.id.show_subtitle -> {
                mSubtitleVisible = !mSubtitleVisible
                activity?.invalidateOptionsMenu()
                updateSubtitle()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateSubtitle() {
        val crimeLab = CrimeLab.get(activity as Context)
        val crimeCount = crimeLab.getCrimes()?.size
        var subtitle: String? = getString(R.string.subtitle_format, crimeCount)
        if (!mSubtitleVisible) {
            subtitle = null
        }
        val activity = activity as AppCompatActivity?
        activity?.supportActionBar?.subtitle = subtitle
    }

    private fun updateUI() {
        val crimeLab = activity?.let {
            CrimeLab.get(it)
        }
        val crimes = crimeLab?.getCrimes()
        if (mAdapter == null) {
            mAdapter = CrimeAdapter(crimes)
            mCrimeRecyclerView?.adapter = mAdapter
        } else {
            mAdapter?.setCrimes(crimes)
            mAdapter?.notifyDataSetChanged()
        }
        updateSubtitle()
    }

    private inner class CrimeHolder : RecyclerView.ViewHolder, View.OnClickListener {

        override fun onClick(v: View?) {
            mCallbacks?.onCrimeSelected(mCrime)
        }

        private var mTitleTextView: TextView? = null
        private var mDateTextView: TextView? = null
        private var mSolvedImageView: ImageView? = null
        private var mCrime = Crime()

        @SuppressLint("WrongViewCast")
        constructor(inflater: LayoutInflater, parent: ViewGroup) : super(
            inflater.inflate(R.layout.list_item_crime, parent, false)
        ) {
            itemView.setOnClickListener(this)
            mTitleTextView = itemView.findViewById<View>(R.id.crime_title) as TextView
            mDateTextView = itemView.findViewById<View>(R.id.crime_date) as TextView
            mSolvedImageView = itemView.findViewById<View>(R.id.crime_solved) as ImageView
        }

        fun bind(crime: Crime) {
            mCrime = crime
            mTitleTextView?.text = mCrime?.mTitle
            mDateTextView?.text = mCrime?.mDate.toString()
            mSolvedImageView?.visibility =
                    if (crime.mSolve)
                        View.VISIBLE
                    else
                        View.GONE

        }
    }


    private inner class CrimeAdapter(private var mCrimes: List<Crime?>?) : RecyclerView.Adapter<CrimeHolder>() {
        override fun getItemCount(): Int {
            return mCrimes?.size ?: 0
        }

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = mCrimes?.get(position)
            holder.bind(crime ?: Crime())
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val layoutInflater = LayoutInflater.from(activity)
            return CrimeHolder(layoutInflater, parent)
        }

        fun setCrimes(crimes: List<Crime?>?) {
            mCrimes = crimes
        }
    }
}
