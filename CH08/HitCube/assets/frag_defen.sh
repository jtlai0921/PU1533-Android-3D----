precision mediump float;
varying vec2 vTextureCoord;//�����q���I�ۦ⾹�L�Ӫ��Ѽ�
uniform sampler2D sTexture;//���z���e���
uniform float uAlpha;
void main()                         
{  
  //���������q���z�����˥X�m���            
  vec4 finalColor = texture2D(sTexture, vTextureCoord); 
  //���������m��� 
  if(finalColor.a<0.1)
  {
    finalColor.a=0.0;
  }else{
   finalColor.a=uAlpha;
  }
  //gl_FragColor = vec4(finalColor.rgb,0.5);
  gl_FragColor = finalColor;
}              