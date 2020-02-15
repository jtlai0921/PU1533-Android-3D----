precision mediump float;
varying vec2 vTextureCoord;//�����q���I�ۦ⾹�L�Ӫ��Ѽ�
uniform sampler2D sTexture;//���z���e���
void main()                         
{  
  //���������q���z�����˥X�m���            
  vec4 finalColor = texture2D(sTexture, vTextureCoord); 
  if(finalColor.r==0.0&&finalColor.g==0.0&&finalColor.b==0.0)
  {
    finalColor.a=0.0;
  }
  //���������m��� 
  gl_FragColor = finalColor;
}              