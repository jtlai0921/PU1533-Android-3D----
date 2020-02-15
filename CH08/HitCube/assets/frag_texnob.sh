precision mediump float;
varying vec2 vTextureCoord;//接收從頂點著色器過來的參數
uniform sampler2D sTexture;//紋理內容資料
void main()                         
{  
  //給此片元從紋理中取樣出彩色值            
  vec4 finalColor = texture2D(sTexture, vTextureCoord); 
  if(finalColor.r==0.0&&finalColor.g==0.0&&finalColor.b==0.0)
  {
    finalColor.a=0.0;
  }
  //給此片元彩色值 
  gl_FragColor = finalColor;
}              