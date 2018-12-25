package criminalintent.android.mobdev.com.criminalintent

import java.util.*

class Crime() {
    var mId: UUID = UUID.randomUUID()
    var mTitle: String? = null
    var mDate: Date = Date()
    var mSolve: Boolean = false
    var mSuspect: String? = null

    constructor(id: UUID) : this() {
        mId = id
        mDate = Date()
    }
}
