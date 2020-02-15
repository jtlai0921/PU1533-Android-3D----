uniform mat4 uMVPMatrix; //總變換矩陣
uniform mat4 uMMatrix; //基本變換矩陣
attribute vec3 aPosition;  //頂點位置
attribute vec4 aColor;  //頂點彩色
varying vec4 vColor;  //用於傳遞給片元著色器的變數
//varying float currAngle;  //目前角度
varying vec3 vPosition;
void main()     
{     
   //根據總變換矩陣計算此次繪制此頂點位置                        		
   gl_Position = uMVPMatrix * vec4(aPosition,1);    
   //vec4 currP=uMMatrix*vec4(aPosition,1); 
   //currZ=currP.z;
 //  vec3 currP=gl_Position.xyz;
   //currP=aPosition-vec3(0.0,0.0,0.0);
   //currP=normalize(currP);
   //currAngle=dot(currP,vec3(0.0,1.0,0.0)-vec3(0.0,0.0,0.0));
   //currAngle=acos(currAngle);
   //currAngle=degrees(currAngle);
     //vPosition=currP.xyz;
    vPosition=aPosition;
   vColor = aColor;//將接收的彩色傳遞給片元著色器
   
}                 