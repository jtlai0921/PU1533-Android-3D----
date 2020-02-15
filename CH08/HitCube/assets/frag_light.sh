precision mediump float;
varying vec2 vTextureCoord;//�����q���I�ۦ⾹�L�Ӫ��Ѽ�
uniform sampler2D sTexture;//���z���e���
uniform float uA;
uniform float uId;
void main()                         
{  
  //���������q���z�����˥X�m���            
  vec4 finalColor = texture2D(sTexture, vTextureCoord); 
  
  if(uId<0.1)
  {
    finalColor.a=finalColor.a/30.0*uA*1.5;
    
  }else if(uId<1.1){
  
   finalColor.a= finalColor.a-finalColor.a/200.0*(uA-30.0);
  }else if(uId<2.1){
  
     finalColor.a= finalColor.a/2.0-finalColor.a/400.0*(uA-130.0);
  }
  if(finalColor.a<0.1)
  {
    finalColor.a=0.0;
  }
  //���������m��� 
  gl_FragColor = finalColor;
}              