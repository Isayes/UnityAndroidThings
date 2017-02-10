Unity 官方的手册上有一张说明图 `Script Lifecycle Flowchart`, 网址是 → [点这里](https://docs.unity3d.com/Manual/ExecutionOrder.html), 图片如下 :

![](http://images2015.cnblogs.com/blog/1098699/201702/1098699-20170210135323869-345354810.jpg)

Reset is called in the Editor when the script is attached or reset. → Reset 是在用户点击检视面板的 Reset 按钮或者首次添加该组件时被调用, 此函数只在编辑模式下被调用.

Start is only ever called once for a given script. → Start 仅在 Update 函数第一次被调用前调用, 只会调用一次.

The physics cycle may happen more than once per frame if the fixed time step is less than the actual frame update time. → 如果固定时间步长小于实际帧更新时间, 那么每一帧物理周期将会可能发生不止一次.

 If a coroutine has yielded previously but us now due to resume then execution takes place during this part of the update. → 如果一个协程之前已经 yield 了, 但是现在由于恢复了, 那么将执行剩下的部分.

 OnDrawGizmos is only called while working in the editor. → OnDrawGizmos 只在编辑模式下被调用.

 OnGUI is called multiple time per frame update. → OnGUI 在每一帧更新的时候调用多次.

 OnApplicationPause is called after the frame where the pause occurs but issues another frame before actually pausing. → OnApplicationPause 在程序检测到暂停的时候, 会在帧的结尾处被调用.

 OnDisable is called only when the script was disabled during the frame. OnEnable will be called if it is enabled again. → OnDisable 在脚本失效时被调用, 被销毁时也会被调用. 如果再启用的话, OnEnable 会再一次被调用.

## 1. 生命周期的划分 # #

Unity 脚本生命周期分为几个大部分 :

`编辑 → 初始化 → 游戏逻辑 → 渲染 → GUI → 卸载模块 Teardown`

![](http://img.blog.csdn.net/20140119212730296?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcWl0aWFuNjc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

## 2. 几个重要的方法 # #

Unity 脚本从创建到销毁是具有一个完整的生命周期的, 主要的调用顺序如下 : Awake → OnEnable → Start → Update → LateUpdate → OnDisable → OnDestroy

```html
1. Awake() 当脚本实例被载入时调用, 一般进行一些初始化赋值. 脚本唤醒，此方法为系统执行的第一个方法，用于脚本的初始化，在脚本的生命周期中只执行一次.
2. OnEnable() 脚本可用时被调用、如果脚本是不可用的，将不会被调用
3. Start() 在 Awake() 方法之后、Update() 方法之前执行，并且只执行一次.
4. FixedUpdate() 固定更新，固定更新常用于移动模型等操作.
5. Update() 正常更新，用于更新逻辑。此方法每帧都会由系统自动调用一次.
6. LateUpdate() 推迟更新，此方法在 Update() 方法执行完后调用，同样每一帧都调用.
7. OnGUI () 渲染和处理 GUI 事件时调用, 用于渲染图形界面, 每一帧都调用. ( 如果使用了 NGUI，这个生命周期的事情你就不用考虑了)
8. OnDisable() 当对象变为不可用或非激活状态时此函数被调用.
9. OnDestroy() 当前脚本 MonoBehaviour 销毁时调用.
```

以上是单个脚本的生命周期执行顺序, Unity 不支持多线程, 但是 Unity 可以同时创建很多脚本, 并且可以分别绑定在不同的游戏对象上, 他们分别执行各自生命周期时又如同多线程并行执行脚本, 当有多个脚本时 Awake() Update() 等函数执行顺序是如何的? 与单个脚本执行顺序一样.

建议一般在 Awake 中做一些初始化, 在 Start 中获取游戏对象等, 在实际项目中应该灵活应对

### 三种 update 方法的区别 # ##

```html
1. Update() 每帧都会执行一次;
2. FixedUpdate() 以固定的时间间隔执行, 不受帧率影响, 默认 0.02s, 如果卡帧了 Update 就不会再执行, 而 FixedUpdate 会继续执行. 这里的时间可以在 `Edit>Project Setting>Time>FixedTimeStep` 中修改, 主要用于处理物理逻辑, 比如 RigidBody.
3. LateUpdate() 是在所有 Update 函数调用后被调用. 比如相机跟随就可以用这个函数, 即人物移动在 Update 中实现, 相机跟随在 LateUpdate() 中实现, Play 后的效果是: 角色移动发生在前, 相机移动紧随其后.
```

```html
1. FixedUpdate 比 Update 函数更经常被调用，它可以在一帧内多次调用。如果帧率比较低，它可能会被调用多次，如果帧率比较高，它可能不会被调用。所有的物理计算和刷新都会在 FixedUpdate 之后立即进行。如果要在 FixedUpdate 中执行移动计算，你不需要使用 Time.deltaTime 来乘以你的值，因为 FixedUpdate 是基于一个可靠的定时器被调用，独立于帧率之外。
2. Update 函数会每帧调用一次，它是每帧最重要的刷新函数。
3. LateUpdate 每帧 Update 函数结束后，LateUpdate 就会被调用. 当 LateUpdate 开始时，任何 Update 内的计算都会结束。一个常见的 LateUpdate 应用就是第三人称视角跟随，如果你让你的主角移动和转向在 Update 函数内进行，你可以把所有的摄像机移动和旋转计算放在 LateUpdate 里面，这可以保证主角在摄像机跟随前已经移动结束。
```

### 渲染 # ##

```html
- OnPreCull: 在相机剔除场景之前调用此函数。相机可见的对象取决于剔除。OnPreCull 函数调用发生在剔除之前。
- OnBecameVisible/OnBecameInvisible: 在对象对于相机可见/不可见时调用此函数。
- OnWillRenderObject: 如果对象可见，则为每个相机调用一次此函数。
- OnPreRender: 在相机开始渲染场景之前调用此函数。
- OnRenderObject: 在完成所有常规场景渲染后调用此函数。此时，可使用 GL 类或 Graphics.DrawMeshNow 绘制自定义几何图形。
- OnPostRender: 在相机完成场景渲染后调用此函数。
- OnRenderImage（仅限专业版）： 在完成场景渲染后调用此函数，以便对屏幕图像进行后处理。
- OnGUI: 在每帧上多次调用此函数，以响应 GUI 事件。程序首先将处理 Layout 和 Repaint 事件，然后再处理每个输入事件的 Layout 和 keyboard/鼠标事件。
- OnDrawGizmos 用于在场景视图中绘制小图示 (Gizmos)，以实现可视化目的。
```

### 协同程序 # ##

正常的协程刷新是在 Update 函数返回时运行。一个协程是一个能够暂停其执行（yield）直到 YieldInstruction 结束后的一个函数。下面是协程的不同用法：

```html
1. yield：协程会在下一帧调用完所有的 Update 函数后持续执行。
2. yield WaitForSeconds：在下一帧调用完所有的 Update 函数后，一个特定的延迟之后执行。
3. yield WaitForFixedUpdate：在所有的脚本已经调用完所有的 FixedUpdate 后执行。
4. yield WWW：一个 WWW 的下载完成后执行。
5. yield StartCoroutine：协程链，会先等待自定义协程完成后执行。
```

`整理学习来自 Unity 官网和若干他人博客(太多了不一一列举)`

End.