precision mediump float;
uniform highp int isShadow;
uniform sampler2D sTexture;//���z���e���
//�����q���I�ۦ⾹�L�Ӫ��Ѽ�
varying vec4 vAmbient;
varying vec4 vDiffuse;
varying vec4 vSpecular;
varying vec2 vTextureCoord;
varying vec3 vPosition;

void main()                         
{    
   //�N�p��X���m�⵹������
   vec4 finalColor=texture2D(sTexture, vTextureCoord);
   if(isShadow == 1)
   {
   		if((vPosition.y > 4.9 && vPosition.y < 5.0) || (vPosition.x < -4.1) || (vPosition.x > 4.05))
   		{
   			gl_FragColor = vec4(0.2,0.2,0.2,0.7);
   		}
   		else
   		{
   			discard;
   		}
   }
   else
   {
   		if(finalColor.r < 0.5 && finalColor.r > 0.3 && finalColor.g < 0.5 && finalColor.g > 0.3 && finalColor.b < 0.5 && finalColor.b > 0.3)
	    { 
	   		discard;
	    }
        else
        {
	   		//���������m���
	   		gl_FragColor = finalColor*vAmbient+finalColor*vSpecular+finalColor*vDiffuse;
        }
   }
}   