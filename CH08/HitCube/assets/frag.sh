precision mediump float;
varying vec2 vTextureCoord; //�����q���I�ۦ⾹�L�Ӫ��Ѽ�
uniform sampler2D sTexture;//���z���e���
varying float vAlpha;
void main()                         
{           
   //���������q���z�����˥X�m���            
   vec4 fcolor=texture2D(sTexture, vTextureCoord); 
   gl_FragColor = vec4(fcolor.rgb,vAlpha);
}              