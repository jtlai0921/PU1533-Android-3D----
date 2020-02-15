precision mediump float;
varying vec2 vTextureCoord;//接收從頂點著色器過來的參數
uniform sampler2D sTexture;//紋理內容資料
varying vec3 vPosition;
uniform float uKaiShiId; 
uniform float uR; 
uniform float uShaLouCount; 
void main()                         
{  
  //給此片元從紋理中取樣出彩色值            
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
  //給此片元彩色值 
  gl_FragColor = finalColor;
}              