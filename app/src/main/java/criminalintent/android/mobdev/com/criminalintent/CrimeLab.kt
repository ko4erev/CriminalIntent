package criminalintent.android.mobdev.com.criminalintent

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import criminalintent.android.mobdev.com.criminalintent.database.CrimeBaseHelper
import criminalintent.android.mobdev.com.criminalintent.database.CrimeCursorWrapper
import criminalintent.android.mobdev.com.criminalintent.database.CrimeDbSchema.CrimeTable
import java.util.*
import kotlin.collections.ArrayList


class CrimeLab {

    private var mContext: Context? = null
    private var mDatabase: SQLiteDatabase? = null

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
        mContext = context.applicationContext
        mDatabase = CrimeBaseHelper(mContext).writableDatabase
    }

    fun addCrime(c: Crime) {
        var values = getContentValues(c)
        mDatabase?.insert(CrimeTable.NAME, null, values)
    }

    fun getCrimes(): List<Crime?> {
        var crimes: ArrayList<Crime?> = ArrayList()
        var cursor = queryCrimes(null, null)
        cursor.use { cursor ->
            cursor?.moveToFirst()
            while (cursor?.isAfterLast != true) {
                var t = cursor?.getCrime()
                crimes.add(t)
                var s = crimes
                cursor?.moveToNext()
            }
        }
        return crimes
    }

    fun getCrime(id: UUID): Crime? {
        val cursor = queryCrimes(
            CrimeTable.Cols.UUID + " = ?",
            arrayOf(id.toString())
        )
        cursor.use { cursor ->
            if (cursor?.count == 0) {
                return null
            }
            cursor?.moveToFirst()
            return cursor?.getCrime()
        }
    }

    fun updateCrime(crime: Crime) {
        val uuidString = crime.mId.toString()
        val values = getContentValues(crime)
        mDatabase?.update(
            CrimeTable.NAME, values,
            CrimeTable.Cols.UUID + " = ?",
            arrayOf(uuidString)
        )
    }

    private fun queryCrimes(whereClause: String?, whereArgs: Array<String>?): CrimeCursorWrapper? {
        var cursor = mDatabase?.query(
            CrimeTable.NAME,
            null, // с null выбираются все столбцы
            whereClause,
            whereArgs,
            null,
            null,
            null
        )
        return CrimeCursorWrapper(cursor)
    }

    private fun getContentValues(crime: Crime): ContentValues {
        var values = ContentValues()
        values.put(CrimeTable.Cols.UUID, crime.mId.toString())
        values.put(CrimeTable.Cols.TITLE, crime.mTitle)
        values.put(CrimeTable.Cols.DATE, crime.mDate.time)
        values.put(CrimeTable.Cols.SOLVED, if (crime.mSolve) 1 else 0)
        values.put(CrimeTable.Cols.SUSPECT, crime.mSuspect)
        return values
    }
}