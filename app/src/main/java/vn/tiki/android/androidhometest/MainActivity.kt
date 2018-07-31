package vn.tiki.android.androidhometest

import android.os.AsyncTask
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import rx.Observable
import rx.Scheduler
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import vn.tiki.android.androidhometest.adapter.DealAdapter
import vn.tiki.android.androidhometest.data.api.ApiServices
import vn.tiki.android.androidhometest.data.api.response.Deal
import vn.tiki.android.androidhometest.di.initDependencies
import vn.tiki.android.androidhometest.di.inject
import vn.tiki.android.androidhometest.di.releaseDependencies
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    val apiServices by inject<ApiServices>()
    lateinit var dealSubscription: Subscription
    lateinit var dealTimer: PublishSubject<Long>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDependencies(this)

        setContentView(R.layout.activity_main)

        object : AsyncTask<Unit, Unit, MutableList<Deal>>() {
            override fun doInBackground(vararg params: Unit?): MutableList<Deal> {
                return apiServices.getDeals()
            }

            override fun onPostExecute(result: MutableList<Deal>?) {
                super.onPostExecute(result)
//                result.orEmpty()
//                        .forEach { deal ->
//                            println(deal)
//                        }
                logList(result)
            }
        }.execute()
    }

    private fun logList(result: MutableList<Deal>?) {

        /* dealSubscription = Observable.interval(1, TimeUnit.SECONDS)
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribeOn(Schedulers.newThread())
                 .subscribe()*/

        val adapter = DealAdapter(result, this)
        rvDealList.layoutManager = GridLayoutManager(this, 2)
        rvDealList.adapter = adapter

        if (result != null) {
            //get end_time of the last deal
            //to calculate time need repeat
          val timeRepeat = result.get(result.size - 1).endDate.time - (System.currentTimeMillis())

            object : CountDownTimer(timeRepeat, 1000) {
                // Each second, CountDownTimer update UI by notify adapter
                override fun onTick(millisUntilFinished: Long) {
                    adapter.notifyDataSetChanged()
                }

                override fun onFinish() {
                }
            }.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseDependencies()
//        if (!dealSubscription.isUnsubscribed()) {
//            dealSubscription.unsubscribe();
//        }
    }
}
