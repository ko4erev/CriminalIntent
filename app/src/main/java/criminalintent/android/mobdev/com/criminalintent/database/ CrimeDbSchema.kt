package criminalintent.android.mobdev.com.criminalintent.database

class CrimeDbSchema {


    class CrimeTable {
        companion object {
            val NAME = "crimes"
        }

        class Cols {
            companion object {
                val UUID = "uuid"
                val TITLE = "title"
                val DATE = "date"
                val SOLVED = "solved"
            }
        }
    }

}