package criminalintent.android.mobdev.com.criminalintent.database

import android.database.Cursor
import android.database.CursorWrapper
import criminalintent.android.mobdev.com.criminalintent.Crime
import criminalintent.android.mobdev.com.criminalintent.database.CrimeDbSchema.CrimeTable
import java.util.*


class CrimeCursorWrapper(cursor: Cursor?) : CursorWrapper(cursor) {

    fun getCrime(): Crime? {
        var uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID))
        var title = getString(getColumnIndex(CrimeTable.Cols.TITLE))
        var date = getLong(getColumnIndex(CrimeTable.Cols.DATE))
        var isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED))
        var suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT))

        var crime = Crime(UUID.fromString(uuidString))
        crime.mTitle = title
        crime.mDate = Date(date)
        crime.mSolve = isSolved != 0
        crime.mSuspect = suspect
        return crime
    }
}