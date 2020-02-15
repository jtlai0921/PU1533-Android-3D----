precision mediump float;
varying vec2 vTextureCoord;//接收從頂點著色器過來的參數
uniform sampler2D sTexture;//紋理內容資料
uniform int isDown;//按鈕是否被按下的標志位，0表示沒有按下，1表示按下
void main()                         
{
	//給此片元從紋理中取樣出彩色值            
	vec4 finalColor = texture2D(sTexture, vTextureCoord); 
	//根據按鈕是否被按下，改變最終彩色的a值
	if(isDown == 1){
   		finalColor.a *= 0.3;	//按鈕按下時的透明度
	}
	else {
		finalColor.a *= 0.8;	//按鈕沒有被按下時的透明度
	}
   	//為片元彩色給予值
   	gl_FragColor = finalColor;
}