precision mediump float;
varying vec2 vTextureCoord;//接收從頂點著色器過來的參數
uniform sampler2D sTexture;//紋理內容資料
uniform float uA;
uniform float uId;
void main()                         
{  
  //給此片元從紋理中取樣出彩色值            
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
  //給此片元彩色值 
  gl_FragColor = finalColor;
}              