precision mediump float;
uniform highp int isShadow;
uniform sampler2D sTexture;//紋理內容資料
//接收從頂點著色器過來的參數
varying vec4 vAmbient;
varying vec4 vDiffuse;
varying vec4 vSpecular;
varying vec2 vTextureCoord;
varying vec3 vPosition;
void main()                         
{    
   //將計算出的彩色給此片元
   vec4 finalColor=texture2D(sTexture, vTextureCoord);
   if(isShadow == 1)
   {
   		gl_FragColor = vec4(0.2,0.2,0.2,0.7);
   }
   else
   {
   		//給此片元彩色值
	   	gl_FragColor = finalColor*vAmbient+finalColor*vSpecular+finalColor*vDiffuse;
   }
}