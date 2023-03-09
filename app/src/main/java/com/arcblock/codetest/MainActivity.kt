package com.arcblock.codetest

import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.arcblock.codetest.Config.Companion.BASE_URL
import com.arcblock.codetest.adapter.BlogRecyAdapter
import com.arcblock.codetest.adapter.TopTagRecyAdapter
import com.arcblock.codetest.http.NetWorkUtil
import com.arcblock.codetest.model.BlogBeanItem
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.tencent.smtt.utils.l
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_loading_dialog.*


/**
 * 首页
 */
class MainActivity : BaseActivity() {


    private var data: List<BlogBeanItem> = arrayListOf()
    var tagData = hashMapOf<String,ArrayList<BlogBeanItem>>()
    var categoriesData = hashMapOf<String,ArrayList<BlogBeanItem>>()

    // 顶部标签
    private val mTopTagAdapter by lazy {
        TopTagRecyAdapter(mutableListOf())
    }

    // 新闻Item
    private val mBlogListAdapter by lazy {
        BlogRecyAdapter(mutableListOf())
    }


    override fun getLayout(): Int {
        return R.layout.activity_main
    }


    override fun initView() {
        super.initView()
        mRlvTopTagList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mRlvTopTagList.itemAnimator = DefaultItemAnimator()
        mRlvTopTagList.adapter = mTopTagAdapter

        mRlvBlogList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRlvBlogList.itemAnimator = DefaultItemAnimator()
        mRlvBlogList.adapter = mBlogListAdapter

        mSrlRefresh.setOnRefreshListener {
            initData()
        }

        tv_error.setOnClickListener {
            initData()
        }

        mTopTagAdapter.setOnItemClickCallBack(object :TopTagRecyAdapter.OnItemClickCallBack{
            override fun onItemClick(position: Int, item: String) {
                val blogBeanItems = tagData.get(item)
                mBlogListAdapter.setNewData(blogBeanItems)
            }

        })
        mBlogListAdapter.setOnItemBtnClickCallBack(object :BlogRecyAdapter.OnItemBtnClickCallBack{
            override fun onCategorieClick(categorie: String, item: BlogBeanItem) {
                val blogBeanItems = categoriesData.get(categorie)
                mBlogListAdapter.setNewData(blogBeanItems)
            }

            override fun onItemClick(position: Int, item: BlogBeanItem) {
                WebActivity.startWeb(this@MainActivity,BASE_URL.plus(item.frontmatter?.path))
            }

        })
    }

    override fun initData() {
        startLoading()
        NetWorkUtil.getInstance().getService().httpGetBlog()
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :
            io.reactivex.Observer<List<BlogBeanItem>> {
            override fun onSubscribe(d: Disposable) {
                mSrlRefresh.visibility = View.VISIBLE
                tag_title.visibility = View.VISIBLE
                tv_error.visibility = View.GONE
            }

            override fun onError(e: Throwable) {
                mSrlRefresh.visibility = View.GONE
                tag_title.visibility = View.GONE
                tv_error.visibility = View.VISIBLE
                mSrlRefresh.isRefreshing = false
                endLoading()
                ToastUtils.showShort(R.string.get_info_error)
                LogUtils.i(e.message)
            }

            override fun onComplete() {
                mSrlRefresh.visibility = View.VISIBLE
                tag_title.visibility = View.VISIBLE
                tv_error.visibility = View.GONE
                endLoading()
                mSrlRefresh.isRefreshing = false

            }

            override fun onNext(t: List<BlogBeanItem>) {


                if(t != null && t.size>0) {
                    data = t
                    tagData.clear()
                    tagData.put("All", ArrayList(t))
                    for (blog in t) {
                        if (blog?.frontmatter?.tags?.size > 0) {
                            for (tag in blog.frontmatter.tags) {
                                var item: ArrayList<BlogBeanItem>? = tagData.get(tag)
                                if (item != null && item.size > 0) {
                                    item.add(blog);
                                } else {
                                    var arrayList = ArrayList<BlogBeanItem>()
                                    arrayList.add(blog)
                                    tagData.put(tag, arrayList)
                                }
                            }

                            for (categorie in blog.frontmatter.categories) {
                                var item: ArrayList<BlogBeanItem>? = categoriesData.get(categorie)
                                if (item != null && item.size > 0) {
                                    item.add(blog);
                                } else {
                                    var arrayList = ArrayList<BlogBeanItem>()
                                    arrayList.add(blog)
                                    categoriesData.put(categorie, arrayList)
                                }
                            }
                        }
                    }
                    mTopTagAdapter.setNewData(ArrayList(tagData.keys))
                    mBlogListAdapter.setNewData(data)
                }else{
                    tag_title.visibility = View.GONE
                    mSrlRefresh.visibility = View.GONE
                   mBlogListAdapter.setEmptyView(R.layout.empty_layout,null)
                }
            }

        })
    }


}