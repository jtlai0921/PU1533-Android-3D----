precision mediump float;
uniform sampler2D sLightTexture;//�G�����z���e���
uniform sampler2D sShadowTexture;//�t�B���z���e���
//�����q���I�ۦ⾹�L�Ӫ��Ѽ�
varying vec3 vPosition;
varying vec2 vTextureCoord;
varying vec4 vAmbient;
varying vec4 vDiffuse;
varying vec4 vSpecular;

void main()
{
 	vec4 finalColorLight;
 	vec4 finalColorShadow;
 	
 	finalColorLight = texture2D(sLightTexture,vTextureCoord);
 	finalColorLight = finalColorLight * vAmbient + finalColorLight * vSpecular + finalColorLight * vDiffuse;
 	finalColorShadow = texture2D(sShadowTexture,vTextureCoord);
 	finalColorShadow = finalColorShadow * vec4(0.5,0.5,0.5,1.0);
 	
 	float dis = sqrt(vPosition.x * vPosition.x + vPosition.y * vPosition.y + vPosition.z * vPosition.z);
 	
 	float t = 1.0 - dis / 8.0;
 	gl_FragColor = t * finalColorLight + (1.0 - t) * finalColorShadow;

}