# 体验 GoogleVR 官方 Demo

Import GoogleVRForUnity.unitypackage 的时候出现了失败 , 原因只有两个:

1. 文件路径有中文 ;
2. GoogleVRForUnity.unitypackage 文件有损坏 ;

事实证明 , 我便上了 2 的当 , 宿舍网太渣 , 用浏览器下载了 GoogleVRForUnity.unitypackage 之后无法导入 , 排除了原因 1 之后一看此包大小有 21 MB , 第二天用公司的网重新下载了一遍果然发现实际上 GoogleVRForUnity.unitypackage 是应该有 67.7 MB 的 .

---

build android apk 的时候遇到了以下挫折:

`Build failure Unable to merge android manifests. See the Console for more details. See the Console for details.`

查看 Console 显示:

`Warning: [Temp/StagingArea/AndroidManifest-main.xml:14, /Users/hufei/Projects/u3d/CardboardDemo/Temp/StagingArea/android-libraries/gvr-permissionsupport-release/AndroidManifest.xml:3] Main manifest has but library uses targetSdkVersion=’24’
]`

于是直接在 /Temp/StagingArea/ 修改 Manifest 之后发现不可行, 原来是改错地方了 , 因为每次 build 还是会被替换成原本的还原 , 原来没有从根本上改, 即需要在 Plugins 文件夹里修改 android 的 manifest 设置

![](http://img.blog.csdn.net/20170104205236377?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvSGVhcnR5aHU=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

我把 `<uses-sdk android:minSdkVersion="19" android:targetSdkVersion="22" />` 的 22 改成 24 就好了

---

在手机模拟器上运行 Demo 效果如下:

![](http://ww4.sinaimg.cn/mw690/a53846c3gw1fbex1f2jujj20u40iemyq.jpg)

![](http://ww4.sinaimg.cn/mw690/a53846c3gw1fbex1e5x01j20u40iedhz.jpg)

![](http://ww2.sinaimg.cn/mw690/a53846c3gw1fbex1hja4nj20u40iegnq.jpg)

GoogleVR SDK 还有另三个 Demo 如下:

![](http://ww3.sinaimg.cn/mw690/a53846c3gw1fbex1fs3maj20ya0gmq51.jpg)
![](http://ww1.sinaimg.cn/mw690/a53846c3gw1fbex1jtq75j20y80gimyp.jpg)
![](http://ww1.sinaimg.cn/mw690/a53846c3gw1fbex1ikiduj20yc0gkwgs.jpg)
