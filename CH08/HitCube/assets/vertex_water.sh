uniform mat4 uMVPMatrix; //�`�ܴ��x�}
uniform mat4 uMMatrix;//���ܴ��x�}
attribute vec3 aPosition;  //���I��m
attribute vec2 aTexCoor;    //���I���z�y��
varying vec2 vTextureCoord;  //�Ω�ǻ��������ۦ⾹���ܼ�
void main()     
{  
   //�ھ��`�ܴ��x�}�p�⦹��ø����I��m                            		
   //gl_Position = uMVPMatrix * vec4(aPosition,1); 
    vec4 temp_Position=uMMatrix * vec4(aPosition,1); 
    float currX=temp_Position.x; 
    float currY=temp_Position.y;
    float currZ=temp_Position.z;
    float angle;
	if(currX<-0.42)
    {
    angle=(currX/0.7+1.0)*150.0;
    }else if(currX<-0.14)
    {
   // angle=(currX-0.28)/0.7*65.0;
   angle=60.0-(currX/0.7+0.6)*150.0;
    }
    else if(currX<0.14)
    {
   // angle=(currX+0.28)/0.7*65.0;
   angle=(currX/0.7+0.2)*150.0;
    }else if(currX<0.42)
    {
    //angle=(currX+0.28)/0.7*65.0;
    angle=60.0-(currX/0.7-0.2)*150.0;
    }else if(currX<0.7)
    {
    //angle=currX/0.7*65.0;
    angle=(currX/0.7-0.6)*150.0;
    }
    
    angle=radians(angle); 
    currY=cos(angle)*aPosition.y;
    currZ=sin(angle)*aPosition.z;
 
    gl_Position = uMVPMatrix * vec4(aPosition.x,currY,currZ,1);  
   //�N���I�����z�y�жǵ������ۦ⾹
   vTextureCoord=aTexCoor;
}                 