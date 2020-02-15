uniform mat4 uMVPMatrix; //�`�ܴ��x�}
uniform float uStartAngle;//���ذ_�l����
uniform float uWidthSpan;//���������`���
attribute vec3 aPosition;  //���I��m
attribute vec2 aTexCoor;    //���I���z�y��
varying vec2 vTextureCoord;  //�Ω�ǻ��������ۦ⾹���ܼ�
void main()     
{            
   //�p��X�V�i��                		
   float angleSpanH=4.0*3.14159265;//���������`���   
   float startX=-uWidthSpan/2.0;//�_�lX�y��
   //�ھڤ��������`��סB���������`��פΥثe�IX�y�Ч��X�ثe�IX�y�й���������
   float currAngle=uStartAngle+((aPosition.x-startX)/uWidthSpan)*angleSpanH;
   float tz=sin(currAngle)*0.1;      
   
   //�ھ��`�ܴ��x�}�p�⦹��ø����I��m
   gl_Position = uMVPMatrix * vec4(aPosition.x,aPosition.y,tz,1); 
   vTextureCoord = aTexCoor;//�N���������z�y�жǻ��������ۦ⾹
}                      