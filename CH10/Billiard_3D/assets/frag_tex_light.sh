//�����z�M�������ۦ⾹
precision mediump float;
varying vec2 vTextureCoord;//�����q���I�ۦ⾹�L�Ӫ��Ѽ�
varying vec4 ambient;
varying vec4 diffuse;
varying vec4 specular;
uniform sampler2D sTexture;//���z���e���
void main()                         
{  
  //���������q���z�����˥X�m���            
  vec4 finalColor = texture2D(sTexture, vTextureCoord);
  //���������m��� 
  gl_FragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
}