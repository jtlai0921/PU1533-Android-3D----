precision mediump float;
uniform int isShadowFrag;//�O�_ø��v
uniform sampler2D sTextureBall;//��y���z���e���
uniform sampler2D sTextureTable;//�ୱ���z���e���
varying vec2 vTextureCoord; //�����q���I�ۦ⾹�L�Ӫ��Ѽ�
varying vec4 ambient;
varying vec4 diffuse;
varying vec4 specular;
varying vec3 vMMPosition;//�ܴ��᪺��m
void main()                         
{    
   	//�N�p��X���m�⵹������
   	vec4 finalColor;

   	if(isShadowFrag==0)
   	{
   		//ø��y�����A���z�q�y���z����
	    finalColor=texture2D(sTextureBall, vTextureCoord);
	    gl_FragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;//���������m���
   	}
   	else
   	{   		
   		//��m�ܴ��᪺x�Bz�y�Э�
	   	float x = vMMPosition.x;
	   	float z = vMMPosition.z;
	   	//�Y�G��m���b�y�x���A�N�����˱�
	   	if(   		
	   		//��m���b�x�Τ�
	   		x < -16.08 ||
	   		x > 16.08 ||
	   		z < -10.08 ||
	   		z > 10.08 ||
	   		//��m�b�|��
	   		x+z < -24.88 ||
	   		z-x < -24.88 ||
	   		z-x > 24.88 ||
	   		x+z > 24.88
	   	)
	   	{
	   		finalColor=texture2D(sTextureTable, vTextureCoord); 
	   		finalColor.a = 0.0;
	   		gl_FragColor = finalColor*ambient*1.6;//���������m���
	   	}
	   	else
	   	{
	   		//ø��v
		     finalColor=texture2D(sTextureTable, vTextureCoord); 
		     gl_FragColor = finalColor*ambient*1.4;//���������m���
	   	}
   	}
}              