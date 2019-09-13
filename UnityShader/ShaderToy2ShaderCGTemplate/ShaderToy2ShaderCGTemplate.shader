Shader "Custom/ShaderToy2ShaderCGTemplate" {
	
	Properties {
    }
	
    CGINCLUDE
	
    #include "UnityCG.cginc"
	#include "ShaderToyDefines.cginc"
    #pragma target 3.0

	// Properties

    struct v2f {
        float4 pos : SV_POSITION;
        float4 scrPos : TEXCOORD0;
    };

    v2f vert(appdata_base v) {
        v2f o;
        o.pos = UnityObjectToClipPos (v.vertex);
        o.scrPos = ComputeScreenPos(o.pos);
        return o;
    }

    vec4 main(vec2 fragCoord);

    fixed4 frag(v2f _iParam) : COLOR0 {
        return main(gl_FragCoord);
    }
	
	//------------------------------------------
	// Some other functions
	//------------------------------------------

    vec4 main(vec2 fragCoord) {
		// TODO ShaderToy mainImage Code
    }

    ENDCG

    SubShader {
        Pass {

            CGPROGRAM
            #pragma vertex vert
            #pragma fragment frag
            #pragma fragmentoption ARB_precision_hint_fastest
            ENDCG

        }
    }

    FallBack Off
}
