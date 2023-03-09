package com.arcblock.codetest.adapter

import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import com.arcblock.codetest.Config.Companion.BASE_URL
import com.arcblock.codetest.R
import com.arcblock.codetest.http.NetWorkUtil
import com.arcblock.codetest.model.BlogBeanItem
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.NetworkUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder


class BlogRecyAdapter(data: List<BlogBeanItem>) :
    BaseQuickAdapter<BlogBeanItem, BaseViewHolder>(
        R.layout.item_list_child_layout, data
    ) {

    override fun convert(helper: BaseViewHolder, item: BlogBeanItem) {

        helper.setText(R.id.mTvTime,item.frontmatter?.date)
        helper.setText(R.id.mTvTitle,item.frontmatter?.title)
        Glide.with(helper.getView<ImageView>(R.id.img_))
            .load(BASE_URL.plus(item.frontmatter?.banner?.childImageSharp?.fixed?.src))
            .error(R.drawable.selector_f7f8fa_to_1d67fc_point)
            .into(helper.getView(R.id.img_))


        val mTvSource = helper.getView<TextView>(R.id.mTvSource)
        if(item.frontmatter?.categories != null && item.frontmatter?.categories.size>0) {
            mTvSource.setText(item.frontmatter?.categories[0])
        }

        helper.itemView.setOnClickListener {
            mCallBack?.onItemClick(helper.adapterPosition, item)
        }


        mTvSource.setOnClickListener {
            mCallBack?.onCategorieClick(if (TextUtils.isEmpty(mTvSource.text)) "" else mTvSource.text.toString(), item)
        }
    }
    private var mCallBack:OnItemBtnClickCallBack?=null
    public interface OnItemBtnClickCallBack{
        fun onCategorieClick(categorie:String,item: BlogBeanItem)
        fun onItemClick(position:Int,item: BlogBeanItem)
    }
    public fun setOnItemBtnClickCallBack(callback:OnItemBtnClickCallBack?){
        this.mCallBack = callback
    }

}