package com.ogscompany.savesound

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomNavigationDrawerFragment: BottomSheetDialogFragment() {
    var millisecondTime: Long = 0
    var startTime:Long = 0
    var timeBuff:Long = 0
    var updateTime = 0L
    var seconds: Int = 0
    var minutes:Int = 0
    var milliSeconds:Int = 0
    lateinit var time : TextView
    lateinit var handler: Handler
    private fun startTimer()
    {
        time.text = getString(R.string.startTime)
        handler = Handler()
        startTime = SystemClock.uptimeMillis()
        handler.postDelayed(runnable, 0)
    }
    fun stopTimer()
    {
        timeBuff += millisecondTime
        handler.removeCallbacks(runnable)
        millisecondTime = 0L
        startTime = 0L
        timeBuff = 0L
        updateTime = 0L
        seconds = 0
        minutes = 0
        milliSeconds = 0
    }
    private var runnable: Runnable = object : Runnable {

        override fun run() {

            millisecondTime = SystemClock.uptimeMillis() - startTime

            updateTime = timeBuff + millisecondTime

            seconds = (updateTime / 1000).toInt()

            minutes = seconds / 60

            seconds %= 60

            milliSeconds = (updateTime % 1000).toInt()

            time.text = ("" + minutes + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%03d", milliSeconds))

            handler.postDelayed(this, 0)
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.fragment_bottomsheet, container, false)
        time = root.findViewById<View>(R.id.timer) as TextView
        startTimer()
        return root
    }
}

