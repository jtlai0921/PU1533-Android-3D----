//�P�ŵۦ⾹
uniform mat4 uMVPMatrix; //�`�ܴ��x�}
uniform float uPointSize;//�I�ؤo
attribute vec3 aPosition;  //���I��m

void main()     
{     
   //�ھ��`�ܴ��x�}�p�⦹��ø����I��m                         		
   gl_Position = uMVPMatrix * vec4(aPosition,1); 
   gl_PointSize=uPointSize;
}                 