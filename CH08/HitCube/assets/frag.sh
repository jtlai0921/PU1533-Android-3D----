precision mediump float;
varying vec2 vTextureCoord; //接收從頂點著色器過來的參數
uniform sampler2D sTexture;//紋理內容資料
varying float vAlpha;
void main()                         
{           
   //給此片元從紋理中取樣出彩色值            
   vec4 fcolor=texture2D(sTexture, vTextureCoord); 
   gl_FragColor = vec4(fcolor.rgb,vAlpha);
}              