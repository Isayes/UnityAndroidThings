# 绘制朱利亚集合 Julia 奇幻图形

## 1. 理论知识 # #

朱利亚集合是一个在复平面上形成分形的点的集合。以法国数学家加斯顿·朱利亚（Gaston Julia）的名字命名。朱利亚集合可以由下式进行反复迭代得到：

![](https://imgsa.baidu.com/baike/s%3D90/sign=3a36a0f5f51f3a295ec8d9ce99253428/ac4bd11373f08202eccd90144efbfbedab641bb9.jpg)  对于固定的复数 c，取某一 z 值（如 z = z0 ），可以得到序列

![](https://imgsa.baidu.com/baike/s%3D238/sign=8b4428235643fbf2c12ca120887eca1e/8435e5dde71190ef3bde9ff4cb1b9d16fdfa60f7.jpg)

这一序列可能反散于无穷大或始终处于某一范围之内并收敛于某一值。我们将使其不扩散的 z 值的集合称为朱利亚集合。

Julia 集是动力系统的一个斥子（repeller）。斥子的反义是吸引子，所谓吸引子，就是存在一个开集 V 和闭集 F 和一个映射 f，如果满足 f(F)=F，而且 V 里面所有点 x，都有 f(x) 迭代次数越多，结果离 F 越远；则称 F 为 f 的吸引子，V 为 F 的吸引域。类似的，对一个闭不变子集 F，如果 F 附近的所有点经迭代后远离 F，则 F 是 f 的斥子。所以为了产生 Julia 集图形，需要按下面的算法：

对每一个像素点 (x, y)，对其做 Z(n+1) = Z(n)^2+c 的迭代，如果迭代结果 Zo 值超出范围，那么就对该点停止迭代，迭代次数 o 就是迭代的结果（也就像远离 J 的距离）。然后对每个不同的 o’ 用不同的颜色表示，结果就能得到非常漂亮的图像。参考了 : <http://blog.sina.com.cn/s/blog_4ce0162301013df0.html>

![](http://s9.sinaimg.cn/middle/4ce01623gbfd07f66fbe8&690)

## 2. Unity Shader 实现 # #

材质效果图 :

![](http://ww2.sinaimg.cn/large/a53846c3gw1fbh0tyifsbg20aw05xjx7.gif)

脚本 :

```csharp
using UnityEngine;
using System.Collections;

[ExecuteInEditMode]
public class ScriptShader004 : MonoBehaviour {

	public Material mat;

	// Use this for initialization
	void Start () {

	}

	// Update is called once per frame
	void Update () {

	}

	void OnRenderImage(RenderTexture src, RenderTexture dest){
		Graphics.Blit (src, dest, mat);
	}
}
```

Shader004 :

```c
Shader "Custom/Shader004" {

	Properties {
		// 定义基本属性 , 可以从编辑里面进行设置的变量
		_MainTex("Texture", 2D) = "white"{}
		cx("cx",range(-0.8, 0.375)) = -0.8
		cy("cy",range(-1,-1)) = 0.156
		scale("scale", range(1,3)) = 1.6
	}

	SubShader {

		Tags {"RenderType" = "Opaque"}
		LOD 100
		Pass {
			CGPROGRAM

			#pragma vertex vert
			#pragma fragment frag
			#include "UnityCG.cginc"

			struct appdata{
				float4 vertex : POSITION;
				float2 uv : TEXCOORD0;
			};

			struct v2f{
				float2 uv : TEXCOORD0;
				float4 vertex : SV_POSITION;
			};

			sampler2D _MainTex;
			float4 _MainTex_ST;
			// 复数 c 的实部
			float cx;
			// 复数 c 的虚部
			float cy;
			float scale;

			v2f vert(appdata v){
				v2f o;
				o.vertex = mul(UNITY_MATRIX_MVP, v.vertex);
				o.uv = v.uv.xy * _MainTex_ST.xy + _MainTex_ST.zw;
				return o;
			}

			fixed4 frag(v2f i) : SV_Target{
				// 迭代初始值的实部
				float ax = scale * (0.5 - i.uv.x) / 0.5;
				// 迭代初始值的虚部
				float ay = scale * (0.5 - i.uv.y) / 0.5;
				float juliaValue;
				// 进行 200 次迭代
				for(int index = 0; index<200; index++){
					// 迭代函数实现 , 先计算复数乘法 , 然后加上 c
					float _ax = ax*ax - ay*ay;
					float _ay = ay*ax + ax*ay;
					ax = _ax + cx;
					ay = _ay + cy;
					// 计算模长 , 超过阈值则认为不属于 Julia 集 , 返回黑色
					juliaValue = sqrt(ax * ax + ay*ay);
					if(juliaValue > 100){
						return fixed4(0,0,0,1);
					}
				}
				// Julia 集内部的点 , 需要根据 Julia 值来计算颜色 , 这个可以自己设置颜色
				return fixed4(
				juliaValue,
				(fixed)(sin(_Time * 100)+1)/2,
				(fixed)(cos(_Time * 50)+1)/2,
				1
				);
			}
			ENDCG
		}
	}
}
```
![](http://ww4.sinaimg.cn/mw690/a53846c3gw1fbh16j8hfgg20aw05x0xz.gif)

![](http://ww3.sinaimg.cn/large/a53846c3gw1fbh1db9v4kg20aw05x4qp.gif)

End.
