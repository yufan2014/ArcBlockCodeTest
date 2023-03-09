package com.arcblock.codetest.view

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import com.arcblock.codetest.R

class LoadingProgress(context: Context) : Dialog(context, R.style.MyProgressDialog) {
    private var loadingview: LoadingView? = null
    private var content: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_loading_dialog)
        loadingview = findViewById(R.id.loadingview)
        setOnDismissListener { dialog: DialogInterface? ->
            if (loadingview != null) {
                loadingview!!.stopAnim()
            }
        }
    }


    override fun dismiss() {
        loadingview?.stopAnim()
        super.dismiss()
    }

    /**
     * @param context 上下文对象
     */
    init {
        this.content=content
        //点击提示框外面是否取消提示框
        setCanceledOnTouchOutside(false)
        //点击返回键是否取消提示框
        setCancelable(false)
    }
}