precision mediump float;
varying vec2 vTextureCoord;
varying vec4 vAmbient;
varying vec4 vDiffuse;
varying vec4 vSpecular;
uniform sampler2D sTexture;//���z���e���
varying float alphaTemp;
varying float isShadowVary; //���vø��Ч�

void main()
{
	if(isShadowVary==0.0)
	{						//ø��饻��
	   	vec4 finalColor = texture2D(sTexture, vTextureCoord); 
		vec4 finalColor1 = finalColor*vAmbient+finalColor*vDiffuse+finalColor*vSpecular;
		gl_FragColor = vec4(finalColor1.x,finalColor1.y,finalColor1.z,alphaTemp);
   	}else if(isShadowVary==1.0)
   	{							  	    
	    gl_FragColor = vec4(0.2,0.2,0.2,1.0);//ø��v
   	}else if(isShadowVary==2.0)
   	{
   		gl_FragColor = vec4(0.2,0.2,0.2,0.0);//ø��X�ɳ��v
   	}
}