# 一个最简单的顶点/片元着色器

```c
Shader "Custom/Chapter5-SimpleShader" {

	SubShader {
		Pass{
			CGPROGRAM

      // 两条非常重要的编译指令
			#pragma vertex vert // 定点着色器
			#pragma fragment frag // 片元着色器

      // 这一步是把顶点坐标从模型空间转换到裁剪空间
			float4 vert(float4 v : POSITION) : SV_POSITION {
				return mul(UNITY_MATRIX_MVP, v);
			}

			fixed4 frag() : SV_Target{
				return fixed4(1.0,1.0,1.0,1.0);
			}

			ENDCG
		}
	}
}

```

1. vert , 顶点着色器代码, 逐顶点执行, 函数的输入 v 包含了这个顶点的位置, 返回值是该顶点在裁剪空间中的位置, POSITION : 把模型的顶点坐标填充到输入参数中, SV_POSITION : 顶点着色器的输出是裁剪空间中的顶点坐标
2. frag , SV_Target : 把用户的输出颜色存储到一个渲染目标中, 这里将输出到默认的帧缓存中, 片元着色器输出的颜色的每个分量范围是[0,1].

使用结构体来定义顶点着色器的输入参数
```
struct a2v {
    float4 vertex : POSITION;
    float3 normal : NORMAL;
    float4 texcoord : TEXCOORD0;
};
```

- 使用 POSITION 语义得到了模型的顶点位置坐标;
- 使用  NORMAL 表示是模型空间的法线方向;
- 使用 TEXCOORD0 表示模型空间的第一套纹理;

```c
SubShader {
		Pass{
			CGPROGRAM

			#pragma vertex vert
			#pragma fragment frag

      //  a2v 把数据从应用阶段传到顶点着色器
			struct a2v{
				float4 vertex : POSITION;
				float3 normal : NORMAL;
				float4 texcoord : TEXCOORD0;
			};

			float4 vert(a2v v) : SV_POSITION {
				return mul(UNITY_MATRIX_MVP, v.vertex);
			}

			fixed4 frag() : SV_Target{
				return fixed4(1.0,1.0,1.0,1.0);
			}

			ENDCG
		}
	}
```

填充到 POSITION / TANGENT / NORMAL 这些语句中的数据是从哪里来的呢? 在 Unity 中, 它们是由使用该材质 Mesh Render 组件提供的, 在每帧调用 Draw Call 的时候, Mesh Render 组件会把它负责渲染的模型数据发送给 Unity Shader.

一个模型通常包括了一组三角面片, 每个三角面片由三个顶点组成, 每个顶点又包含了一些数据, 例如顶点位置 / 法线 / 切线 / 纹理坐标 / 顶点颜色等

## 顶点着色器和片元着色器之间的通信 # #

我们希望从顶点着色器中输出一些数据传递给片元着色器, 比如模型的法线 / 纹理坐标等数据, 这就涉及到顶点/片元着色器之间的通信.
我们可以再定义一个结构体用来定义顶点着色器的输出

```c
Shader "Custom/Shader002" {

	SubShader {
		Pass{
			CGPROGRAM

			#pragma vertex vert
			#pragma fragment frag

			struct a2v{
				float4 vertex : POSITION;
				float3 normal : NORMAL;
				float4 texcoord : TEXCOORD0;
			};

			// 使用一个结构体来定义顶点着色器的输出
			struct v2f {
				// SV_POSITION 语义告诉 Unity , pos 里包括了顶点在裁剪空间的位置信息
				float4 pos : SV_POSITION;
				// COLOR0 语义告诉 Unity 用于存储颜色信息
				fixed3 color : COLOR0;
			};

			v2f vert(a2v v) {
				// 生命输出结构
				v2f o;
				o.pos = mul(UNITY_MATRIX_MVP, v.vertex);
				o.color = v.normal * 0.5 + fixed3(0.5,0.5,0.5);
				return o;
			}

			fixed4 frag(v2f i) : SV_Target{
				// 将插值后的 i.color 显示到屏幕上
				return fixed4(i.color, 1.0);
			}

			ENDCG
		}
	}
}
```

![](http://ww3.sinaimg.cn/large/a53846c3gw1fbfuwjwksrj20sk0fwt9p.jpg)
