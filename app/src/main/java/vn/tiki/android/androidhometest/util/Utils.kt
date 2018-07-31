package vn.tiki.android.androidhometest.util

class Utils {

    companion object {
        /**
         * Adding "0" when number < 10
         * */
        fun formatNumber(number: Int): String {
            return if (number >= 10) {
                number.toString()
            } else {
                "0$number"
            }
        }
    }
}