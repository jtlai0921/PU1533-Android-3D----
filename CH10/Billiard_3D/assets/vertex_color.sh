uniform mat4 uMVPMatrix; //�`�ܴ��x�}
attribute vec3 aPosition;  //���I��m
attribute vec4 aColor;    //���I�m��
varying  vec4 aaColor;  //�Ω�ǻ��������ۦ⾹���ܼ�
void main()     
{                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1); //�ھ��`�ܴ��x�}�p�⦹��ø����I��m
   aaColor = aColor;//�N�������m��ǻ��������ۦ⾹
}                      