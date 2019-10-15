# Orient-PadTemplate
`Orient-PadTemplate`项目旨在为`Android Pad`项目搭建提供参考方案。 

![图片](<https://github.com/mCyp/Orient-PadTemplate/blob/master/device-2019-07-27-104850.png>)

## 一、技术选型

- 语言：`[Java]`或者`Kotlin`
- 数据库框架：[GreenDao](<https://github.com/greenrobot/greenDAO>)、`DBFlow`和`Room`
- 数据观察库：[RxJava2](<https://github.com/ReactiveX/RxJava>)
- 生命周期处理库：[RxLifecycle](<https://github.com/trello/RxLifecycle>)
- 网络请求库：[Retrofit2](<https://github.com/square/retrofit>)
- 控件绑定库：[Butterknife](<https://github.com/JakeWharton/butterknife>)
- 图片处理：[Glide](<https://github.com/bumptech/glide>)
- 设计模式：`MVP`
- 屏幕适配推荐：[AndroidAutoSize](<https://github.com/JessYanCoding/AndroidAutoSize>) 建议阅读-> [《今日头条适配方案》](<https://juejin.im/post/5b7a29736fb9a019d53e7ee2>)
- 其他：[Dagger 2](<https://github.com/google/dagger>)，还有的一些详见项目

## 二、功能

### # 二维码

二维码库推荐：[BGAQRCode-Android](<https://github.com/bingoogolapple/BGAQRCode-Android>)

通常，ZBar的识别速度更快。ZXing可以识别二维码，也可以生成二维码。

如果涉及到**二维码打印**，需要集成打印机的SDK。

### # 表格

表格 + 分页策略

#### 线性表格

线性表格方案推荐`RecyclerView`+自定义分割线，可以实现比较美观的样式。

分页策略：自动刷新 -> [Paging](<https://www.jianshu.com/p/0b7c82a5c27f>)

#### 网格表格

网格表格推荐：[ScrollablePanel](<https://github.com/Kelin-Hong/ScrollablePanel>)

为什么不采用`RecylerView`中的网格布局呢？因为网格布局必须让行或者列其中一个全部展示，换句话说如果我的项目中的行或者列太多就不行。

分页策略：下拉刷新 -> [SwipeToLoadLayout](<https://github.com/Aspsine/SwipeToLoadLayout>) + 自定义策略

### # 相机

#### 拍照

调用系统相机，注意视频Android 7.0及以上

#### 录制视频

视频录制：[VideoRecorderAndCompressor](<https://github.com/chenzhihui28/VideoRecorderAndCompressor>)

网上大部分视频录制的SDK都是要收费的，我选择了一款免费的开源项目，直接从中移植。

#### 音频录制

## 三、规范

建议遵循《阿里巴巴Android开发手册》