uniform int isShadow;//是否繪制陰影
uniform mat4 uMVPMatrix; //總變換矩陣
uniform mat4 uMMatrix; //變換矩陣
uniform mat4 uMCameraMatrix; //攝影機矩陣
uniform mat4 uMProjMatrix; //投影矩陣
uniform vec3 uLightLocation;	//光源位置
uniform vec3 uCamera;	//攝影機位置
attribute vec3 aPosition;  //頂點位置
attribute vec3 aNormal;    //頂點法向量
attribute vec2 aTexCoor;    //頂點紋理座標
varying vec2 vTextureCoord;  //用於傳遞給片元著色器的變數
varying vec4 ambient;
varying vec4 diffuse;
varying vec4 specular; 
varying vec3 vMMPosition;//變換後的位置
//定位光光源計算的方法
void pointLight
(
  in vec3 normal,//法向量
  inout vec4 ambient,//環境光分量
  inout vec4 diffuse,//散射光分量
  inout vec4 specular,//鏡面反射光分量  
  in vec3 uLightLocation,	//光源位置
  in vec4 lightAmbient,//光的環境光分量
  in vec4 lightDiffuse,//光的散射光分量
  in vec4 lightSpecular//光的鏡面反射光分量
)
{
  //計算變換後的法向量
  vec3 normalTarget=aPosition+normal;
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(aPosition,1)).xyz;
  newNormal=normalize(newNormal);
  
  //計算從表面點到攝影機的向量
  vec3 eye= normalize(uCamera-(uMMatrix*vec4(aPosition,1)).xyz);
  
  //光源位置
  vec3 lLocation=uLightLocation;
  
  //計算從表面點到光源位置的向量
  vec3 vp= normalize(lLocation-(uMMatrix*vec4(aPosition,1)).xyz);
  //計算表面點和光源位置的距離
  float d=length(vp);
  //格式化vp
  vp=normalize(vp);
  vec3 halfVector=normalize(vp+eye);//光最亮方向
  
  float shininess=100.0;//粗糙度，越小越光滑
  
  float nDotViewPosition;//法線與光方向的點積
  float nDotViewHalfVector;//法線與光最亮方向的點積
  float powerFactor;//鏡面反射光冪因子
  
  nDotViewPosition=max(0.0,dot(newNormal,vp));
  nDotViewHalfVector=max(0.0,dot(newNormal,halfVector));
  
  if(nDotViewPosition==0.0)
  {
     powerFactor=0.0;
  }
  else
  {
     powerFactor=pow(nDotViewHalfVector,shininess);
  }
  
  ambient+=lightAmbient;
  diffuse+=lightDiffuse*nDotViewPosition;
  specular=lightSpecular*powerFactor;  
}

void main()     
{
   vec3 vMMPositionTmp = vec3(0.0, 0.0, 0.0);
   if(isShadow==1)
   {//繪制本影，計算小陰影頂點位置
      vec3 A=vec3(0.0,-1.936,0.0);//投影平面上任一點座標
      vec3 n=vec3(0.0,1.0,0.0);//投影平面法向量
      vec3 S=uLightLocation; //光源位置     
      vec3 V=(uMMatrix*vec4(aPosition,1)).xyz;  //經由平移和旋轉變換後的點的座標    
      vec3 VL=S+(V-S)*(dot(n,(A-S))/dot(n,(V-S)));//求得的投影點座標
      gl_Position = uMProjMatrix*uMCameraMatrix*vec4(VL,1); //根據總變換矩陣計算此次繪制此頂點位置
      vMMPositionTmp = VL;//變換後的位置
   }
   else
   {
	  gl_Position = uMVPMatrix * vec4(aPosition,1); //根據總變換矩陣計算此次繪制此頂點位置
	  vMMPositionTmp = (uMMatrix*vec4(aPosition,1)).xyz;//變換後的位置
   }
   
   vec4 ambientTemp=vec4(0.0,0.0,0.0,0.0);
   vec4 diffuseTemp=vec4(0.0,0.0,0.0,0.0);
   vec4 specularTemp=vec4(0.0,0.0,0.0,0.0);
   
   pointLight(normalize(aNormal),ambientTemp,diffuseTemp,specularTemp,uLightLocation,vec4(0.4,0.4,0.4,1.0),vec4(0.7,0.7,0.7,1.0),vec4(0.3,0.3,0.3,1.0));
   
   ambient=ambientTemp;
   diffuse=diffuseTemp;
   specular=specularTemp;
   
   vTextureCoord = aTexCoor;//將接收的紋理座標傳遞給片元著色器
   vMMPosition = vMMPositionTmp;
}                      