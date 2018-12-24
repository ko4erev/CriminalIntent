package criminalintent.android.mobdev.com.criminalintent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v4.view.ViewPager
import android.support.v4.app.FragmentStatePagerAdapter
import java.util.*


class CrimePagerActivity : AppCompatActivity() {

    private var mViewPager: ViewPager? = null
    private var mCrimes: List<Crime?>? = null

    companion object {
        private const val EXTRA_CRIME_ID = "criminalintent.android.mobdev.com.criminalintent.crime_id"

        fun newIntent(packageContext: Context, crimeId: UUID?): Intent {
            val intent = Intent(packageContext, CrimePagerActivity::class.java)
            intent.putExtra(EXTRA_CRIME_ID, crimeId)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime_pager)

        val crimeId = intent.getSerializableExtra(EXTRA_CRIME_ID) as UUID

        mViewPager = findViewById(R.id.crime_view_pager)
        mCrimes = CrimeLab.get(this).getCrimes()

        var fragmentManager: FragmentManager = supportFragmentManager
        mViewPager?.adapter = object : FragmentStatePagerAdapter(fragmentManager) {

            override fun getItem(position: Int): Fragment {
                val crime = mCrimes?.get(position)
                return CrimeFragment.newInstance(crime?.mId ?: UUID.randomUUID())
            }

            override fun getCount(): Int {
                return mCrimes?.size ?: 0
            }
        }
        for (i in 0 until (mCrimes?.size ?: 0)) {
            if (mCrimes?.get(i)?.mId == crimeId) {
                mViewPager?.currentItem = i
                break
            }
        }
    }
}