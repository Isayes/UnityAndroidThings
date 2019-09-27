# 日记 & 说明

## APK 下载 # #

[传送门 → http://fir.im/kzh7 下载 apk（持续更新）](http://fir.im/kzh7)

[![App Logo](https://raw.githubusercontent.com/Isayes/ShanbayDemo/ed1535868e6c89bb3dedc99a3c28a9d0597e5d20/app/src/main/res/mipmap-xxhdpi/ic_launcher.png) ](http://fir.im/kzh7)

---

## 最终效果 # #

继续优化ing...

![](https://raw.githubusercontent.com/Isayes/ShanbayDemo/792f3ae9a507eccd86361cc2a66ac1be3635c001/screenshots/8.gif)


---

---

---

## 开发过程 # #

`#5`

已完成 App 的各基本要求

目前实现的结果：  

![](https://raw.githubusercontent.com/Isayes/ShanbayDemo/7c140635c925bf3cacbebf552735ad63594f4b12/screenshots/7.gif)

利用 Spannable 实现的点击单词高亮，两端对齐如果使用第三方的 JustifyTextView 会使 Spannable 失效，虽然排版不怎么美观，但已完成了所有的功能要求，接着会对这个小作业进行优化，细节上需要继续改进以及修复存在的 bug。

后面的课余时间想用 WebView + JS 再做一遍，然后对比一下

---

`#4`

目前实现的 Demo 结果：

![](https://raw.githubusercontent.com/Isayes/ShanbayDemo/033d9e79bf3d81591cc1d022fcd288ff185777c3/screenshots/6.gif) ![](https://raw.githubusercontent.com/Isayes/ShanbayDemo/df7cac60095771a1848269848eabc75cb80fc208/screenshots/5.gif)

关于 “文章界面有一个按钮，点击则在文章中高亮在单词列表中出现的单词” 这个需求，如果我把按钮直接放在文章里会感觉有点丑，所以我打算利用 Toolbar，模仿扇贝新闻通过点击 Toolbar 上的图标来滑出其它所需要的控件，从而进行下一步的操作，如上左图。 

[ TextView 自动换行导致混乱的原因 ]：半角字符与全角字符混乱所致，一般情况下，我们输入的数字、字母以及英文标点都是半角，所以占位无法确定。它们与汉字的占位大大的不同，由于这个原因，导致很多文字的排版参差不齐。  
[ 解决办法 ]：

1. 将 TextView 中的字符全角化。数字、字母以及标点全部转为全角字符，使之与汉字同占两个字节；
2. 去除特殊字符或将所有的中文标点符号都换成英文标点符号。通过正则表达式将所有特殊字符过滤，或利用 replaceAll() 将中文标点替换为英文标点；

---

`#3`

目前实现的 Demo 结果：

![#3](https://raw.githubusercontent.com/Isayes/ShanbayDemo/cb791f4608ec5792fdb7ca21092dbbc0c95b9a1c/screenshots/4.gif)

关于如何高亮文本的问题，通过搜索，发现了可以使用 Spannable（SpannableString）来进行类似于富文本（包括高亮文字，选择文字，点击文字等等）的操作？于是找了几个例子先稍微看了看：  

1. [https://github.com/912807136/SpannableString](https://github.com/912807136/SpannableString)
2. [https://github.com/sunalong/SpannableStringBuilderDemo](https://github.com/sunalong/SpannableStringBuilderDemo)
3. [https://github.com/iwgang/SimplifySpan](https://github.com/iwgang/SimplifySpan)
4. [https://github.com/JMPergar/AwesomeText](https://github.com/JMPergar/AwesomeText)
5. [https://github.com/lawloretienne/Trestle](https://github.com/lawloretienne/Trestle)
6. [https://github.com/Johnjson/SpannableDemo](https://github.com/Johnjson/SpannableDemo)
7. [https://github.com/lgvalle/autospangridlayout](https://github.com/lgvalle/autospangridlayout)

或许可以使用别的方案？WebView？明天继续研究

1. [Android Webview 高亮的一些问题](http://jiayanjujyj.iteye.com/blog/1010070)
2. 其它

---

`#2`

目前实现的 Demo 结果： 

![](https://raw.githubusercontent.com/Isayes/ShanbayDemo/033850953b33c930e816023c574736b9ada662f8/screenshots/3.gif) 

![](https://github.com/Isayes/ShanbayDemo/blob/master/screenshots/2.gif?raw=true)

1. 对 txt 的内容进行了格式上的预处理（添加了一些 FLAG）
2. 从 txt 中获取了文章的目录

---

`#1 `

目前实现的 Demo 结果：  

![#1](https://raw.githubusercontent.com/Isayes/ShanbayDemo/ecb5ad6fcf9ac37c8915e12f7831a5d0cc37a128/screenshots/1.gif)

1. 做前期思考准备
2. 搭好了 App 的框架，明天下午再继续

## 应用要求 # #

**App 基本要求**   
这是一个简易的分级阅读程序， 基本功能是：  

1. 打开后看到文章列表
2. 点击列表某项，打开文章
3. 文章界面有一个按钮，点击则在文章中高亮在单词列表中出现的单词
4. 文章内容实现两边对齐；
5. 单词实现点击高亮；

譬如 nce4_words 的内容如下：  

单词              级别  
compare         3  
backward        2  
technology      2  
alien           1  

文本内容是
Compared with the alien, our technology is backward。
如果 slide-bar 为 2， 那么只高亮级别在 2 及以下的词，包括 backward, technology,
alien；如果 slide-bar 为 3，那么高亮级别小于等于 3 的词。

---
