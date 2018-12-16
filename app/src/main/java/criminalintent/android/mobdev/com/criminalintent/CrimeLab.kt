package criminalintent.android.mobdev.com.criminalintent

import android.content.Context
import java.util.*

class CrimeLab {

    private var mCrimes: ArrayList<Crime>? = null

    companion object {
        private var sCrimeLab: CrimeLab? = null

        fun get(context: Context): CrimeLab {
            if (sCrimeLab == null) {
                sCrimeLab = CrimeLab(context)
            }
            return sCrimeLab as CrimeLab
        }
    }

    private constructor(context: Context) {
        mCrimes = ArrayList()
    }

    fun addCrime(c: Crime) {
        mCrimes?.add(c)
    }

    fun getCrimes(): List<Crime>? {
        return mCrimes
    }

    fun getCrime(id: UUID): Crime? {
        mCrimes?.forEach {
            if (it.mId == id) {
                return it
            }
        }
        return null
    }
}