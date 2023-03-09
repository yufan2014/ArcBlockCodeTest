package com.arcblock.codetest.adapter

import android.text.TextUtils
import android.widget.TextView
import com.arcblock.codetest.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * 标签适配器
 */
class TopTagRecyAdapter(data: List<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(
        R.layout.item_tag_list_layout, data
    ) {

    var mSelectValueCode: String? = null

    override fun convert(helper: BaseViewHolder, item: String) {

        val mTvTag = helper.getView<TextView>(R.id.mTvTag)
        mTvTag.text = item

        helper.itemView.setOnClickListener {
            mSelectValueCode = item
            mCallBack?.onItemClick(helper.adapterPosition, item)
        }
    }


    private var mCallBack: OnItemClickCallBack? = null

    public interface OnItemClickCallBack {
        fun onItemClick(position: Int, item:String)
    }

    public fun setOnItemClickCallBack(callback: OnItemClickCallBack?) {
        this.mCallBack = callback
    }

}