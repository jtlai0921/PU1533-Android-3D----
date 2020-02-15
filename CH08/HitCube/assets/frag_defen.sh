precision mediump float;
varying vec2 vTextureCoord;//接收從頂點著色器過來的參數
uniform sampler2D sTexture;//紋理內容資料
uniform float uAlpha;
void main()                         
{  
  //給此片元從紋理中取樣出彩色值            
  vec4 finalColor = texture2D(sTexture, vTextureCoord); 
  //給此片元彩色值 
  if(finalColor.a<0.1)
  {
    finalColor.a=0.0;
  }else{
   finalColor.a=uAlpha;
  }
  //gl_FragColor = vec4(finalColor.rgb,0.5);
  gl_FragColor = finalColor;
}              