precision mediump float;
varying vec2 vTextureCoord;//�����q���I�ۦ⾹�L�Ӫ��Ѽ�
uniform sampler2D sTexture;//���z���e���
uniform int isTransparent;//�O�_�z��
void main()                         
{
  	//���������q���z�����˥X�m���            
	vec4 finalColor = texture2D(sTexture, vTextureCoord); 
	//�ھڬO�_�z���A���̲ܳױm�⪺a��
	if(isTransparent != 0){
   		finalColor.a *= 0.8;	//����O���z����
	}
	//�������m�⵹����
   	gl_FragColor = finalColor;
}