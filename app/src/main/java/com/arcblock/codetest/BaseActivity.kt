package com.arcblock.codetest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arcblock.codetest.view.LoadingProgress
import com.blankj.utilcode.util.BarUtils

abstract class BaseActivity : AppCompatActivity(){

    private var mProgress: LoadingProgress? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())

        BarUtils.setStatusBarLightMode(this, true)

        initView()
        initData()


    }

    abstract fun getLayout(): Int

    open fun initView(){

     }
    open fun initData(){

     }

    open fun startLoading() {
        if (mProgress == null) {
            mProgress = LoadingProgress(this)
        }
        if (!this.isFinishing) {
            if (!mProgress!!.isShowing) {
                mProgress!!.show()
            }
        }
    }

    open fun endLoading() {
        if (mProgress != null && mProgress!!.isShowing) {
            mProgress!!.dismiss()
        }
    }

    override fun onBackPressed() {
        if (mProgress == null || !mProgress!!.isShowing()) {
            super.onBackPressed()
        }
    }
}