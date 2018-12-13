package criminalintent.android.mobdev.com.criminalintent

import java.util.*

data class Crime(
    var mId: UUID = UUID.randomUUID(),
    var mTitle: String? = null,
    var mDate: Date = Date(),
    var mSolve: Boolean = false
)