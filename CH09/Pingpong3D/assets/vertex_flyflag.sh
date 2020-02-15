uniform mat4 uMVPMatrix; //總變換矩陣
uniform float uStartAngle;//本框起始角度
uniform float uWidthSpan;//水平長度總跨度
attribute vec3 aPosition;  //頂點位置
attribute vec2 aTexCoor;    //頂點紋理座標
varying vec2 vTextureCoord;  //用於傳遞給片元著色器的變數
void main()     
{            
   //計算X向波浪                		
   float angleSpanH=4.0*3.14159265;//水平角度總跨度   
   float startX=-uWidthSpan/2.0;//起始X座標
   //根據水平角度總跨度、水平長度總跨度及目前點X座標折算出目前點X座標對應的角度
   float currAngle=uStartAngle+((aPosition.x-startX)/uWidthSpan)*angleSpanH;
   float tz=sin(currAngle)*0.1;      
   
   //根據總變換矩陣計算此次繪制此頂點位置
   gl_Position = uMVPMatrix * vec4(aPosition.x,aPosition.y,tz,1); 
   vTextureCoord = aTexCoor;//將接收的紋理座標傳遞給片元著色器
}                      