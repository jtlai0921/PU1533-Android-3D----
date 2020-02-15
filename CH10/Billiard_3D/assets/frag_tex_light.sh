//有紋理和光源的著色器
precision mediump float;
varying vec2 vTextureCoord;//接收從頂點著色器過來的參數
varying vec4 ambient;
varying vec4 diffuse;
varying vec4 specular;
uniform sampler2D sTexture;//紋理內容資料
void main()                         
{  
  //給此片元從紋理中取樣出彩色值            
  vec4 finalColor = texture2D(sTexture, vTextureCoord);
  //給此片元彩色值 
  gl_FragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
}