uniform int isShadow;//�O�_ø��v
uniform mat4 uMVPMatrix; //�`�ܴ��x�}
uniform mat4 uMMatrix; //�ܴ��x�}
uniform mat4 uMCameraMatrix; //��v���x�}
uniform mat4 uMProjMatrix; //��v�x�}
uniform vec3 uLightLocation;	//������m
uniform vec3 uCamera;	//��v����m
attribute vec3 aPosition;  //���I��m
attribute vec3 aNormal;    //���I�k�V�q
attribute vec2 aTexCoor;    //���I���z�y��
varying vec2 vTextureCoord;  //�Ω�ǻ��������ۦ⾹���ܼ�
varying vec4 ambient;
varying vec4 diffuse;
varying vec4 specular; 
varying vec3 vMMPosition;//�ܴ��᪺��m
//�w��������p�⪺��k
void pointLight
(
  in vec3 normal,//�k�V�q
  inout vec4 ambient,//���ҥ����q
  inout vec4 diffuse,//���g�����q
  inout vec4 specular,//�譱�Ϯg�����q  
  in vec3 uLightLocation,	//������m
  in vec4 lightAmbient,//�������ҥ����q
  in vec4 lightDiffuse,//�������g�����q
  in vec4 lightSpecular//�����譱�Ϯg�����q
)
{
  //�p���ܴ��᪺�k�V�q
  vec3 normalTarget=aPosition+normal;
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(aPosition,1)).xyz;
  newNormal=normalize(newNormal);
  
  //�p��q���I����v�����V�q
  vec3 eye= normalize(uCamera-(uMMatrix*vec4(aPosition,1)).xyz);
  
  //������m
  vec3 lLocation=uLightLocation;
  
  //�p��q���I�������m���V�q
  vec3 vp= normalize(lLocation-(uMMatrix*vec4(aPosition,1)).xyz);
  //�p����I�M������m���Z��
  float d=length(vp);
  //�榡��vp
  vp=normalize(vp);
  vec3 halfVector=normalize(vp+eye);//���̫G��V
  
  float shininess=100.0;//���W�סA�V�p�V����
  
  float nDotViewPosition;//�k�u�P����V���I�n
  float nDotViewHalfVector;//�k�u�P���̫G��V���I�n
  float powerFactor;//�譱�Ϯg�����]�l
  
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
   {//ø��v�A�p��p���v���I��m
      vec3 A=vec3(0.0,-1.936,0.0);//��v�����W���@�I�y��
      vec3 n=vec3(0.0,1.0,0.0);//��v�����k�V�q
      vec3 S=uLightLocation; //������m     
      vec3 V=(uMMatrix*vec4(aPosition,1)).xyz;  //�g�ѥ����M�����ܴ��᪺�I���y��    
      vec3 VL=S+(V-S)*(dot(n,(A-S))/dot(n,(V-S)));//�D�o����v�I�y��
      gl_Position = uMProjMatrix*uMCameraMatrix*vec4(VL,1); //�ھ��`�ܴ��x�}�p�⦹��ø����I��m
      vMMPositionTmp = VL;//�ܴ��᪺��m
   }
   else
   {
	  gl_Position = uMVPMatrix * vec4(aPosition,1); //�ھ��`�ܴ��x�}�p�⦹��ø����I��m
	  vMMPositionTmp = (uMMatrix*vec4(aPosition,1)).xyz;//�ܴ��᪺��m
   }
   
   vec4 ambientTemp=vec4(0.0,0.0,0.0,0.0);
   vec4 diffuseTemp=vec4(0.0,0.0,0.0,0.0);
   vec4 specularTemp=vec4(0.0,0.0,0.0,0.0);
   
   pointLight(normalize(aNormal),ambientTemp,diffuseTemp,specularTemp,uLightLocation,vec4(0.4,0.4,0.4,1.0),vec4(0.7,0.7,0.7,1.0),vec4(0.3,0.3,0.3,1.0));
   
   ambient=ambientTemp;
   diffuse=diffuseTemp;
   specular=specularTemp;
   
   vTextureCoord = aTexCoor;//�N���������z�y�жǻ��������ۦ⾹
   vMMPosition = vMMPositionTmp;
}                      