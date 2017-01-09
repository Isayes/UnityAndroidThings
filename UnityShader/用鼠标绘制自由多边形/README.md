# 用鼠标绘制自由多边形

实现 : 支持在 Plane 上用鼠标点击，确定多边形顶点，并且绘制多边形的边，在内部填充颜色 ;

Plane 带有碰撞体 , 使用鼠标选取位置的时候涉及到碰撞检测 .

ScriptShader005.cs 脚本实现鼠标点击和向 Shader 传递信息的功能 .  
Shader005.shader 实现多边形的绘制功能 . [传送门 → 绘制多边形的函数](https://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html)

效果图 :  

![](http://ww1.sinaimg.cn/mw690/a53846c3gw1fbkgb7czs6g20bb0c7jyp.gif)

MainCamera 关联的脚本 ScriptShader005.cs :

```csharp
using UnityEngine;
using System.Collections;

[ExecuteInEditMode]
public class ScriptShader005 : MonoBehaviour
{
	// 绑定材质
	public Material mat;

	// 存储获取的 3D 坐标
	Vector3[] worldPos;

	// 存储待绘制的多边形顶点屏幕坐标
	Vector4[] screenPos;

	// 多边形顶点总数
	int maxPointNum = 10;

	// 当前已经获得的顶点数
	int currentPointNum = 0;

	// 传递顶点数量给 Shader
	int pointNum2Shader = 0;

	// 是否处于顶点获取过程
	bool InSelection = true;

	void Start ()
	{
		worldPos = new Vector3[maxPointNum];
		screenPos = new Vector4[maxPointNum];
	}

	void Update ()
	{
		// 传递顶点屏幕位置信息给 shader
		mat.SetVectorArray ("Value", screenPos);

		// 传递顶点数量给 shader
		mat.SetInt ("PointNum", pointNum2Shader);

		// 使用摄像机发射一条射线 , 以获取要选择的 3D 位置
		Ray ray = Camera.main.ScreenPointToRay (Input.mousePosition);
		RaycastHit hit;
		if (Physics.Raycast (ray, out hit, 100)) {
			Debug.DrawLine (ray.origin, hit.point, Color.red);
		}

		// 利用鼠标点击来获取位置信息
		if (Input.GetMouseButtonDown (0) && InSelection) {
			if (currentPointNum < maxPointNum) {
				currentPointNum++;
				pointNum2Shader++;
				worldPos [currentPointNum - 1] = hit.point;
				Vector3 v3 = Camera.main.WorldToScreenPoint (worldPos [currentPointNum - 1]);
				screenPos [currentPointNum - 1] = new Vector4 (v3.x, v3.y, v3.z, 0);
			} else {
				// 超过了多边形顶点总数就不能继续获取
				InSelection = false;
			}
		}

		// 实时更新已选择的 3D 点的屏幕位置
		for (int i = 0; i < maxPointNum; i++) {
			Vector3 v3 = Camera.main.WorldToScreenPoint (worldPos [i]);
			screenPos [i] = new Vector4 (v3.x, v3.y, v3.z, 0);
		}

		// 检测是否有 3D 点移动到了摄像机后面 , 如果有 , 则停止绘制
		for (int i = 0; i < currentPointNum; i++) {
			if (Vector3.Dot (worldPos [i] - Camera.main.transform.position, Camera.main.transform.forward) <= 0) {
				pointNum2Shader = 0;
				break;
			}
			pointNum2Shader = currentPointNum;
		}

	}

	// 抓取当前的渲染图像进行处理
	void OnRenderImage (RenderTexture src, RenderTexture dest)
	{
		Graphics.Blit (src, dest, mat);
	}
}

```

![](http://ww3.sinaimg.cn/mw690/a53846c3gw1fbkgblh6n5j20me0g2q58.jpg)

Shader005.shader :

```c
Shader "Custom/Shader005" {

	Properties {
		// 定义基本属性 , 可以从编辑器中进行设置的变量

	}

	CGINCLUDE

	// 从应用程序传入顶点函数的数据结构定义
	struct a2v {
		float4 vertex : POSITION;
		float2 uv : TEXCOORD0;
	};

	// 从顶点函数传入片元函数的数据结构定义
	struct v2f {
		float2 uv : TEXCOORD0;
		float4 vertex : SV_POSITION;
	};

	// 定义贴图变量
	sampler2D _MainTex;

	// 定义与脚本进行通信的变量 , 10 个顶点
	vector Value[10];

	int PointNum = 0;


	// 计算两点之间的距离的函数
	float Dis(float4 v1, float4 v2) {
		return sqrt(pow(v1.x - v2.x,2) + pow(v1.y - v2.y,2));
	}

	// 绘制线段
	bool DrawLineSegment(float4 p1, float4 p2, float lineWidth, v2f i) {
		float4 center = float4((p1.x + p2.x)/2, (p1.y + p2.y)/2, 0, 0);
		// 计算点到直线的距离
		float d = abs((p2.y - p1.y) * i.vertex.x +
			(p1.x - p2.x) * i.vertex.y +
			p2.x * p1.y - p2.y * p1.x) / sqrt(pow(p2.y - p1.y, 2) + pow(p1.x - p2.x, 2));
		// 小于或者等于线宽的一般的时候就属于直线的范围
		float lineLength = sqrt(pow(p1.x - p2.x, 2) + pow(p1.y - p2.y, 2));
		if(d <= lineWidth/2 && Dis(i.vertex, center) < lineLength/2) {
			return true;
		}
		return false;
	}

	// 绘制多边形
	bool pnpoly(int nvert, float4 vert[10], float testx, float testy) {
		int i, j;
		bool c = false;
		float vertx[10];
		float verty[10];

		for(int n=0; n<nvert; n++) {
			vertx[n] = vert[n].x;
			verty[n] = vert[n].y;
		}

		for(i=0,j=nvert-1; i<nvert; j=i++){
			if(((verty[i] > testy) != (verty[j] > testy)) && (testx < (vertx[j] - vertx[i]) * (testy - verty[i]) / (verty[j] - verty[i]) + vertx[i])) {
				c = !c;
			}
		}
		return c;
	}

	v2f vert (a2v v) {
		v2f o;
		// 将物体顶点从模型空间转换到摄像机裁剪空间
		// 简写方式 : o.vertex = UnityObjectToClipPos(v.vertex);
		o.vertex = mul(UNITY_MATRIX_MVP, v.vertex);
		// 2D UV 坐标变换 , 简写方式 : o.uv = TRANSFORM_TEX(v.uv, _MainTex);
		// o.uv = v.uv.xy * _MainTex_ST.xy + _MainTex_ST.zw
		return o;
	}

	fixed4 frag(v2f i) : SV_Target {
		// 绘制多边形顶点
		for(int j=0; j< PointNum; j++) {
			if(Dis(i.vertex, Value[j]) < 10/2) {
				return fixed4(1,0,0,0.5);
			}
		}

		// 绘制多边形的边
		for(int k=0; k<PointNum; k++) {
			if(k == PointNum - 1) {
				if(DrawLineSegment(Value[k], Value[0], 2,i)) {
					return fixed4(1, 1, 0, 0.5);
				}
			} else {
				if(DrawLineSegment(Value[k], Value[k+1], 2, i)) {
					return fixed4(1, 1, 0, 0.5);
				}
			}
		}

		// 填充多边形的内部
		if(pnpoly(PointNum, Value, i.vertex.x, i.vertex.y)) {
			return fixed4(0, 1, 0, 0.3);
		}
		return fixed4(0, 0, 0, 0);
	}

	ENDCG

	SubShader {
		Tags { "RenderType"="Opaque" }
		LOD 200

		Pass {
			// 选取 Alpha 混合方式
			Blend SrcAlpha OneMinusSrcAlpha
			// 在 CGPROGRAM 代码块中写自己的处理过程
			CGPROGRAM
			// 定义顶点函数和片元函数的入口分别为 vert 和 frag
			#pragma vertex vert
			#pragma fragment frag
			#include "UnityCG.cginc"
			ENDCG
		}
	}
}

```

点到直线距离公式 :

![](http://ww2.sinaimg.cn/mw690/a53846c3gw1fbkgbdae2og204n01a3y9.gif)

> 参考 : http://blog.csdn.net/zzlyw/article/details/53992048

End.
