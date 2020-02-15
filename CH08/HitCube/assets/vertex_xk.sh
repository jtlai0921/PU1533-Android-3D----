//星空著色器
uniform mat4 uMVPMatrix; //總變換矩陣
uniform float uPointSize;//點尺寸
attribute vec3 aPosition;  //頂點位置

void main()     
{     
   //根據總變換矩陣計算此次繪制此頂點位置                         		
   gl_Position = uMVPMatrix * vec4(aPosition,1); 
   gl_PointSize=uPointSize;
}                 