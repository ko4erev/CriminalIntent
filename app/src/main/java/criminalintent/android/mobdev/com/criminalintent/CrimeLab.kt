package criminalintent.android.mobdev.com.criminalintent

import android.content.Context
import java.util.*

class CrimeLab {

    private var mCrimes: List<Crime>? = null

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
        for (i in 0..99) {
            val crime = Crime()
            crime.mTitle = "Crime #$i"
            crime.mSolve = (i % 2 == 0) // Для каждого второго объекта
            (mCrimes as ArrayList<Crime>).add(crime)
        }
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