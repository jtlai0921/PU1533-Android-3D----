precision mediump float;
varying vec2 vTextureCoord;//�����q���I�ۦ⾹�L�Ӫ��Ѽ�
uniform sampler2D sTexture;//���z���e���
void main()                         
{           
   //���������q���z�����˥X�m���            
   gl_FragColor = texture2D(sTexture, vTextureCoord); 
}              