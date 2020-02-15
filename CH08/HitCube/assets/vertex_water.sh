uniform mat4 uMVPMatrix; //總變換矩陣
uniform mat4 uMMatrix;//基本變換矩陣
attribute vec3 aPosition;  //頂點位置
attribute vec2 aTexCoor;    //頂點紋理座標
varying vec2 vTextureCoord;  //用於傳遞給片元著色器的變數
void main()     
{  
   //根據總變換矩陣計算此次繪制此頂點位置                            		
   //gl_Position = uMVPMatrix * vec4(aPosition,1); 
    vec4 temp_Position=uMMatrix * vec4(aPosition,1); 
    float currX=temp_Position.x; 
    float currY=temp_Position.y;
    float currZ=temp_Position.z;
    float angle;
	if(currX<-0.42)
    {
    angle=(currX/0.7+1.0)*150.0;
    }else if(currX<-0.14)
    {
   // angle=(currX-0.28)/0.7*65.0;
   angle=60.0-(currX/0.7+0.6)*150.0;
    }
    else if(currX<0.14)
    {
   // angle=(currX+0.28)/0.7*65.0;
   angle=(currX/0.7+0.2)*150.0;
    }else if(currX<0.42)
    {
    //angle=(currX+0.28)/0.7*65.0;
    angle=60.0-(currX/0.7-0.2)*150.0;
    }else if(currX<0.7)
    {
    //angle=currX/0.7*65.0;
    angle=(currX/0.7-0.6)*150.0;
    }
    
    angle=radians(angle); 
    currY=cos(angle)*aPosition.y;
    currZ=sin(angle)*aPosition.z;
 
    gl_Position = uMVPMatrix * vec4(aPosition.x,currY,currZ,1);  
   //將頂點的紋理座標傳給片元著色器
   vTextureCoord=aTexCoor;
}                 