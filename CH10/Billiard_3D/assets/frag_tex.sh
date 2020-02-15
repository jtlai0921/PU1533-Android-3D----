precision mediump float;
varying vec2 vTextureCoord;//接收從頂點著色器過來的參數
uniform sampler2D sTexture;//紋理內容資料
void main()                         
{           
   //給此片元從紋理中取樣出彩色值            
   gl_FragColor = texture2D(sTexture, vTextureCoord); 
}              