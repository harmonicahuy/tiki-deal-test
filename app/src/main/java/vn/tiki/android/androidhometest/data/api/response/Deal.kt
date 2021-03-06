package vn.tiki.android.androidhometest.data.api.response

import java.util.Date

class Deal(
    val productName: String,
    val productThumbnail: String,
    val productPrice: Double,
    val startedDate: Date,
    val endDate: Date


) {
    override fun toString(): String {
        return "Deal(productName='$productName', productThumbnail='$productThumbnail', productPrice=$productPrice, startedDate=$startedDate, endDate=$endDate)"
    }
}
