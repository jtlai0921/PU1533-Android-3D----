precision mediump float;
varying vec2 vTextureCoord;//接收從頂點著色器過來的參數
uniform sampler2D sTexture;//紋理內容資料
uniform float uProgress;
varying vec3 vPosition;
void main()                         
{   
	if(uProgress>vPosition.x)
	{   
   		//給此片元從紋理中取樣出彩色值            
   		gl_FragColor = texture2D(sTexture, vTextureCoord);
   	}else
   	{
   		gl_FragColor = vec4(texture2D(sTexture, vTextureCoord).xyz,0.0);
   	} 
}              