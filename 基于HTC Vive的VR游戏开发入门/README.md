# 基于 HTC Vive 的 VR 游戏开发入门

学习自 CSDN 学院公开课视频 <http://edu.csdn.net/course/detail/2629>

VR 硬件飞速发展, 但内容却极为稀缺，作为开发者，我们究竟该如何上手 VR 开发？具体到在不同的平台上怎么做？如何做工具选型？延迟、晕眩、全景、人机交互、3D 动画、性能优化... 无一不是痛点. 又该如何将 VR 技术更好地运用到游戏、应用开发以及内容创作中，了解哪些是从事 VR 开发必备技能，又有哪些是为「糟粕」亟需摒弃.

## 1. Vive VR 设计的建议 # #

刷新率必须在 90 FPS 以上, 延时要小(交互延时, 运算延时, 显示延时)  
游戏时长建议 30 分钟以内  
画面纹理不要太复杂  
摄像机不要乱动, 如模拟震荡,颠簸等, 可以有静物参考  
尽量真实, 像真的即可, 场景尽量简单(质量达标, 如小于场景), 避免本来就让人眩晕的场景  
不要有运动冲突, 尽量不要代用户发声  
交互多元, 自然的交互, 行走,抓取,抛掷,躲避,爬行等等  
交互引导非常重要  
多人游戏更好玩, 如对战,协作,对话等  
对延时要求高, 暂时只能设计对网格要求低或局域游戏  
虚拟形象和 IK 动画  
先做原型, 验证验证再验证  

## 2. 转战 VR 开发重点关注点 # #

1.输入输出方式变化

输入输出更自然,直观, 容易让玩家错过信息, 不易判断玩家输入意图, 比如面向某物体时触发输出, 或者利用声音吸引用户注意力

头显(位置和朝向)
控制器 / 手柄(明确的触发指令和位置,朝向信息)
手势(leap motion)
语音控制
自制工具(结合 optitrack)
触感(需要额外硬件配合)
... ...

2.近似无 2D 界面

绝大部分引导都是直接 3D 物体的显示, 比如某个物体与众不同, 类似高亮或虚化
也会附在场景的某个设备中, 以 2D 界面显示, 如 Raw Data 场景中的显示器
天空中飘来五个字, 如果是漂浮的 2D 界面, 距离在 0.5 米以外较好, 也不要太远
以合适大小的字体附在手柄上也是不错的选择
... ...

3.数学, 物理

3D 和 2D 的运动, 朝向, 形变, 成像等计算, 重温线性代数知识
交互时的碰撞, 摩擦, 重力加速等效果
要像真的, 比如一个石头, 抛出去要有石块的手感, 要有生命周期, 防止物体飞远了一直存在

4.音乐音效
5.场景的应用

物尽其用, 尽量在场景中的东西都是有用的, 比如射击游戏中建筑物都可以做掩体
出现的物体尽量都可以交互, 否则应当让玩家很容易区分出来
物体出现的距离比较规律或固定, 比如都是 3 米或 5 米或 10 米距离, 否则眼睛容易疲劳
... ...

6.互联网数据同步

多人时更有趣, 弱联网游戏或者局域网游戏
弱联网即数据同步可以接受比较长的延时, 即秒级别的延时, 像三国杀, 棋牌之类
局域网才适合实时对战,格斗类, 否则延时会严重影响沉浸感
能看到队友或对手, 涉及到位置, 姿态同步以及 IK
... ...

7.SteamVR API

Value Software openvr, 目标是硬件无关的通用 API
IVRSystem - Main interface for display, distortion, tracking, controller, and event access.
IVRChaperone - Provides access to chaperone soft and hard bounds.
IVRCompositor - Allows an application to render 3D content through the VR compositor
IVROverlay - Allows an application to render 2D content through the VR Compositor
IVRRenderModels - Allows an application access to render models.
IVRScreenshots - Allows an application to request and submit screenshots.

## 3. VR 开发资源 # #

<https://www.htcvive.com/cn/develop_portal/>


End.
