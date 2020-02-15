precision mediump float;
varying vec2 vTextureCoord;//接收從頂點著色器過來的參數
uniform sampler2D sTexture;//紋理內容資料
uniform float currZLH;
varying float currZ;
void main()                         
{ 
  //給此片元從紋理中取樣出彩色值            
  vec4 finalColor = texture2D(sTexture, vTextureCoord); 
  if(finalColor.a<0.1)
  {
      float dis=abs(currZLH-currZ);
      if(dis<6.0)  
      {  
         if(dis<=0.75)
         {
           finalColor=vec4(1.0,0.0,0.0,1.0);
         }else if(dis<=1.5)
         {
           finalColor=vec4(0.858,0.0,0.0,1.0);
         }else if(dis<=2.25)
         {
           finalColor=vec4(0.715,0.0,0.0,1.0);
         }else if(dis<=3.0)
         {
           finalColor=vec4(0.572,0.0,0.0,1.0);
         }else if(dis<=3.75)
         {
           finalColor=vec4(0.429,0.0,0.0,1.0);
         }else if(dis<=4.5)
         {
           finalColor=vec4(0.286,0.0,0.0,1.0);
         }else if(dis<=5.25)
         {
           finalColor=vec4(0.143,0.0,0.0,1.0);
         }else if(dis<=6.0)
         {
           finalColor=vec4(0.0,0.0,0.0,1.0);
         }
         
        
      }
  }

     //給此片元彩色值 
     gl_FragColor = finalColor;
}              