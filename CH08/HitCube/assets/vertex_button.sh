uniform mat4 uMVPMatrix; //�`�ܴ��x�}
uniform mat4 uMMatrix; //���ܴ��x�}
attribute vec3 aPosition;  //���I��m
attribute vec4 aColor;  //���I�m��
varying vec4 vColor;  //�Ω�ǻ��������ۦ⾹���ܼ�
//varying float currAngle;  //�ثe����
varying vec3 vPosition;
void main()     
{     
   //�ھ��`�ܴ��x�}�p�⦹��ø����I��m                        		
   gl_Position = uMVPMatrix * vec4(aPosition,1);    
   //vec4 currP=uMMatrix*vec4(aPosition,1); 
   //currZ=currP.z;
 //  vec3 currP=gl_Position.xyz;
   //currP=aPosition-vec3(0.0,0.0,0.0);
   //currP=normalize(currP);
   //currAngle=dot(currP,vec3(0.0,1.0,0.0)-vec3(0.0,0.0,0.0));
   //currAngle=acos(currAngle);
   //currAngle=degrees(currAngle);
     //vPosition=currP.xyz;
    vPosition=aPosition;
   vColor = aColor;//�N�������m��ǻ��������ۦ⾹
   
}                 