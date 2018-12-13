package criminalintent.android.mobdev.com.criminalintent

import android.support.v4.app.Fragment

class CrimeListActivity : SingleFragmentActivity() {

    override fun createFragment(): Fragment {
        return CrimeListFragment()
    }
}