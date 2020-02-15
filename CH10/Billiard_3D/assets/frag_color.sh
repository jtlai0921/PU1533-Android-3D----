precision mediump float;
varying  vec4 aaColor; //接收從頂點著色器過來的參數
void main()                         
{
	vec4 finalColor = aaColor;
	finalColor.a = 0.9;//改變彩虹條的透明度
	gl_FragColor = finalColor;//給此片元彩色值
}              