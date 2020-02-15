//�����z�M�������ۦ⾹
uniform mat4 uMVPMatrix; //�`�ܴ��x�}
uniform mat4 uMMatrix; //�ܴ��x�}
uniform vec3 uCamera;	//��v����m
uniform vec3 uLightLocationSun;	//�Ӷ�������m
attribute vec3 aPosition;  //���I��m
attribute vec2 aTexCoor;    //���I���z�y��
attribute vec3 aNormal;    //�k�V�q
varying vec2 vTextureCoord;  //�Ω�ǻ��������ۦ⾹���ܼ�
varying vec4 ambient;
varying vec4 diffuse;
varying vec4 specular;

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
  
  //�p��q�����I����v�����V�q
  vec3 eye= normalize(uCamera-(uMMatrix*vec4(aPosition,1)).xyz);
  
  //������m
  vec3 lLocation=uLightLocation;
  
  //�p��q�����I�������m���V�q
  vec3 vp= normalize(lLocation-(uMMatrix*vec4(aPosition,1)).xyz);
  //�p������I�M������m���Z��
  float d=length(vp);
  //�榡��vp
  vp=normalize(vp);
  vec3 halfVector=normalize(vp+eye);//���̫G��V
  
  float shininess=50.0;//���W�סA�V�p�V����
  
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
   gl_Position = uMVPMatrix * vec4(aPosition,1); //�ھ��`�ܴ��x�}�p�⦹��ø����I��m  
   
   vec4 ambientTemp=vec4(0.0,0.0,0.0,0.0);
   vec4 diffuseTemp=vec4(0.0,0.0,0.0,0.0);
   vec4 specularTemp=vec4(0.0,0.0,0.0,0.0);   
   
   pointLight(normalize(aNormal),ambientTemp,diffuseTemp,specularTemp,uLightLocationSun,vec4(0.6,0.6,0.6,1.0),vec4(0.7,0.7,0.7,1.0),vec4(0.3,0.3,0.3,1.0));
   
   ambient=ambientTemp;
   diffuse=diffuseTemp;
   specular=specularTemp;
   
   //�N���I�����z�y�жǵ������ۦ⾹
   vTextureCoord=aTexCoor;
}                 