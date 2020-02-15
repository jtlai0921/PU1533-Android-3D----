precision mediump float;
varying vec2 vTextureCoord;//�����q���I�ۦ⾹�L�Ӫ��Ѽ�
uniform sampler2D sTexture;//���z���e���
uniform int isDown;//���s�O�_�Q���U���ЧӦ�A0��ܨS�����U�A1��ܫ��U
void main()                         
{
	//���������q���z�����˥X�m���            
	vec4 finalColor = texture2D(sTexture, vTextureCoord); 
	//�ھګ��s�O�_�Q���U�A���̲ܳױm�⪺a��
	if(isDown == 1){
   		finalColor.a *= 0.3;	//���s���U�ɪ��z����
	}
	else {
		finalColor.a *= 0.8;	//���s�S���Q���U�ɪ��z����
	}
   	//�������m�⵹����
   	gl_FragColor = finalColor;
}