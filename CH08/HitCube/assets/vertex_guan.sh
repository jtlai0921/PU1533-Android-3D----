uniform mat4 uMVPMatrix; //�`�ܴ��x�}
uniform mat4 uMMatrix; //���ܴ��x�}
attribute vec3 aPosition;  //���I��m
attribute vec2 aTexCoor;    //���I���z�y��
varying vec2 vTextureCoord;  //�Ω�ǻ��������ۦ⾹���ܼ�
varying float currZ;
void main()     
{     
   //�ھ��`�ܴ��x�}�p�⦹��ø����I��m                        		
   gl_Position = uMVPMatrix * vec4(aPosition,1); 
    
   //vec4 currP=uMMatrix*vec4(aPosition,1); 
   //currZ=currP.z;
   currZ=gl_Position.z;
   //�N���I�����z�y�жǵ������ۦ⾹
   vTextureCoord=aTexCoor;
}                 