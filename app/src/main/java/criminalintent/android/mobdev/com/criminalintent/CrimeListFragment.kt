package criminalintent.android.mobdev.com.criminalintent

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


class CrimeListFragment : Fragment() {

    private var mCrimeRecyclerView: RecyclerView? = null
    private var mAdapter: CrimeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
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

    private fun updateUI() {
        val crimeLab = activity?.let {
            CrimeLab.get(it) }
        val crimes = crimeLab?.getCrimes()
        if (mAdapter == null) {
            mAdapter = CrimeAdapter(crimes!!)
            mCrimeRecyclerView?.adapter = mAdapter
        } else {
            mAdapter?.notifyDataSetChanged()
        }
    }

    private inner class CrimeHolder : RecyclerView.ViewHolder, View.OnClickListener {

        override fun onClick(v: View?) {
            val intent = CrimePagerActivity.newIntent(activity as Context, mCrime?.mId)
            startActivity(intent)
        }

        private var mTitleTextView: TextView? = null
        private var mDateTextView: TextView? = null
        private var mSolvedImageView: ImageView? = null
        private var mCrime: Crime? = null

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


    private inner class CrimeAdapter(private val mCrimes: List<Crime>) : RecyclerView.Adapter<CrimeHolder>() {
        override fun getItemCount(): Int {
            return mCrimes.size
        }

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = mCrimes[position]
            holder.bind(crime)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val layoutInflater = LayoutInflater.from(activity)
            return CrimeHolder(layoutInflater, parent)
        }
    }
}
