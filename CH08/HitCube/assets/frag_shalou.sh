precision mediump float;
varying vec2 vTextureCoord;//�����q���I�ۦ⾹�L�Ӫ��Ѽ�
uniform sampler2D sTexture;//���z���e���
varying vec3 vPosition;
uniform float uKaiShiId; 
uniform float uR; 
uniform float uShaLouCount; 
void main()                         
{  
  //���������q���z�����˥X�m���            
  vec4 finalColor = texture2D(sTexture, vTextureCoord); 
  if(vPosition.y>0.0&&finalColor.g>0.9&&finalColor.r<0.1)
  {
    finalColor.a=0.0;
  }
   if(uKaiShiId==1.0){
   if(vPosition.y>0.0&&finalColor.g>0.9&&finalColor.r<0.1)
   {
       if(abs(vPosition.y)>=uR-uR/20.0*uShaLouCount)
       {
           finalColor.a=1.0;
            
       } 
     
   }
   
   if(vPosition.y<0.0&&finalColor.g>0.9&&finalColor.r<0.1)
   {
        if(abs(vPosition.y)>=uR-uR/20.0*uShaLouCount)
       {
           finalColor.a=0.0;
            
       } 
   } 
  }
  //���������m��� 
  gl_FragColor = finalColor;
}              