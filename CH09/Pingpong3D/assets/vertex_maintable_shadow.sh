uniform int isShadow;
uniform mat4 uMVPMatrix;
uniform mat4 uMMatrix;
uniform mat4 uMProjCameraMatrix; //��v�B��v���s�զX�x�}
uniform vec3 uCamera;
uniform vec3 uLightLocation;
attribute vec3 aPosition;
attribute vec3 aNormal;
attribute vec2 aTexCoor;
varying vec2 vTextureCoord;
varying vec4 vAmbient;
varying vec4 vDiffuse;
varying vec4 vSpecular;
varying vec3 vPosition;

//�w��������p�⪺��k
void pointLight(				//�w��������p�⪺��k
  in vec3 normal,				//�k�V�q
  inout vec4 ambient,			//���ҥ��̲ױj��
  inout vec4 diffuse,			//���g���̲ױj��
  inout vec4 specular,			//�譱���̲ױj��
  in vec3 lightLocation,		//������m
  in vec4 lightAmbient,			//���ҥ��j��
  in vec4 lightDiffuse,			//���g���j��
  in vec4 lightSpecular			//�譱���j��
){
  ambient=lightAmbient;			//�����o�X���ҥ����̲ױj��  
  vec3 normalTarget=aPosition+normal;	//�p���ܴ��᪺�k�V�q
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(aPosition,1)).xyz;
  newNormal=normalize(newNormal); 		//��k�V�q�W���
  //�p��q���I����v�����V�q
  vec3 eye= normalize(uCamera-(uMMatrix*vec4(aPosition,1)).xyz);  
  //�p��q���I�������m���V�qvp
  vec3 vp= normalize(lightLocation-(uMMatrix*vec4(aPosition,1)).xyz);  
  vp=normalize(vp);//�榡��vp
  vec3 halfVector=normalize(vp+eye);	//�D���u�P���u���b�V�q    
  float shininess=50.0;					//���W�סA�V�p�V����
  float nDotViewPosition=max(0.0,dot(newNormal,vp)); 	//�D�k�V�q�Pvp���I�n�P0���̤j��
  diffuse=lightDiffuse*nDotViewPosition;				//�p�ⴲ�g�����̲ױj��
  float nDotViewHalfVector=dot(newNormal,halfVector);	//�k�u�P�b�V�q���I�n 
  float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess)); 	//�譱�Ϯg���j�צ]�l
  specular=lightSpecular*powerFactor;    			//�p���譱�����̲ױj��
}

void main()
{
	if(isShadow == 1)
	{
		vec3 A = vec3(0.0,0.1,0.0);
		vec3 n = vec3(0.0,1.0,0.0);
		vec3 S = uLightLocation;
		vec3 V = (uMMatrix * vec4(aPosition,1)).xyz;
		vec3 VL = S + (V - S) * (dot(n,(A - S)) / dot(n,(V - S)));
		gl_Position = uMProjCameraMatrix * vec4(VL,1);
	}
	else
	{
		gl_Position = uMVPMatrix * vec4(aPosition,1);
	}
	vec4 ambientTemp=vec4(0.0,0.0,0.0,0.0);
    vec4 diffuseTemp=vec4(0.0,0.0,0.0,0.0);
    vec4 specularTemp=vec4(0.0,0.0,0.0,0.0);
	pointLight(normalize(aNormal),ambientTemp,diffuseTemp,specularTemp,uLightLocation,vec4(0.8,0.8,0.8,1.0),vec4(0.7,0.7,0.7,1.0),vec4(0.6,0.6,0.6,1.0));
	vAmbient=ambientTemp;
    vDiffuse=diffuseTemp;
    vSpecular=specularTemp;
    vTextureCoord = aTexCoor;
    vPosition = aPosition;
}