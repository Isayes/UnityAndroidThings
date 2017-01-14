Shader "Custom/Shader001" {
	Properties {
		_Color ("Color", Color) = (1,1,1,1)
		_MainTex ("Albedo (RGB)", 2D) = "white" {}
		_Glossiness ("Smoothness", Range(0,1)) = 0.5
		_Metallic ("Metallic", Range(0,1)) = 0.0
	}
	SubShader {
		Tags { "RenderType"="Opaque" }
		LOD 200
		
		CGPROGRAM
		// Physically based Standard lighting model, and enable shadows on all light types
		#pragma surface surf Standard fullforwardshadows vertex:vert

		// Use shader model 3.0 target, to get nicer looking lighting
		#pragma target 3.0

		sampler2D _MainTex;

		struct Input {
			float2 uv_MainTex;
		};

		half _Glossiness;
		half _Metallic;
		fixed4 _Color;

		//增加的顶点控制代码  
		void vert(inout appdata_full v)  
		{  
		    //x,y,z 为随时间变化的量，数值可以自己随意设置  
		    float x =  sin(_SinTime*20);   
		    float y =  sin(_SinTime*20);  
		    float z =  sin(_SinTime*20);  
		    //定义一个 4*4 的矩阵类型，将旋转和平移包含进去  
		    float4x4 m = {1,0,0,x,  
		                  0,1,0,y,  
		                  0,0,1,z,  
		                  0,0,0,1};  
		    //对顶点进行变换  
		    v.vertex = mul(m,v.vertex);  
		} 

		void surf (Input IN, inout SurfaceOutputStandard o) {
			// Albedo comes from a texture tinted by color
			fixed4 c = tex2D (_MainTex, IN.uv_MainTex) * _Color;
			o.Albedo = c.rgb;
			// Metallic and smoothness come from slider variables
			o.Metallic = _Metallic;
			o.Smoothness = _Glossiness;
			o.Alpha = c.a;
		}

		ENDCG
	}
	FallBack "Diffuse"
}
