precision mediump float;
varying vec2 vTextureCoord;//接收從頂點著色器過來的參數
uniform sampler2D sTexture;//紋理內容資料
uniform int isTransparent;//是否透明
void main()                         
{
  	//給此片元從紋理中取樣出彩色值            
	vec4 finalColor = texture2D(sTexture, vTextureCoord); 
	//根據是否透明，改變最終彩色的a值
	if(isTransparent != 0){
   		finalColor.a *= 0.8;	//儀表板的透明度
	}
	//為片元彩色給予值
   	gl_FragColor = finalColor;
}