precision mediump float;
varying  vec4 aaColor; //�����q���I�ۦ⾹�L�Ӫ��Ѽ�
void main()                         
{
	vec4 finalColor = aaColor;
	finalColor.a = 0.9;//���ܱm�i�����z����
	gl_FragColor = finalColor;//���������m���
}              