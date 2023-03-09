package com.arcblock.codetest.http

import com.arcblock.codetest.model.BlogBeanItem
import io.reactivex.Observable
import retrofit2.http.GET

interface Api {

    @GET("blog/posts.json")
    fun httpGetBlog(): Observable<List<BlogBeanItem>>
}