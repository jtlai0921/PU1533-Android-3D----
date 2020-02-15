precision mediump float;
varying vec2 vTextureCoord;//�����q���I�ۦ⾹�L�Ӫ��Ѽ�
uniform sampler2D sTexture;//���z���e���
uniform float uProgress;
varying vec3 vPosition;
void main()                         
{   
	if(uProgress>vPosition.x)
	{   
   		//���������q���z�����˥X�m���            
   		gl_FragColor = texture2D(sTexture, vTextureCoord);
   	}else
   	{
   		gl_FragColor = vec4(texture2D(sTexture, vTextureCoord).xyz,0.0);
   	} 
}              