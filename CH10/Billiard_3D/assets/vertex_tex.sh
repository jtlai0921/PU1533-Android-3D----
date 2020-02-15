uniform mat4 uMVPMatrix; //總變換矩陣
attribute vec3 aPosition;  //頂點位置
attribute vec2 aTexCoor;    //頂點紋理座標
varying vec2 vTextureCoord;  //用於傳遞給片元著色器的變數
void main()     
{                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1); //根據總變換矩陣計算此次繪制此頂點位置
   vTextureCoord = aTexCoor;//將接收的紋理座標傳遞給片元著色器
}