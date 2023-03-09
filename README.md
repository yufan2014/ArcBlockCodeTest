# ArcBlockCode demo

## 基于下面提供的 json 数据源，实现一个简易的 文章列表-文章详情 应用。

* json 数据源: https://arcblockio.cn/blog/posts.json 
* Base Url: https://arcblockio.cn (返回的json中的资源需要拼接上此 base url 才可以访问)

## 建议

* 使用 kotlin 编写
* 需要考虑元素值为 null 的情况，代码需要足够健壮，具体外观自由发挥
* 详情页面为 H5 页面，可以自己发挥和增加一些 H5 详情页面可能有的功能和交互
* 考虑性能和可用性（例如：图片是否需要 cache？ 没有网络时如何处理？如果列表非常大怎么办？如果图片非常大怎么办？等等）
* 我们只考察在代码里实现了的细节，如果只是一些想法而没有实现不会得分
