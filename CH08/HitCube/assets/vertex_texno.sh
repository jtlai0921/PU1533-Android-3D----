uniform mat4 uMVPMatrix; //�`�ܴ��x�}
attribute vec3 aPosition;  //���I��m
attribute vec2 aTexCoor;    //���I���z�y��
varying vec2 vTextureCoord;  //�Ω�ǻ��������ۦ⾹���ܼ�
void main()     
{                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1); 
   //�ھ��`�ܴ��x�}�p�⦹��ø����I��m  
 
   //�N���I�����z�y�жǵ������ۦ⾹
   vTextureCoord=aTexCoor; 
}                 