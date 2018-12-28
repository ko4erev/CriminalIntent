package criminalintent.android.mobdev.com.criminalintent

import android.support.v4.app.Fragment
import android.view.View

class CrimeListActivity : SingleFragmentActivity(), CrimeListFragment.Callbacks {

    override fun createFragment(): Fragment {
        return CrimeListFragment()
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_masterdetail
    }

    override fun onCrimeSelected(crime: Crime) {
        if (findViewById<View>(R.id.detail_fragment_container) == null) {
            var intent = CrimePagerActivity.newIntent(this, crime.mId)
            startActivity(intent)
        } else {
            var newDetail = CrimeFragment.newInstance(crime.mId)
            supportFragmentManager.beginTransaction()
                .replace(R.id.detail_fragment_container, newDetail)
                .commit()
        }
    }
}