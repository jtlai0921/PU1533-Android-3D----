precision mediump float;
varying vec2 vTextureCoord;//�����q���I�ۦ⾹�L�Ӫ��Ѽ�
uniform sampler2D sTexture;//���z���e���
void main()                         
{  
  //���������q���z�����˥X�m���            
  vec4 finalColor = texture2D(sTexture, vTextureCoord); 
  //���������m��� 
  //gl_FragColor = finalColor;
    if(finalColor.a>0.1)
   {
    gl_FragColor = vec4(0.0,0.0,1.0,0.6);
   }else
   {
   gl_FragColor = vec4(0.0,0.0,1.0,0.0);
   }
}              