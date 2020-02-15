precision mediump float;
varying  vec4 vColor; //接收從頂點著色器過來的參數
void main() {  
   gl_FragColor = vColor;//給此片元彩色值
}