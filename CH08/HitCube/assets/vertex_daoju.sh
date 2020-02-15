uniform mat4 uMVPMatrix; //�`�ܴ��x�}
uniform mat4 uMMatrix; //�ܴ��x�}
uniform vec3 uLightLocation;//������m
uniform vec3 uCamera;	//��v����m
attribute vec3 aPosition;  //���I��m
attribute vec3 aNormal;    //�k�V�q
varying vec4 vAmbient;//�Ω�ǻ��������ۦ⾹�����ҥ����q
varying vec4 vDiffuse;//�Ω�ǻ��������ۦ⾹�����g�����q
varying vec4 vSpecular;//�Ω�ǻ��������ۦ⾹���譱�Ϯg�����q
void pointLight(					//�w��������p�⪺��k
  in vec3 normal,				//�k�V�q
  inout vec4 ambient,			//���ҥ��̲ױj��
  inout vec4 diffuse,				//���g���̲ױj��
  inout vec4 specular,			//�譱���̲ױj��
  in vec3 lightLocation,			//������m
  in vec4 lightAmbient,			//���ҥ��j��
  in vec4 lightDiffuse,			//���g���j��
  in vec4 lightSpecular			//�譱���j��
){
  ambient=lightAmbient;			//�����o�X���ҥ����̲ױj��  
  vec3 normalTarget=aPosition+normal;	//�p���ܴ��᪺�k�V�q
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(aPosition,1)).xyz;
  newNormal=normalize(newNormal); 	//��k�V�q�W���
  //�p��q���I����v�����V�q
  vec3 eye= normalize(uCamera-(uMMatrix*vec4(aPosition,1)).xyz);  
  //�p��q���I�������m���V�qvp
  vec3 vp= normalize(lightLocation-(uMMatrix*vec4(aPosition,1)).xyz);  
  vp=normalize(vp);//�榡��vp
  vec3 halfVector=normalize(vp+eye);	//�D���u�P���u���b�V�q    
  float shininess=50.0;				//���W�סA�V�p�V����
  float nDotViewPosition=max(0.0,dot(newNormal,vp)); 	//�D�k�V�q�Pvp���I�n�P0���̤j��
  diffuse=lightDiffuse*nDotViewPosition;				//�p�ⴲ�g�����̲ױj��
  float nDotViewHalfVector=dot(newNormal,halfVector);	//�k�u�P�b�V�q���I�n 
  float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess)); 	//�譱�Ϯg���j�צ]�l
  specular=lightSpecular*powerFactor;    			//�p���譱�����̲ױj��
}
void main()  {                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1); //�ھ��`�ܴ��x�}�p�⦹��ø����I��m
    vec4 ambientTemp=vec4(0.0,0.0,0.0,0.0);
   vec4 diffuseTemp=vec4(0.0,0.0,0.0,0.0);
   vec4 specularTemp=vec4(0.0,0.0,0.0,0.0);   
   
   pointLight(normalize(aNormal),ambientTemp,diffuseTemp,specularTemp,uLightLocation,vec4(0.15,0.15,0.15,1.0),vec4(0.8,0.8,0.8,1.0),vec4(0.7,0.7,0.7,1.0));
   
   vAmbient=ambientTemp;
   vDiffuse=diffuseTemp;
   vSpecular=specularTemp;
  
}                      