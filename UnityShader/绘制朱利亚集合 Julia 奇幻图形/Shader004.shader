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