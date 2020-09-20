package com.bpapps.ex2048clone

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "TAG.MainActivity"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val arr = Array(5) { i ->
//            Array(4) { j ->
//                i * j
//
//            }
//        }
//        arr.forEachIndexed { i, arrr ->
//            arrr.forEachIndexed { j, x ->
//                Log.d(TAG, "arr[$i][$j] = $x")
//            }
//        }
    }
}