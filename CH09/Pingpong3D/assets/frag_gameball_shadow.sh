precision mediump float;
varying vec2 vTextureCoord;
varying vec4 vAmbient;
varying vec4 vDiffuse;
varying vec4 vSpecular;
uniform sampler2D sTexture;//紋理內容資料
varying float alphaTemp;
varying float isShadowVary; //陰影繪制標志

void main()
{
	if(isShadowVary==0.0)
	{						//繪制物體本身
	   	vec4 finalColor = texture2D(sTexture, vTextureCoord); 
		vec4 finalColor1 = finalColor*vAmbient+finalColor*vDiffuse+finalColor*vSpecular;
		gl_FragColor = vec4(finalColor1.x,finalColor1.y,finalColor1.z,alphaTemp);
   	}else if(isShadowVary==1.0)
   	{							  	    
	    gl_FragColor = vec4(0.2,0.2,0.2,1.0);//繪制陰影
   	}else if(isShadowVary==2.0)
   	{
   		gl_FragColor = vec4(0.2,0.2,0.2,0.0);//繪制出界陰影
   	}
}