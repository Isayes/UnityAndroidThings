# 对 Unity 中常用类之间关系的调研

下面是 Unity 常用类间的关系图

![](http://images2015.cnblogs.com/blog/1098699/201703/1098699-20170309201210938-1693138886.png)

Unity 任何要绑定在 GameObject 上的脚本都必须继承自 MonoBehaviour. 许多常用的类都属于组件, 如 Transform,Camera,Light 和 Animation 等. 所有的 GameObject 上都绑定了一个 Transform 组件, 且该组件无法被删除.

下面是 MonoBehaviour 常用方法

![](http://images2015.cnblogs.com/blog/1098699/201703/1098699-20170309201221250-2104449481.png)

下面是 Object 体系结构图

![](http://images2015.cnblogs.com/blog/1098699/201703/1098699-20170309210450375-1419744347.png)

End.
