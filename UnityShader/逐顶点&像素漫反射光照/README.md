# 逐顶点/像素漫反射光照

漫反射光照符合兰伯特定律 : 反射光线的强度 与 表面法线 和 光源方向 之间的夹角的余弦值成正比 .

计算机图形第一定律 : 如果它看起来是对的 , 那么它就是对的 .

逐顶点光照的计算量往往要小于逐像素光照 .

逐顶点光照依赖于线性插值来得到像素光照 , 当光照模型中有非线性的计算的时候 , 逐顶点光照就会出问题 , 例如计算高光反射 .

逐顶点光照会在渲染图元内部对顶点进行插值 , 渲染图元内部的颜色总是暗于顶点处的最高颜色 , 在某些情况下会产生明显的棱角现象 .

```c
// Upgrade NOTE: replaced '_World2Object' with 'unity_WorldToObject'

Shader "Custom/DiffuseVertexLevel" {

	Properties {
		_Diffuse("Diffuse", Color) = (1, 1, 1, 1)
	}

	SubShader {
		Pass {
			Tags { "LightMode"="ForwardBase" }

			CGPROGRAM
			#pragma vertex vert
			#pragma fragment frag
			#include "Lighting.cginc"

			fixed4 _Diffuse;

			struct a2v {
				float4 vertex : POSITION;
				float3 normal : NORMAL;
			};

			struct v2f {
				float4 pos : SV_POSITION;
				fixed3 color : COLOR;
			};

			// 逐顶点的漫反射光照
			v2f vert(a2v v) {
				v2f o;
				o.pos = mul(UNITY_MATRIX_MVP, v.vertex);
				fixed3 ambient = UNITY_LIGHTMODEL_AMBIENT.xyz;
				fixed3 worldNormal = normalize(mul(v.normal, (float3x3)unity_WorldToObject));
				fixed3 worldLight = normalize(_WorldSpaceLightPos0.xyz);
				fixed3 diffuse = _LightColor0.rgb * _Diffuse.rgb * saturate(dot(worldNormal, worldLight));
				o.color = ambient + diffuse; // 环境光 + 漫射光
				return o;
			}

			fixed4 frag(v2f f) : SV_Target {
				return fixed4(f.color, 1.0);
			}

			ENDCG
		}
	}
	FallBack "Diffuse"
}

```

逐像素光照可以得到更加平滑的光照效果 , 但是有一个问题存在 : 当光照无法到达的区域 , 模型的外观通常是全黑的 , 没有任何的额明暗变化 , 失去了模型细节表现 , 因此有一种改善技术提出 , 即 , 半兰布特光照模型(没有任何物理依据 , 只是一个视觉加强技术) .

```c
Shader "Custom/DiffusePixelLevel" {

	Properties {
		_Diffuse("Diffuse", Color) = (1,1,1,1)
	}

	SubShader {
		Pass {

			CGPROGRAM
			#pragma vertex vert
			#pragma fragment frag
			#include "Lighting.cginc"

			fixed4 _Diffuse;

			struct a2v {
				float4 vertex : POSITION;
				float3 normal : NORMAL;
			};

			struct v2f {
				float4 pos : SV_POSITION;
				float3 worldNormal : TEXCOORD0;
			};

			v2f vert(a2v v) {
				v2f o;
				o.pos = mul(UNITY_MATRIX_MVP, v.vertex);
				o.worldNormal = mul(v.normal, (float3x3)_World2Object);
				return o;
			}

			// 逐像素漫反射光照
			fixed4 frag(v2f f) : SV_Target {
				fixed3 ambient = UNITY_LIGHTMODEL_AMBIENT.xyz;
				fixed3 worldNormal = normalize(f.worldNormal);
				fixed3 worldLightDir = normalize(_WorldSpaceLightPos0.xyz);
				fixed3 diffuse = _LightColor0 * _Diffuse.rgb * (dot(worldNormal, worldLightDir) * 0.5 + 0.5); // 半兰伯特光照模型
				fixed3 color = ambient + diffuse;
				return fixed4(color, 1.0);
			}

			ENDCG
		}
	}
	FallBack "Diffuse"
}

```
End.
