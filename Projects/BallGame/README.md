# 颠球的动作

## 左右移动盘子的脚本 # #

```csharp
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MovePlate : MonoBehaviour
{
	// 控制左右移动的盘子的速度, 默认 10.0
	public float speed = 10.0f;

	void Update ()
	{
		// 得到水平方向的输入, 左右键控制移动方向, 按下右方向该函数返回正值, 反之负值
		float h = Input.GetAxis ("Horizontal");
		// 移动脚本绑定的游戏对象, 移动的距离
		transform.Translate (Vector3.right * h * speed * Time.deltaTime);
	}
}

```

脚本效果 :

![](http://images2015.cnblogs.com/blog/1098699/201703/1098699-20170310165832217-2008210471.gif)

## 控制乒乓球弹跳逻辑的脚本 # #

```csharp
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class BallController : MonoBehaviour
{
	// 控制球和盘子发生碰撞时候的受力大小
	public float thrust = 40.0f;

	private Rigidbody rb;

	// Use this for initialization
	void Start ()
	{
		rb = GetComponent<Rigidbody> ();
	}

	// 当球下落到 y 值小于 -10 时, 销毁 Ball 对象并退出程序
	void Update ()
	{
		if (transform.position.y < -10) {
			Destroy (gameObject);
			Application.Quit ();
		}
	}

	// 处理碰撞体间碰撞的事件
	void OnCollisionEnter (Collision collision)
	{
		// 为刚体组件添加了一个位于 XY 平面上随机方向和大小的力
		rb.AddForce (new Vector3 (Random.Range (-0.2f, 0.2f), 1.0f, 0) * thrust);
	}
}

```

## 让摄像机始终对着目标 # #

Unity 官方案例原书中的 "Import Package → Scripts" 在我的新版 Unity 编辑器里面没有找到, 经探索, 发现新的 SmoothFollow.cs 是在 "Import Package → Utility" 里. 此脚本是 Unity 自带的资源, 主要功能是让摄像机始终对着目标对象(Target 属性指定, 这里为 Ball).

![](http://images2015.cnblogs.com/blog/1098699/201703/1098699-20170310170023514-473984478.png)
![](http://images2015.cnblogs.com/blog/1098699/201703/1098699-20170310165941357-1734308863.png)

左右移动方向键接住球后随机向左或向右弹跳, 但摄像机保持了始终对着球.

最终效果 :

![](http://images2015.cnblogs.com/blog/1098699/201703/1098699-20170310165903092-1204943304.gif)
