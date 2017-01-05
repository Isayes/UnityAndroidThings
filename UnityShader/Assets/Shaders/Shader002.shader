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