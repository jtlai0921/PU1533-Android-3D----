precision mediump float;
uniform float uR; 
uniform float uCount; 
//varying float currAngle;
varying vec3 vPosition;
varying vec4 vColor;  //用於傳遞給片元著色器的變數
void main()                         
{ 
      vec4 finalColor;
      //float dis=abs(currZ);
      vec3 currP=vPosition-vec3(0.0,0.0,0.0);
      currP=normalize(currP);
      vec3 currY=vec3(0.0,1.0,0.0)-vec3(0.0,0.0,0.0);
      currY=normalize(currY);
      float currAngle=dot(currP,currY);
      currAngle=acos(currAngle);
      currAngle=degrees(currAngle);
      float tempR=(1.0-sqrt(vPosition.x*vPosition.x+vPosition.y*vPosition.y)/uR);
      tempR=tempR*0.2+0.8;
      if(vPosition.x>0.0)
      {
        currAngle=360.0-currAngle;
      }
     // if(abs(currAngle-30.0)<1.0||abs(currAngle-90.0)<1.0||abs//(currAngle-150.0)<1.0)  
  //    {  
   //      finalColor=vec4(1.0,0.0,0.0,0.0);   
   //   }else if(abs(currAngle-210.0)<1.0||abs(currAngle-270.0)//<1.0||abs(currAngle-330.0)<1.0)
  //    {
   //    finalColor=vec4(1.0,0.0,0.0,0.0);
  //    }else
      if(currAngle>30.0&&currAngle-30.0<15.0*uCount)
      {  
         finalColor=vColor*tempR;
       
      }else if(currAngle>90.0&&currAngle-90.0<15.0*uCount)
      {
        finalColor=vColor*tempR;
      }else if(currAngle>150.0&&currAngle-150.0<15.0*uCount)
      {
        finalColor=vColor*tempR;
      }else if(currAngle>210.0&&currAngle-210.0<15.0*uCount)
      {
        finalColor=vColor*tempR;
      }else if(currAngle>270.0&&currAngle-270.0<15.0*uCount)
      {
        finalColor=vColor*tempR;
      }else if(currAngle>330.0&&currAngle-330.0<15.0*uCount)
      {
        finalColor=vColor*tempR;
      }else if(currAngle>=0.0&&uCount>2.0&&currAngle-0.0<15.0*(uCount-2.0))
      {
        finalColor=vColor*tempR;
      }else
      {
        finalColor=vColor;
      }
      

     //給此片元彩色值  
     gl_FragColor = finalColor;
}              