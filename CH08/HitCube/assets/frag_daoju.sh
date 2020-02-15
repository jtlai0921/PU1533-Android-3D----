precision mediump float;
uniform float uColorId;
uniform float uDaoJuA;
uniform float uDaoJuALevel;
varying vec4 vAmbient;//接收從頂點著色器過來的環境光分量
varying vec4 vDiffuse;//接收從頂點著色器過來的散射光分量
varying vec4 vSpecular;//接收從頂點著色器過來的鏡面反射光分量
void main() {  
    vec4 finalColor;
   if(uColorId==0.0){
    finalColor = vec4(1.0,1.0,1.0,1.0);//給此片元彩色值
   }else if(uColorId==1.0)
   {
   finalColor= vec4(1.0,1.0,1.0,1.0);//給此片元彩色值
   }else if(uColorId==2.0)
   {
   finalColor= vec4(0.0,1.0,0.0,1.0);//給此片元彩色值
   }else
   {
    finalColor= vec4(1.0,1.0,0.0,1.0);//給此片元彩色值
   }
   if(uDaoJuALevel==0.0)
   {
    finalColor.a=0.2*uDaoJuA;
   }else if(uDaoJuALevel==1.0)
   {
   finalColor.a=1.0-0.05*(uDaoJuA-5.0);
   }
  
  gl_FragColor=finalColor*vAmbient + finalColor*vDiffuse + finalColor*vSpecular;
}